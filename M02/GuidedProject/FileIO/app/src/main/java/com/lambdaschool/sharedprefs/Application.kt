package com.lambdaschool.sharedprefs

import android.app.Application
import timber.log.Timber

// TODO 4: Change to the repo interface here
val repo: JournalRepoInterface by lazy {
    App.repo!!
}

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

    companion object {
        var repo: JournalRepoInterface? = null
    }

    override fun onCreate() {
        super.onCreate()

        // TODO 5: Instantiate the File repo here instead of Prefs
        //repo = Prefs(applicationContext)
        repo = JournalFileRepo(applicationContext)

        // "Timber" Library
        if (BuildConfig.DEBUG) {
            Timber.plant(MyDebugTree())
        }
    }
}