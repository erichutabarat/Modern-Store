package com.modernstore.app.db.preferencemanager

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("Modernstore-sharedpreferences", Context.MODE_PRIVATE)

    fun LoggedIn(isLogged: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLogged", isLogged)
        editor.apply()
    }

    fun getLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLogged", false)
    }

    fun clearPreferences() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}
