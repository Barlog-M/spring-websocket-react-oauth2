package li.barlog.app.oauth

import com.fasterxml.jackson.databind.ObjectMapper
import li.barlog.app.TestController
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(
	classes = arrayOf(
		AuthTestConfig::class,
		TestController::class
	),
	webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AuthIT {
	@Autowired
	private lateinit var mapper: ObjectMapper

	@Autowired
	private lateinit var restTemplate: TestRestTemplate

	@LocalServerPort
	private lateinit var port: Integer

	@Test
	fun authentication() {
		val url = createAuthUrl(port.toInt(), "foo", "bar")

		val response = restTemplate.postForEntity(url, null, String::class.java)
		assertEquals(HttpStatus.OK, response.statusCode)

		val results = mapper.readValue(response.body, Map::class.java)
		val token = results["access_token"]
		assertFalse(token.toString().isEmpty())
	}

	@Test
	fun postWithoutAuthentication() {
		val response = restTemplate.postForEntity("/api/test", null, String::class.java)
		assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
	}

	@Test
	fun postWithAuthentication() {
		val access_token = requestToken().first

		val request = createRequestWithToken(access_token)

		val response = restTemplate.exchange("/api/test", HttpMethod.POST, request, String::class.java)
		assertEquals(HttpStatus.OK, response.statusCode)

		val map = mapper.readValue(response.body, Map::class.java)
		assertEquals("ok", map["status"])
	}

	@Test
	fun tokenRefresh() {
		val refresh_token = requestToken().second

		val url = createTokenRefreshUrl(port.toInt(), refresh_token)

		val e = restTemplate.postForEntity(url, null, String::class.java)
		val tokenMap = mapper.readValue(e.body, Map::class.java)
		val new_access_token = tokenMap["access_token"].toString()

		val request = createRequestWithToken(new_access_token)
		val response = restTemplate.exchange("/api/test", HttpMethod.POST, request, String::class.java)
		assertEquals(HttpStatus.OK, response.statusCode)

		val map = mapper.readValue(response.body, Map::class.java)
		assertEquals("ok", map["status"])
	}

	private fun createRequestWithToken(token: String): HttpEntity<Void> {
		val headers = HttpHeaders()
		headers.contentType = MediaType.APPLICATION_JSON_UTF8
		headers.accept = listOf(MediaType.APPLICATION_JSON_UTF8)
		headers.add("Authorization", "Bearer $token")

		val request = HttpEntity<Void>(headers)
		return request
	}

	private fun requestToken(): Pair<String, String> {
		val url = createAuthUrl(port.toInt(), "foo", "bar")

		val e = restTemplate.postForEntity(url, null, String::class.java)
		val tokenMap = mapper.readValue(e.body, Map::class.java)
		val access_token = tokenMap["access_token"].toString()
		val refresh_token = tokenMap["refresh_token"].toString()
		return Pair(access_token, refresh_token)
	}
}
