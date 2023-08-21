package fr.cekogha.cashcardapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeHttpRequests().requestMatchers("/cashcards/**").hasRole("CARD-OWNER").and().csrf()
				.disable().httpBasic();
		return httpSecurity.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService testOnlyUsers(PasswordEncoder passwordEncoder) {
		User.UserBuilder users = User.builder();
		UserDetails christianDetails = users.username("christian").password(passwordEncoder.encode("abc123"))
				.roles("CARD-OWNER").build();

		UserDetails hanksDetails = users.username("hanks-owns-no-cards")
				.password(passwordEncoder.encode("hankspassword")).roles("NON-OWNER").build();

		UserDetails karlDetails = users.username("karl").password(passwordEncoder.encode("karlpassword"))
				.roles("CARD-OWNER").build();
		return new InMemoryUserDetailsManager(christianDetails, hanksDetails, karlDetails);
	}
}
