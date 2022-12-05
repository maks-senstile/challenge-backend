package com.senstile.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("delivery")
class DeliveryProperties {
    lateinit var userServiceUrl: String
}