package com.example.nano.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Account(
    @PrimaryKey val uid: Int,
    val name: String,
    val code: String,
    val avatar: String
)