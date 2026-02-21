package com.github.atzhukov.sbmtdemo.controller

import com.github.atzhukov.sbmtdemo.data.entity.Tenant
import com.github.atzhukov.sbmtdemo.data.entity.User
import com.github.atzhukov.sbmtdemo.service.AuthService
import com.github.atzhukov.sbmtdemo.service.DocumentService
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(
	private val documentService: DocumentService,
	private val authService: AuthService
): Api {

	override fun getDocuments(): List<Api.Response.Document>
			= documentService.getAllDocuments().map { it.toDto() }

	override fun signUp(request: Api.Request.SignUp) {
		val user = User(
			login = request.credentials.login,
			password = request.credentials.password, // will be hashed by authService
			name = request.name,
			tenants = request.tenants.map { Tenant(id = it) }.toSet()
		)
		authService.createUser(user)
	}

	override fun signIn(credentials: Api.Request.Credentials): String
			= authService.signIn(credentials.login, credentials.password)

}
