package com.senstile.entity

import kotlinx.serialization.Serializable

@Serializable
data class AddressEntity(
    val id: Int,
    val addressLine: String?,
    val city: String?,
    val country: String?,
    val postalCode: String?
)