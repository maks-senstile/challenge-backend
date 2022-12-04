package com.senstile.domain.events

data class User(
    val id: Int,
    val firstName: String?,
    val address: Address
)