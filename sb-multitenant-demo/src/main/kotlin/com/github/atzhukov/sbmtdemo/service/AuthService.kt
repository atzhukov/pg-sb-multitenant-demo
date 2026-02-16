package com.github.atzhukov.sbmtdemo.service

import com.github.atzhukov.sbmtdemo.data.entity.User

interface AuthService {
    fun getByLogin(login: String): User?
}