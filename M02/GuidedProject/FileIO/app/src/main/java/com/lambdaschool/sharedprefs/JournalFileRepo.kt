package com.lambdaschool.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import com.lambdaschool.sharedprefs.model.JournalEntry
import java.io.File
import java.io.FileWriter

// TODO 3: Implement the interface here
class JournalFileRepo(var context: Context?): JournalRepoInterface {

    // Basic structure: We will save each object to its own json file

    // TODO 6: createEntry implementation
    override fun createEntry(entry: JournalEntry) {
        val entryString = entry.toJsonObject()
        val filename = context.whichDir
        writeToFile(filename, entryString)
    }

    // TODO 8: writeToFile helper

    // TODO 9: Save storage directory as a member variable

    // TODO 10: Check for external storage is writeable

    // TODO 11: readAllEntries implementation

    // TODO 12: Save fileList as a member variable

    // TODO 13: readFromFile helper

    // TODO 14: updateEntry implementation

    // TODO 15: deleteEntry implementation


}