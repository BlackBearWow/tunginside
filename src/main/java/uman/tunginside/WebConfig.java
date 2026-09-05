package uman.tunginside;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .allowedMethods("*");
    }

    // SPA에서 새로고침 라우팅 문제 해결. static에 없는 리소스면 index.html을 반환한다.
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource resource = location.createRelative(resourcePath);
                        // 요청한 리소스가 존재하면 그대로 반환
                        if(resource.exists() && resource.isReadable()) {
                            return resource;
                        }
                        // /api요청은 제외하고, 그 외에 찾을 수 없는 요청은 index.html 반환
                        if(resourcePath.startsWith("api/")) {
                            return null;
                        }
                        return new ClassPathResource("static/index.html");
                    }
                });
    }
}
