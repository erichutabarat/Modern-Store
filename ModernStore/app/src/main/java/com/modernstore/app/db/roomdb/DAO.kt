package com.modernstore.app.db.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: Long): User?

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    fun getUserByUsernameAndPassword(username: String, password: String): User?

    @Query("SELECT * FROM users WHERE username = :username")
    fun getUserByUsername(username: String): User?


    @Query("SELECT balance from users WHERE username = :username")
    fun getBalanceByUsername(username: String): Double
    @Query("UPDATE users SET balance = :newBalance where username =:username")
    fun updateBalance(newBalance: Double, username: String)
    @Query("SELECT id from users WHERE username =:username")
    fun getIdByUsername(username: String): Int

    @Query("SELECT COUNT(*) FROM users WHERE username = :username AND password = :password")
    suspend fun isValidUser(username: String, password: String): Boolean
}
@Dao
interface CartDao {
    @Insert
    fun insertCart(cart: Cart)

    @Query("SELECT * FROM carts WHERE userId = :userId")
    fun getCartsByUser(userId: Long): List<Cart>

    @Delete
    fun deleteCart(cart: Cart)
}