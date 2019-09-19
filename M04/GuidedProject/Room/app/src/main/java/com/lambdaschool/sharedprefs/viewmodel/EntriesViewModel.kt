package com.lambdaschool.sharedprefs.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.lambdaschool.sharedprefs.model.JournalEntry
import com.lambdaschool.sharedprefs.repo

// TODO 24: Create a ViewModel for entries
class EntriesViewModel : ViewModel() {

    val entries: LiveData<List<JournalEntry>> by lazy {
        readAllEntries()
    }

    fun createEntry(entry: JournalEntry) {
        repo.createEntry(entry)

    }

    fun readAllEntries(): LiveData<List<JournalEntry>> {
        return repo.readAllEntries()
    }

    fun updateEntry(entry: JournalEntry) {
        repo.updateEntry(entry)
    }

    fun deleteEntry(entry: JournalEntry) {
        repo.deleteEntry(entry)
    }
    // TODO 25: Create a LiveData object for the entries

    // TODO 26: Recreate the repo calls to as functions here.
}

