package com.github.atzhukov.sbmtdemo.controller.impl

import com.github.atzhukov.sbmtdemo.config.auth.JwtUtils
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
	private val jwtUtils: JwtUtils
): Api {

	override fun getDocuments(): List<Document>
			= documentService.getAllDocuments()

	override fun signUp(credentials: Api.Credentials) {
		if (authService.existsByLogin(credentials.login)) {
			throw ResponseStatusException(HttpStatus.CONFLICT, "Username already exists")
		}
		val user = User(
			login = credentials.login,
			name = credentials.login + " (Name)"
		)
		authService.create(user, credentials.password)
	}

	override fun signIn(credentials: Api.Credentials): String {
		val credentialsToken = UsernamePasswordAuthenticationToken(
			credentials.login,
			credentials.password
		)

		val auth = authenticationManager.authenticate(credentialsToken)
		val principal = auth.principal as UserWithDetails
		return jwtUtils.createToken(principal)
	}

}
