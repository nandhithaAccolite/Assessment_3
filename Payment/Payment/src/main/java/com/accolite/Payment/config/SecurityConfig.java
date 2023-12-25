package com.accolite.Payment.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/users/register/**").permitAll()
                .antMatchers("/users/getSecret/**").permitAll()/// Allow public access to registration
                .antMatchers("/users/exchange-token").permitAll()
                .antMatchers("/users/enroll-offline-payment/**").permitAll()
                .antMatchers("/users/check-functionality-eligibility").permitAll()
                .antMatchers("/wallet/add-funds/**").permitAll()
                .antMatchers("/wallet/balance").permitAll()
                .antMatchers("/wallet/generate-offline-payment-codes/**").permitAll()
                .antMatchers("/payments/online/**").permitAll()
                .antMatchers("/users/switch-offline-payments").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .csrf().disable();  // Disable CSRF for simplicity, consider enabling in production
    }

}

