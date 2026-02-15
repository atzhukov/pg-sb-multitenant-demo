package com.github.atzhukov.sbmtdemo.data.repo

import com.github.atzhukov.sbmtdemo.data.entity.Document
import org.springframework.data.jpa.repository.JpaRepository

interface DocumentRepository: JpaRepository<Document, Long>