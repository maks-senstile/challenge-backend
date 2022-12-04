package com.senstile.user

data class AddressModel(
    val id: Int,
    val addressLine: String?,
    val city: String?,
    val country: String?,
    val postalCode: String?
)