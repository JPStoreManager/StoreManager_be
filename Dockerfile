FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY src ./src

# 보안을 위한 시크릿 파일을 컨테이너에 마운트
RUN --mount=type=secret,id=db_url \
    --mount=type=secret,id=db_username \
    --mount=type=secret,id=db_password \
    --mount=type=secret,id=google_email \
    --mount=type=secret,id=google_app_pw \
    DB_URL=$(cat /run/secrets/db_url) \
    DB_USERNAME=$(cat /run/secrets/db_username) \
    DB_PASSWORD=$(cat /run/secrets/db_password) \
    GOOGLE_EMAIL=$(cat /run/secrets/google_email) \
    GOOGLE_APP_PW=$(cat /run/secrets/google_app_pw) \
    ./gradlew build


FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder /app/build/libs/*-SNAPSHOT.jar app.jar

# 애플리케이션이 사용할 포트 명시
EXPOSE 8080

# 컨테이너가 시작될 때 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]