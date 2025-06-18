package ads.dacha.services.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;


@Configuration
@EnableWebSecurity
public class MvcConfig implements WebMvcConfigurer{
    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath + "/")
                .setCachePeriod(3600);
            

        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600);        
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/auth", "/reg", "/css/**", "/js/**", "/uploads/**", "/static/**", "/error").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
            .loginPage("/auth")
            .loginProcessingUrl("/login")  // Обработчик формы входа
            .usernameParameter("email")   // Указываем имя поля email
            .passwordParameter("password") // Указываем имя поля пароля
            .defaultSuccessUrl("/", true) // Обязательный редирект
            .failureUrl("/auth?error=true")
            .permitAll()
            )
            .requestCache(request -> request
                .requestCache(new NullRequestCache())
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/auth?logout=true")
                .permitAll()
            )
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/auth"))
            )
            .csrf().disable(); // Отключено для тестирования

        return http.build();
    }

    /*@Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }*/

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
    
}
