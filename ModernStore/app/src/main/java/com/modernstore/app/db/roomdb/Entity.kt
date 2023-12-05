package com.modernstore.app.db.roomdb

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val username: String,
    val password: String,
    val email: String,
    val balance: Double
)
@Entity(
    tableName = "carts",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Cart(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val imageSrc: String,
    val userId: Long,
    val productId: Long,
    val productPrice: Double
)