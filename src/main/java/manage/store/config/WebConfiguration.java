package manage.store.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Value("${cors.allow-ip}")
    private String[] allowIps;

    public static final String RESPONSE_CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/docs/**")
                .addResourceLocations("classpath:/static/docs/");
    }

}
