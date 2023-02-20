package com.example.nano.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Account(
    @PrimaryKey val uid: Long,
    val name: String,
    val email: String,
    val avatar: String
)