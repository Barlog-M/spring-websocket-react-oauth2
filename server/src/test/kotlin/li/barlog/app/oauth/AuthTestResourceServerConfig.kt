package li.barlog.app.oauth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.TokenStore

@Configuration
@EnableResourceServer
open class AuthTestResourceServerConfig : ResourceServerConfigurerAdapter() {
	@Autowired
	private lateinit var tokenStore: TokenStore

	override fun configure(http: HttpSecurity) {
		// @formatter:off
		http
			.requestMatchers().antMatchers("/api/**")
			.and()
				.authorizeRequests()
					.antMatchers("/api/**").hasRole("USER")
					.anyRequest().authenticated()
			.and()
				.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.csrf().disable()
		// @formatter:on
	}

	override fun configure(resources: ResourceServerSecurityConfigurer) {
		resources.tokenStore(tokenStore)
	}
}
