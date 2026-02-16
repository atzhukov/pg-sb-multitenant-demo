package com.github.atzhukov.sbmtdemo.service.impl

import com.github.atzhukov.sbmtdemo.data.entity.Document
import com.github.atzhukov.sbmtdemo.data.repo.DocumentRepository
import com.github.atzhukov.sbmtdemo.service.DocumentService
import org.springframework.stereotype.Service

@Service
class DocumentServiceImpl(
	private val documentRepository: DocumentRepository
) : DocumentService {
	override fun getAllDocuments(): List<Document>
			= documentRepository.findAll()
}
