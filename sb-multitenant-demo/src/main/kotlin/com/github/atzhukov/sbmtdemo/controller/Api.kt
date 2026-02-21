package com.github.atzhukov.sbmtdemo.controller

import com.github.atzhukov.sbmtdemo.data.entity.Document
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus

@RequestMapping("/api")
interface Api {

	@GetMapping("/documents")
	fun getDocuments(): List<Response.Document>

	@PostMapping("/signup")
	@ResponseStatus(HttpStatus.CREATED)
	fun signUp(@RequestBody request: Request.SignUp)

	@PostMapping("/signin")
	fun signIn(@RequestBody credentials: Request.Credentials): String

	abstract class Request {
		data class Credentials(val login: String, val password: String)
		data class SignUp(val credentials: Credentials, val name: String, val tenants: List<Long>)
	}

	@Suppress("RemoveRedundantQualifierName")
	abstract class Response {
		data class Document(
			val id: Long,
			val name: String,
			val contents: String,
			val notes: List<Response.Note>,
			val tags: List<Response.Tag>,
			val tenant: Response.Tenant?
		)
		data class Note(val id: Long, val contents: String)
		data class Tag(val id: Long, val name: String)
		data class Tenant(val id: Long, val name: String)
	}

}
