package com.github.atzhukov.sbmtdemo.service

import com.github.atzhukov.sbmtdemo.data.entity.User

interface AuthService {
	fun existsByLogin(login: String): Boolean
	fun getByLogin(login: String): User?
	fun createUser(user: User): Long
}
