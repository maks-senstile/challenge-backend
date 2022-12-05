package com.senstile.view

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserView(
    val id: Int,
    @SerialName("first_name")
    val firstName: String?,
    val addresses: List<AddressView>?
)