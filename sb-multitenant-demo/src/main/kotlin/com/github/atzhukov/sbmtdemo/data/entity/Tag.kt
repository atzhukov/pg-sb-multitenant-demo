package com.github.atzhukov.sbmtdemo.data.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "tags")
class Tag(

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long?,

	@Column(nullable = false)
	var name: String?,

	@ManyToOne(fetch = FetchType.EAGER)
	var tenant: Tenant?

)
