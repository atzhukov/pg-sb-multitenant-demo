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
import jakarta.persistence.Table
import java.time.OffsetDateTime

@Entity
@Table(name = "users")
class User(

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long?,

  @Column(nullable = false)
  var login: String?,

  @Column(nullable = false)
  var password: String?,

  @Column(nullable = false)
  var name: String?,

  @Column(nullable = false)
  var lastLogin: OffsetDateTime?,

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
		name = "users_to_tenants",
		joinColumns = [JoinColumn(name = "user")],
		inverseJoinColumns = [JoinColumn(name = "tenant")]
  )
  var tenants: Set<Tenant>?

)
