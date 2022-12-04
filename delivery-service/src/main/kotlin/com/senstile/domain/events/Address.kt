package com.senstile.domain.events

data class Address(
    val id: Int,
    val addressLine: String?,
    val city: String?,
    val country: String?,
    val postalCode: String?
)