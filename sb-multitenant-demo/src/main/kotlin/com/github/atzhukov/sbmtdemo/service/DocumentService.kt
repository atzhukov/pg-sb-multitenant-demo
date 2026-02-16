package com.github.atzhukov.sbmtdemo.service

import com.github.atzhukov.sbmtdemo.data.entity.Document

interface DocumentService {

  fun getAllDocuments(): List<Document>

}