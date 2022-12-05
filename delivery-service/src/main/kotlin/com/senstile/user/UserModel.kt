package com.senstile.user

import com.senstile.domain.exceptions.ObjectNotFoundException

data class UserModel(
    val id: Int,
    val firstName: String?,
    val addresses: List<AddressModel>?
) {
    fun requireAddress(addressId: Int): AddressModel {
        return this.addresses?.firstOrNull { it.id == addressId }
            ?: throw ObjectNotFoundException("User '$id' address '$addressId' not found.")
    }
}