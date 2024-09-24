package com.unsoed.elvora.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val email: String,
    val fullName: String,
    val token: String,
    val premium: Boolean
): Parcelable

@Parcelize
data class UserShippingModel(
    val name: String,
    val telephoneNumber: String,
    val street: String,
    val village: String,
    val address: String,
): Parcelable

@Parcelize
data class UserVerify(
    val name: String,
    val nik: String,
    val date: String
): Parcelable