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
	fun getDocuments(): List<Document>

	@PostMapping("/signup")
	@ResponseStatus(HttpStatus.CREATED)
	fun signUp(@RequestBody request: SignUpRequest)

	@PostMapping("/signin")
	fun signIn(@RequestBody credentials: Credentials): String

	data class Credentials(val login: String, val password: String)
	data class SignUpRequest(val credentials: Credentials, val name: String, val tenants: List<Long>)

}
