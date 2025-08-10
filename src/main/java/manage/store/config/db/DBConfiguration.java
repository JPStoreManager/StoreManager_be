package manage.store.config.db;

import manage.store.config.db.typeHandler.common.*;
import manage.store.config.db.typeHandler.sales.MoneyTypeHandler;
import manage.store.config.db.typeHandler.user.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DB 연결을 사용하는 모듈이 추가될 때마다 모듈을 수동으로 추가해야 한다.
 */
@Configuration
@MapperScan(
    basePackages = {"manage.store.**.repository.**"},
    annotationClass = Mapper.class
)
public class DBConfiguration {

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // 1. 멀티 모듈 환경에서 여러 모듈에 퍼져있는 xml을 조회하기 위해 classpath*:mapper/*.xml 사용
        // 2. 멀티 모듈 테스트 시에는 모든 모듈의 jar가 빌드된 이후 테스트가 실행되게 된다.
        //    그렇게 되면 jar에 있는 xml과 classpath에 있는 xml이 모두 조회되어 중복 fragment 등록 익셉션이 발생한다.
        //    따라서, 중복된 mapper xml을 제거하기 위해 "모듈:mapper 상대 경로"를 키로 사용하여 중복을 제거한다.
        Resource[] mapperXmlResources = Arrays.stream(resolver.getResources("classpath:mapper/**/*.xml"))
                                         .filter(distinctByKey(DBConfiguration::simpleKey))
                                         .toArray(Resource[]::new);
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(mapperXmlResources);
        SqlSessionFactory sqlSessionFactory = factoryBean.getObject();
        setMyBatisConfig(sqlSessionFactory);
        return sqlSessionFactory;
    }

    /**
     * Resource의 URL에서 모듈 이름과 mapper 상대 경로를 추출하여 간단한 키를 생성한다.
     * 예: "StoreManager_be_user:mapper/user.xml"
     *
     * @param resource MyBatis Mapper XML 파일의 Resource
     * @return 모듈 이름:mapper 상대 경로
     */
    private static String simpleKey(Resource resource) {
        try {
            String url = resource.getURL().toString();
            // 1) 모듈 이름 추출 (StoreManager_be_XXX)
            Pattern modulePattern = Pattern.compile("StoreManager_be_[^/]+");
            Matcher moduleMatcher = modulePattern.matcher(url);
            String moduleName = moduleMatcher.find() ? moduleMatcher.group() : url;

            // 2) Mapper 내부 경로(“mapper/…xml”) 추출
            //    "/mapper/" 위치를 찾아서 그 뒤의 문자열을 가져온다.
            String relativePath;
            int idxMapper = url.indexOf("/mapper/");
            if (idxMapper >= 0) {
                // “/mapper/” 뒤(“mapper/...”)만 자른다. 맨 앞 “/”를 제거하기 위해 +1.
                relativePath = url.substring(idxMapper + 1);
            } else {
                // 비정상 URL이라면, 그냥 파일 이름만
                relativePath = resource.getFilename();
            }

            // 3) 모듈이름 + ":" + mapper 상대경로
            return moduleName + ":" + relativePath;

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * 스트림 중복 제거용 헬퍼
     * keyExtractor 결과가 중복되면 두 번째 이후는 필터링된다.
     */
    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    /**
     * MyBatis 설정 적용
     * @param sqlSessionFactory
     */
    private void setMyBatisConfig(SqlSessionFactory sqlSessionFactory) {
        if(sqlSessionFactory == null) return;

        org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);
        setMyBatisTypeHandlers(configuration);
    }

    /**
     * MyBatis TypeHandler 설정
     * @param configuration
     */
    private void setMyBatisTypeHandlers(org.apache.ibatis.session.Configuration configuration) {
        if (configuration == null) return;

        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        // common
        typeHandlerRegistry.register(CommonCodeCdTypeHandler.class);
        typeHandlerRegistry.register(CommonCodeGrpCdTypeHandler.class);
        typeHandlerRegistry.register(DbUpdateDateTypeHandler.class);
        typeHandlerRegistry.register(SortOrderTypeHandler.class);
        typeHandlerRegistry.register(DeleteFlagTypeHandler.class);

        // sales
        typeHandlerRegistry.register(MoneyTypeHandler.class);

        // user
        typeHandlerRegistry.register(EmailTypeHandler.class);
        typeHandlerRegistry.register(OtpNoTypeHandler.class);
        typeHandlerRegistry.register(PhoneNoTypeHandler.class);
        typeHandlerRegistry.register(ResidentRegistNoTypeHandler.class);
        typeHandlerRegistry.register(SalaryTypeHandler.class);
        typeHandlerRegistry.register(UserAuthCodeTypeHandler.class);
        typeHandlerRegistry.register(UserIdTypeHandler.class);
        typeHandlerRegistry.register(UserNameTypeHandler.class);
        typeHandlerRegistry.register(WorkDateTypeHandler.class);
        typeHandlerRegistry.register(WorkStatusCodeTypeHandler.class);
    }
}
