package com.lambdaschool.sharedprefs

import com.lambdaschool.sharedprefs.model.JournalEntry

// TODO 1: Extract behavior from Prefs to an interface

interface JournalRepoInterface {
    fun createEntry(entry: JournalEntry)
    fun readAllEntries(): MutableList<JournalEntry>
    fun updateEntry(entry: JournalEntry)
    fun deleteEntry(entry: JournalEntry)
}