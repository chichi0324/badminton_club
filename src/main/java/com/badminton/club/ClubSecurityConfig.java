package com.badminton.club;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 開啟方法註解支持，我們設置prePostEnabled = true是為了後面能夠使用hasRole()這類表達式
 */
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
@EnableWebSecurity
public class ClubSecurityConfig extends WebSecurityConfigurerAdapter {

//	@Autowired
//	DataSource dataSource;
	
	@Autowired
    UserDetailsService clubUserDetailsService;

	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		
//		auth.jdbcAuthentication().dataSource(dataSource);
		
		auth.userDetailsService(clubUserDetailsService);
		

//		auth //Builder Design Pattern
//		.inMemoryAuthentication() //自訂Runtime時的使用者帳號
//			.withUser("john") //新增user
//			.password("{noop}test123") //指定密碼
//			.roles("MEMBER") //指派權限群組
//			.and() //再新增使用者
//			.withUser("mary")
//			.password("{noop}test123")
//			.roles("MEMBER", "MANAGER")
//			.and() //再新增使用者
//			.withUser("susan")
//			.password("{noop}test123")
//			.roles("MEMBER", "MANAGER","SURPERMANAGER");
		
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
		    .antMatchers("/activity/search/detail/apply**").hasRole("MEMBER")
			.antMatchers("/member/**").hasRole("MEMBER")
			.antMatchers("/manager/**").hasRole("MANAGER")
			.antMatchers("/superManager/**").hasRole("SURPERMANAGER")
			.and()
			.formLogin()
				.loginPage("/showMyLoginPage")
				.loginProcessingUrl("/login")
				.permitAll()
			.and()
			.logout()
			.permitAll()
			.and()
			.exceptionHandling().accessDeniedPage("/access-denied");
		
	}
	   
		
}







