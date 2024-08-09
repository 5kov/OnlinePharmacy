package bg.softuni.onlinepharmacy.config;


import bg.softuni.onlinepharmacy.repository.UserRepository;
import bg.softuni.onlinepharmacy.service.impl.PharmacyUserDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
        .authorizeHttpRequests(
            // Setup which URL-s are available to who
            authorizeRequests ->
                authorizeRequests
                    // all static resources to "common locations" (css, images, js) are available to anyone
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                    // some more resources for all users
                    .requestMatchers("/", "/login", "/register", "/error").permitAll()
                    .requestMatchers("/administrator-manage-users", "/delete-user", "/toggle-role", "/administrator-add-active-ingredient",
                            "/administrator-manage-active-ingredients", "/deleteIngredient/{id}", "/administrator-add-medicines",
                            "/administrator-manage-medicines", "/save", "/delete/**", "/data/**", "/administrator-add-interactions",
                            "/administrator-manage-interactions", "/delete-interaction/**", "/orders", "/news/add", "/news/delete").hasRole("ADMIN")
                    // all other URL-s should be authenticated.
                    .anyRequest()
                    .authenticated()
        )
        .formLogin(formLogin ->
            formLogin
                // Where is our custom login form?
                .loginPage("/login")
                // what is the name of the username parameter in the Login POST request?
                .usernameParameter("username")
                // what is the name of the password parameter in the Login POST request?
                .passwordParameter("password")
                // What will happen if the login is successful
                .defaultSuccessUrl("/index", true)
                // What will happen if the login fails
                .failureForwardUrl("/users/login-error")
        )
        .logout(
            logout ->
                logout
                    // what is the logout URL?
                    .logoutUrl("/logout")
                    // Where to go after successful logout?
                    .logoutSuccessUrl("/index")
                    // invalidate the session after logout.
                    .invalidateHttpSession(true)
        )
        .build();
  }

  @Bean
  public PharmacyUserDetailsService userDetailsService(UserRepository userRepository) {
    return new PharmacyUserDetailsService(userRepository);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return Pbkdf2PasswordEncoder
        .defaultsForSpringSecurity_v5_8();
  }
}
