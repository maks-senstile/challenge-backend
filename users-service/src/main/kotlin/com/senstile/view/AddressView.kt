package com.senstile.view

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddressView(
    val id: Int,
    @SerialName("address_line")
    val addressLine: String?,
    val city: String?,
    val country: String?,
    @SerialName("postal_code")
    val postalCode: String?
)