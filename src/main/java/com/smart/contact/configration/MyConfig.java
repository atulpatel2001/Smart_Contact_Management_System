package com.smart.contact.configration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
public class MyConfig{

	@Autowired
	private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

	@Autowired
	private CustomLogoutSuccessHandler customLogoutSuccessHandler;
    @Bean
    UserDetailsService getUserDetailsService() {
		return new UserDetailServiceImple();
	}

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

    @Bean
    DaoAuthenticationProvider getDaoAuthenticationProvider() {
		DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(this.getUserDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		
		return authenticationProvider;			
	}

    
	@Bean
     SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		
//    	httpSecurity.csrf(csrf -> csrf.disable()).authorizeHttpRequests(authorize -> authorize
//                .requestMatchers("/**").permitAll()
//        .requestMatchers("/admin/**").hasRole("ADMIN")
//        .requestMatchers("/user/**").hasRole("USER")
//        .anyRequest().authenticated()).formLogin(formLogin -> formLogin.loginPage("/signin"));




		httpSecurity.csrf(AbstractHttpConfigurer::disable)
		.authorizeHttpRequests(auth->auth.requestMatchers("/admin/**")
				.hasRole("ADMIN")
				.requestMatchers("/user/**")
				.hasRole("USER")
				.requestMatchers("/**")
				.permitAll()
				.anyRequest().authenticated()).formLogin(form-> {
					form.loginPage("/signin").loginProcessingUrl("/doLogin").defaultSuccessUrl("/user/index", true).successHandler(this.customAuthenticationSuccessHandler);
				}).logout(log ->log.logoutSuccessHandler(this.customLogoutSuccessHandler));

		return httpSecurity.build();
	}
} 

