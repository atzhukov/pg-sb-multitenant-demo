package com.github.atzhukov.sbmtdemo.controller

import com.github.atzhukov.sbmtdemo.data.entity.Document
import com.github.atzhukov.sbmtdemo.data.entity.Note
import com.github.atzhukov.sbmtdemo.data.entity.Tag
import com.github.atzhukov.sbmtdemo.data.entity.Tenant

fun Document.toDto() = Api.Response.Document(
	id = this.id!!,
	name = this.name!!,
	contents = this.contents!!,
	notes = this.notes!!.map { it.toDto() },
	tags = this.tags!!.map { it.toDto() },
	tenant = this.tenant?.toDto()
)

fun Note.toDto() = Api.Response.Note(
	id = this.id!!,
	contents = this.contents!!
)

fun Tag.toDto() = Api.Response.Tag(
	id = this.id!!,
	name = this.name!!
)

fun Tenant.toDto() = Api.Response.Tenant(
	id = this.id!!,
	name = this.name!!
)
