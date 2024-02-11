package cd.bnb.syprome.configurations;

import cd.bnb.syprome.filters.JwtFilter;
import cd.bnb.syprome.services.UserJwtService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{

   private UserJwtService servive;
   private JwtFilter jwtFilter;
    @Value("${allowedOrigins}")
    private List<String> domains;
    private static final String[] AUTH_WHITELIST = {
            "/api/**"
    };

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(servive);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(request-> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(Arrays.asList("*"));
            configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
            configuration.setAllowedHeaders(Arrays.asList("Authorization","Cache-Control","Content-Type"));
            return configuration;
        }).and().csrf().disable().authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll().anyRequest().authenticated().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().headers().frameOptions().disable().and().headers()
                .defaultsDisabled()
                .contentTypeOptions();
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }




}