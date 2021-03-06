package tech.devcrazelu.url_shortener.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tech.devcrazelu.url_shortener.filters.ExceptionHandlingFilter;
import tech.devcrazelu.url_shortener.filters.JwtRequestFilter;
import tech.devcrazelu.url_shortener.filters.RequestValidationFilter;
import tech.devcrazelu.url_shortener.services.UserService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtRequestFilter jwtFilter;
    @Autowired
    private RequestValidationFilter validationFilter;
    @Autowired
    private ExceptionHandlingFilter exceptionHandlingFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests().antMatchers("/createAccount").permitAll()
                .antMatchers("/createAccount/**/{email}").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/login/**/{email}").permitAll()
                .antMatchers("/{shortUrl}").permitAll()
                .anyRequest().authenticated()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(validationFilter, JwtRequestFilter.class);
        http.addFilterBefore(exceptionHandlingFilter, RequestValidationFilter.class);

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }
}
