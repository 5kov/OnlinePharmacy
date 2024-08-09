package bg.softuni.onlinepharmacy.config;


import bg.softuni.onlinepharmacy.service.JwtService;
import bg.softuni.onlinepharmacy.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;

import java.util.Map;

@Configuration
public class RestConfig {
    @Bean("newsRestClient")
    public RestClient newsRestClient(NewsApiConfig newsApiConfig ,
                                     ClientHttpRequestInterceptor requestInterceptor) {
        return RestClient
                .builder()
                .baseUrl(newsApiConfig.getBaseUrl())
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .requestInterceptor(requestInterceptor)
                .build();
    }

    @Bean
    public ClientHttpRequestInterceptor requestInterceptor(UserService userService,
                                                           JwtService jwtService) {
        return (r, b, e) -> {
            // put the logged user details into bearer token
            userService
                    .getCurrentUser()
                    .ifPresent(mud -> {
                        String bearerToken = jwtService.generateToken(
                                mud.getUuid().toString(),//
                                Map.of(
                                        "roles",
                                        mud.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
                                )
                        );

                        System.out.println("BEARER TOKEN: " + bearerToken);

                        r.getHeaders().setBearerAuth(bearerToken);
                    });

            return e.execute(r, b);
        };
    }

}
