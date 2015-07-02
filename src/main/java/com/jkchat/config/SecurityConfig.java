package com.jkchat.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	AuthenticationProvider customAuthenticationProvider;
	@Autowired
	SimpleUrlLogoutSuccessHandler lsh;
	@Autowired
	UserDetailsService service;
	@Autowired
	DataSource dataSource;
	@Autowired
	AuthenticationSuccessHandler authenticationSuccessHandler;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.authenticationProvider(customAuthenticationProvider);
	}

	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
		db.setDataSource(dataSource);
		return db;
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin().loginPage("/login")
				.loginProcessingUrl("/perfom_login").permitAll().and()
				.authorizeRequests().antMatchers("/register").permitAll().and()
				.authorizeRequests().anyRequest().authenticated().and()
				.sessionManagement().maximumSessions(1)
				.maxSessionsPreventsLogin(true)
				.sessionRegistry(sessionRegistry()).and();
		http.logout().logoutSuccessHandler(lsh).logoutSuccessUrl("/").and()
				.csrf().and().rememberMe()
				.tokenRepository(persistentTokenRepository())
				.userDetailsService(service).tokenValiditySeconds(1209600);
		http.rememberMe().authenticationSuccessHandler(
				authenticationSuccessHandler);
		http.formLogin().successHandler(authenticationSuccessHandler);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}

}
