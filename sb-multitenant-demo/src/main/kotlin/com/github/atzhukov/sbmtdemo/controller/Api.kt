package com.github.atzhukov.sbmtdemo.controller

import com.github.atzhukov.sbmtdemo.data.entity.Document
import com.github.atzhukov.sbmtdemo.data.entity.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@RequestMapping("/api")
interface Api {

	@GetMapping("/documents")
	fun getDocuments(): List<Document>

	@GetMapping("/users")
	fun findUserByLogin(login: String): User?

	@PostMapping("/login")
	fun login(@RequestParam("login") login: String, @RequestParam("password") password: String)

}
