package io.liter.web.api.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Slf4j
@Configuration
@EnableWebFlux
public class WebConfig implements WebFluxConfigurer {

	/*@Override
	public void addCorsMappings(CorsRegistry registry) {

		registry.addMapping("/auth/token");
		registry.addMapping("/sample/**");
	}*/


    /*@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/auth/token");
        registry.addMapping("/auth/**").allowedMethods("GET","PUT","POST","OPTIONS");
        registry.addMapping("/user/**").allowedMethods("GET","PUT","POST","OPTIONS");
        registry.addMapping("/sample/**").allowedMethods("GET","PUT","POST","OPTIONS");
        registry.addMapping("/review/**").allowedMethods("GET","PUT","POST","OPTIONS");
    }*/

    @Bean
    public CorsWebFilter corsFilter() {
        log.info("]-----] corsFilter [-----[");

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}