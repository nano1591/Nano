package com.example.nano.data

import androidx.annotation.DrawableRes

data class Account(
    val id: Long,
    val uid: Long,
    val surname: String,
    val name: String,
    val email: String,
    val altEmail: String,
    @DrawableRes val avatar: Int,
    var isCurrentAccount: Boolean = false
) {
    val fullName: String = "$surname$name"
}