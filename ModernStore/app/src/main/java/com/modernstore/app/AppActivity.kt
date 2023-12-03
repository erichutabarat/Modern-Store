package com.modernstore.app

import android.app.Application
import com.modernstore.app.db.roomdb.AppDatabase

class AppActivity : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.getInstance(this)
    }
}
