package com.senstile

import com.senstile.entity.AddressEntity
import com.senstile.entity.UserEntity
import com.senstile.exceptions.http.ObjectNotFoundException
import com.senstile.view.AddressView
import com.senstile.view.UserView
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory

class UserService: KoinComponent {

    private val userRepository by inject<UserRepository>()

    private val logger = LoggerFactory.getLogger(UserService::class.java)

    suspend fun findUser(id: Int): UserView {
        logger.debug("Querying user '$id'.")

        return userRepository.findUserById(id)?.toView()
            ?: throw ObjectNotFoundException("User '$id' not found.")
    }

    suspend fun findAll(limit: Int, offset: Int): List<UserView> {
        logger.debug("Querying '$limit' users.")

        return userRepository.findAll(limit, offset).map { it.toView() }
    }

    private fun UserEntity.toView(): UserView {
        return UserView(
            id = this.id,
            firstName = this.firstName,
            addresses = this.addresses?.map { a -> a.toView() }
        )
    }

    private fun AddressEntity.toView(): AddressView {
        return AddressView(
            id = this.id,
            addressLine = this.addressLine,
            city = this.city,
            postalCode = this.postalCode,
            country = this.country
        )
    }
}