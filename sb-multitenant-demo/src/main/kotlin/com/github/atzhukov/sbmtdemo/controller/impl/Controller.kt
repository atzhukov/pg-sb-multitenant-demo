package com.github.atzhukov.sbmtdemo.controller.impl

import com.github.atzhukov.sbmtdemo.controller.Api
import com.github.atzhukov.sbmtdemo.data.entity.Document
import com.github.atzhukov.sbmtdemo.data.entity.User
import com.github.atzhukov.sbmtdemo.service.AuthService
import com.github.atzhukov.sbmtdemo.service.DocumentService
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(
	private val documentService: DocumentService,
	private val authService: AuthService,
): Api {

	override fun getDocuments(): List<Document>
			= documentService.getAllDocuments()

	override fun findUserByLogin(login: String): User?
			= authService.getByLogin(login)

}
