package com.github.atzhukov.sbmtdemo.controller.impl

import com.github.atzhukov.sbmtdemo.config.auth.Jwt
import com.github.atzhukov.sbmtdemo.config.auth.UserWithDetails
import com.github.atzhukov.sbmtdemo.controller.Api
import com.github.atzhukov.sbmtdemo.data.entity.Document
import com.github.atzhukov.sbmtdemo.data.entity.User
import com.github.atzhukov.sbmtdemo.service.AuthService
import com.github.atzhukov.sbmtdemo.service.DocumentService
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class Controller(
	private val documentService: DocumentService,
	private val authService: AuthService,
	private val authenticationManager: AuthenticationManager,
): Api {

	override fun getDocuments(): List<Document>
			= documentService.getAllDocuments()

	override fun signUp(credentials: Api.Credentials): String {
		if (authService.existsByLogin(credentials.login)) {
			throw ResponseStatusException(HttpStatus.CONFLICT, "Username already exists")
		}

		val user = User(
			login = credentials.login,
			name = credentials.login + "@@@" // FIXME
		)
		authService.create(user, credentials.password)

		val auth = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(credentials.login, credentials.password))
		val principal = auth.principal as UserWithDetails

		return Jwt.of(principal.username)
	}

}
