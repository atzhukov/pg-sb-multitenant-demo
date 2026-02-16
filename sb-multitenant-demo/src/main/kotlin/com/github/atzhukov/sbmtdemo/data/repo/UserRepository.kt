package com.github.atzhukov.sbmtdemo.data.repo

import com.github.atzhukov.sbmtdemo.data.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long>