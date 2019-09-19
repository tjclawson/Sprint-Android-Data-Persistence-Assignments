package com.lambdaschool.sharedprefs.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.lambdaschool.sharedprefs.JournalRepoInterface
import com.lambdaschool.sharedprefs.model.JournalEntry

// TODO 5: Create the Database repo and implement the methods
class JournalDBRepo(context: Context) : JournalRepoInterface {

    val contxt = context.applicationContext

    override fun createEntry(entry: JournalEntry) {
        database.entriesDao().createEntry(entry)

        //example conditional of what we could do with a API
        /*if (hasInternet) {
            retrofit.createEntry(entry).enqueuq(object: Callback<>) {
                onResponse(response: Response<JournalEntry>)
                    database.entriesDao().createEntry(response.body)
            }
        } else {
            database.entriesDao().createEntry(entry)
        }*/
    }

    override fun readAllEntries(): LiveData<List<JournalEntry>> {
        return database.entriesDao().readAllEntries()
    }

    override fun updateEntry(entry: JournalEntry) {
        database.entriesDao().updateEntry(entry)
    }

    override fun deleteEntry(entry: JournalEntry) {
        database.entriesDao().deleteEntry(entry)
    }

    // TODO 15: Build the Room database
    //by lazy mean there is nothing in variable until it's called, different from lateinit because it can only be assigned once, checks if something is there
    //if there is something there, it returns it
    private val database by lazy {
        Room.databaseBuilder(contxt, JournalEntryDB::class.java, "entry_database")
                //this fallback will just destroy everything if there isn't a migration pattern for it
            .fallbackToDestructiveMigration()
            .build()
    }

    //private  val retrofit: Retrofit

}

