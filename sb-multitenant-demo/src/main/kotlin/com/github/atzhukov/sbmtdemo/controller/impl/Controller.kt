package com.github.atzhukov.sbmtdemo.controller.impl

import com.github.atzhukov.sbmtdemo.config.auth.JwtService
import com.github.atzhukov.sbmtdemo.config.auth.UserWithDetails
import com.github.atzhukov.sbmtdemo.controller.Api
import com.github.atzhukov.sbmtdemo.data.entity.Document
import com.github.atzhukov.sbmtdemo.data.entity.Tenant
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
	private val authService: AuthService
): Api {

	override fun getDocuments(): List<Document>
			= documentService.getAllDocuments()

	override fun signUp(request: Api.SignUpRequest) {
		val user = User(
			login = request.credentials.login,
			password = request.credentials.password, // will be hashed by authService
			name = request.name,
			tenants = request.tenants.map { Tenant(id = it) }.toSet()
		)
		authService.createUser(user)
	}

	override fun signIn(credentials: Api.Credentials): String
			= authService.signIn(credentials.login, credentials.password)

}
