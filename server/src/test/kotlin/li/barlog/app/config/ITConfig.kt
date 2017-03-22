package li.barlog.app.config

import li.barlog.app.App
import li.barlog.app.config.oauth.AuthorizationServerConfig
import li.barlog.app.config.oauth.ResourceServerConfig
import li.barlog.app.csrf.CsrfTestConfig
import li.barlog.app.oauth.AuthTestConfig
import li.barlog.app.oauth.AuthTestResourceServerConfig
import org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType

@EnableAutoConfiguration(
	exclude = arrayOf(
		OAuth2AutoConfiguration::class,
		SecurityAutoConfiguration::class,
		ManagementWebSecurityAutoConfiguration::class
	)
)
@ComponentScan(
	basePackages = arrayOf("li.barlog.app"),
	excludeFilters = arrayOf(
		ComponentScan.Filter(
			type = FilterType.ASSIGNABLE_TYPE,
			classes = arrayOf(
				App::class,
				AppConfig::class,
				SecurityConfig::class,
				AuthenticationConfig::class,
				AuthorizationServerConfig::class,
				ResourceServerConfig::class,
				CsrfTestConfig::class,
				AuthTestConfig::class,
				AuthTestResourceServerConfig::class
			)
		)
	)
)
@Configuration
open class ITConfig
