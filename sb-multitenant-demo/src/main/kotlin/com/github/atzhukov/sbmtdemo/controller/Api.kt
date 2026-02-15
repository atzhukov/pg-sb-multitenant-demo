package com.github.atzhukov.sbmtdemo.controller

import com.github.atzhukov.sbmtdemo.data.entity.Document

interface Api {

    fun getDocuments(): List<Document>

}