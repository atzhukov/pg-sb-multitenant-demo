package com.github.atzhukov.sbmtdemo.controller.impl

import com.github.atzhukov.sbmtdemo.controller.Api
import com.github.atzhukov.sbmtdemo.data.entity.Document
import com.github.atzhukov.sbmtdemo.data.entity.User
import com.github.atzhukov.sbmtdemo.service.AuthService
import com.github.atzhukov.sbmtdemo.service.DocumentService
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(
	private val documentService: DocumentService,
	private val authService: AuthService,
	private val authenticationManager: AuthenticationManager
): Api {

	override fun getDocuments(): List<Document>
			= documentService.getAllDocuments()

	override fun findUserByLogin(login: String): User?
			= authService.getByLogin(login)

	override fun login(login: String, password: String) {
		val authRequest = UsernamePasswordAuthenticationToken(login, password)
		val authResponse = authenticationManager.authenticate(authRequest)
	}

}
