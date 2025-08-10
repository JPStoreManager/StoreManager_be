# StoreManager_be
Store manager backend Server

## Pre-requisites
- 환경 변수 설정
  - `DB_URL`: 데이터베이스 URL
  - `DB_USERNAME`: 데이터베이스 사용자 이름
  - `DB_PASSWORD`: 데이터베이스 비밀번호
  - `GOOGLE_EMAIL`: 구글 이메일 주소
  - `GOOGLE_APP_PW`: 구글 앱 비밀번호

# Source Management
## How to push to ECR
```shell
# 로그인 
aws ecr get-login-password --region <region> | docker login --username AWS --password-stdin <account-id>.dkr.ecr.<region>.amazonaws.com

# 빌드한 Docker 이미지에 ECR 태그 추가
docker tag <빌드한 태그 ID> <account-id>.dkr.ecr.<region>.amazonaws.com/store-manager-prd:latest

# ECR에 푸시
docker push <account-id>.dkr.ecr.<region>.amazonaws.com/store-manager-prd:latest
```

# Build and Run
## How to build Docker image
### Local
```shell
docker build \
  --secret id=db_url,env=DB_URL \
  --secret id=db_username,env=DB_USERNAME \
  --secret id=db_password,env=DB_PASSWORD \
  --secret id=google_email,env=GOOGLE_EMAIL \
  --secret id=google_app_pw,env=GOOGLE_APP_PW \
  --progress=plain  --platform linux/arm64 \
  -t store-manager:local .
```

### Production
```shell
docker build \
  --secret id=db_url,env=DB_URL \
  --secret id=db_username,env=DB_USERNAME \
  --secret id=db_password,env=DB_PASSWORD \
  --secret id=google_email,env=GOOGLE_EMAIL \
  --secret id=google_app_pw,env=GOOGLE_APP_PW \
  --progress=plain  --platform linux/amd64 \
  -t store-manager:latest .
```

## How to run With Docker
```shell
docker run \
       -p 8080:8080 \
       -e DB_URL="$DB_URL" \
       -e DB_USERNAME="$DB_USERNAME" \
       -e DB_PASSWORD="$DB_PASSWORD" \
       -e GOOGLE_EMAIL="$GOOGLE_EMAIL" \
       -e GOOGLE_APP_PW="$GOOGLE_APP_PW" \
       store-manager:local
```