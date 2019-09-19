package com.lambdaschool.sharedprefs.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lambdaschool.sharedprefs.model.JournalEntry

// TODO 9: Let's define the DAO with methods from our repo interface
@Dao
interface JournalEntryDAO {

    // TODO 10: Insert with onConflict = REPLACE
    // TODO 11: Query for all entities
    // TODO 12: Update with onConflict = REPLACE
    // TODO 13: DELETE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createEntry(entry: JournalEntry)

    @Query("SELECT * FROM journalentry")
    fun readAllEntries(): LiveData<List<JournalEntry>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateEntry(entry: JournalEntry)

    @Delete
    fun deleteEntry(entry: JournalEntry)

   /* @Query("select * from journalentry where needsDelete is 1")
    fun findNeedsDelete(): MutableList<JournalEntry>*/
}