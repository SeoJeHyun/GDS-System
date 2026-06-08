package gds;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class WebCorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        config.setAllowCredentials(true); // 쿠키 및 인증 헤더(Authorization) 허용
        config.addAllowedOriginPattern("*"); // 모든 프론트엔드 주소(Origin) 무조건 허용
        config.addAllowedHeader("*"); // 모든 헤더 통과
        config.addAllowedMethod("*"); // OPTIONS(Preflight)를 포함한 모든 메서드 통과
        config.addExposedHeader("Content-Disposition"); // 파일 다운로드용
        
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}