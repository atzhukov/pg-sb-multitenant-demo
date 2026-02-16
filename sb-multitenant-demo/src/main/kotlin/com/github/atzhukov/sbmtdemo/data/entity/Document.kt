package com.github.atzhukov.sbmtdemo.data.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "documents")
class Document(

	@Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long?,

	@Column(nullable = false)
  var name: String?,

	@Column(nullable = false)
  var contents: String?,

	@ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "tenant")
  var tenant: Tenant?,

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "document")
	var notes: List<Note>?,

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "tags_to_documents",
		joinColumns = [JoinColumn(name = "document")],
		inverseJoinColumns = [JoinColumn(name = "tag")]
	)
	var tags: List<Tag>?

)
