package com.example.readinglist

import android.app.Application
import com.example.readinglist.database.BookDBRepo
import timber.log.Timber

val repo: BookRepoInterface by lazy {
    App.repo!!
}

// TODO: 3. Extend Timber to include class, method, line numbers!
class MyDebugTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String? {
        return String.format(
            "[C:%s] [M:%s] [L:%s]",
            super.createStackElementTag(element),
            element.methodName,
            element.lineNumber
        )
    }
}

class App : Application() {

    // TODO: 4. Provide an Application-wide Shared Preferences
    companion object {
        var repo: BookRepoInterface? = null
    }

    override fun onCreate() {
        super.onCreate()

        //repo = BookFileRepo(applicationContext)
        repo = BookDBRepo(applicationContext)

        // TODO: 2. Configure Timber logging
        // "Timber" Library
        if (BuildConfig.DEBUG) {
            Timber.plant(MyDebugTree())
        }
    }
}