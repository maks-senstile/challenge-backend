package com.senstile.entity

data class UserEntity(
    val id: Int,
    var firstName: String? = null,
    var addresses: List<AddressEntity>? = null
)