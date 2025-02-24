package com.kjh.wsd.saramIn_crawling.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger API 문서 설정 클래스
 * OpenAPI 3.0 기반 API 문서를 자동 생성합니다.
 */
@Configuration
public class SwaggerConfig {

    /**
     * OpenAPI 문서 구성을 설정합니다.
     *
     * @return {@link OpenAPI} 객체
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SaramIn Crawling API")
                        .version("1.0")
                        .description("SaramIn Crawling 프로젝트를 위한 API 문서")
                )
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("BearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        ));
    }
}
