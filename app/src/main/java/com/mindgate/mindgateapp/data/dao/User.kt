package com.mindgate.mindgateapp.data.dao

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val name : String? = null,
    val email : String? = null,
    val profile_pic : String? = null
)