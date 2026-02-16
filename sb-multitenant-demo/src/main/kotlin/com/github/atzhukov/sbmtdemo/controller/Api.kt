package com.github.atzhukov.sbmtdemo.controller

import com.github.atzhukov.sbmtdemo.data.entity.Document
import com.github.atzhukov.sbmtdemo.data.entity.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/api")
interface Api {

  @GetMapping("/documents")
  fun getDocuments(): List<Document>

  @GetMapping("/users")
  fun findUserByLogin(login: String): User?

}