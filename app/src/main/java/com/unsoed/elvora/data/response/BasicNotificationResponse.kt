package com.unsoed.elvora.data.response

import com.google.gson.annotations.SerializedName

data class BasicNotificationResponse(
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("success")
    val success: Int? = null,
)
