package com.modernstore.app.db.roomdb

class UserRepository(private val userDao: UserDao) {
    suspend fun addUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }

    suspend fun getUserById(userId: Long): User? {
        return userDao.getUserById(userId)
    }

    suspend fun getUserByUsernameAndPassword(username: String, password: String): User? {
        return userDao.getUserByUsernameAndPassword(username, password)
    }

    suspend fun getUserByUsername(username: String): User? {
        return userDao.getUserByUsername(username)
    }
}
