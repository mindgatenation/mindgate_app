package com.mindgate.mindgateapp.data.mongo

import com.mindgate.mindgateapp.data.dao.Professional
import com.mindgate.mindgateapp.data.dao.User
import com.mindgate.mindgateapp.data.repo.UserRepository

class MongoUserRepository() : UserRepository {

    override suspend fun getUserDetails(userId : String): User? {
        return null
    }

    override suspend fun getProfessionalDetails(userId : String) : Professional? {
        return sampleProfessional
    }

}// Dummy Data
val sampleProfessional = Professional(
    id = "1",
    name = "Sarah Black",
    type = "Physiologists",
    description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.",
    imgUrl = "https://annemariesegal.com/wp-content/uploads/2017/04/adobestock_86346713-cropped-young-woman-in-suit.jpg?w=1680",
    rating = 4.0,
    price = 499,
    tags = listOf("Anxiety", "Stress", "Relationship")
)