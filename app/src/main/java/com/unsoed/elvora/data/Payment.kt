package com.unsoed.elvora.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Payment(
    val name: String,
    val image: Int,
    val desc: String,
    val number: String
): Parcelable
