package com.github.atzhukov.sbmtdemo.controller

import com.github.atzhukov.sbmtdemo.data.entity.Document
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/api")
interface Api {

	@GetMapping("/documents")
	fun getDocuments(): List<Document>

	@PostMapping("/signup")
	fun signUp(@RequestBody credentials: Credentials): String

	data class Credentials(val login: String, val password: String)

}
