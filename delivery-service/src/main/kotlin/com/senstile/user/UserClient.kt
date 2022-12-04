package com.senstile.user

import com.senstile.configuration.DeliveryProperties
import com.senstile.domain.exceptions.ObjectNotFoundException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component

@Component
@EnableConfigurationProperties(DeliveryProperties::class)
class UserClient(
    private val httpClient: HttpClient,
    private val properties: DeliveryProperties
) {

    suspend fun requireUser(userId: Int): UserModel {
        val url = properties.userServiceUrl + "/find-user-by-id/$userId"

        val response = httpClient.get(url) {
        }

        if (response.status == HttpStatusCode.OK)
            return response.body()
        if (response.status == HttpStatusCode.NotFound)
            throw ObjectNotFoundException("User '$userId' not found.")
        else
            throw IllegalArgumentException("Request '$url' returns '${response.status}'.")
    }
}