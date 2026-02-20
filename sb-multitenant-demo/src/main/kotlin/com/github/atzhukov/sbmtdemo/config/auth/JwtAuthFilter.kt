package com.github.atzhukov.sbmtdemo.config.auth

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
	private val jwtService: JwtService
): OncePerRequestFilter() {

	private val log = LoggerFactory.getLogger(JwtAuthFilter::class.java)

	override fun doFilterInternal(
		request: HttpServletRequest,
		response: HttpServletResponse,
		filterChain: FilterChain
	) {
		val header: String? = request.getHeader(HttpHeaders.AUTHORIZATION)

		if (header != null && header.startsWith("Bearer ")) {
			try {
				val token = header.substringAfter("Bearer ")
				val jwtAuth = jwtService.parseToken(token, request)
				if (SecurityContextHolder.getContext().authentication == null) {
					SecurityContextHolder.getContext().authentication = jwtAuth
					log.info("Successfully authenticated {}", jwtAuth)
				}
			} catch (ex: Exception) {
				logger.error("Error while authenticating with a JWT token", ex)
			}
		}

		filterChain.doFilter(request, response)
	}


}
