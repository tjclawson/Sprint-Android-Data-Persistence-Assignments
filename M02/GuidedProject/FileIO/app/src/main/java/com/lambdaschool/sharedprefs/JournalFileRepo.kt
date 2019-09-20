package com.lambdaschool.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Environment
import com.lambdaschool.sharedprefs.model.JournalEntry
import org.json.JSONException
import org.json.JSONObject
import java.io.*

// TODO 3: Implement the interface here
class JournalFileRepo(var context: Context): JournalRepoInterface {

    override fun updateEntry(entry: JournalEntry) {
        createEntry(entry)
    }

    override fun deleteEntry(entry: JournalEntry) {

    }

    // Basic structure: We will save each object to its own json file

    // TODO 6: createEntry implementation
    override fun createEntry(entry: JournalEntry) {
        val entryString = entry.toJsonObject()
        val filename = "journalEntry${entry.date}.json"
        writeToFile(filename, entryString.toString())
    }

    // TODO 8: writeToFile helper
    private fun writeToFile(filename: String, entryString: String) {
        //figure out directory
        val dir = storageDirectory
        val outputFile = File(dir, filename)

        //open FileWriter
        var writer: FileWriter? = null
        try {
            //write
            writer = FileWriter(outputFile)
            writer.write(entryString)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (writer != null) {
                try {
                    //close
                    writer.close()
                } catch (e2: IOException) {
                    e2.printStackTrace()
                }
            }
        }
    }

    // TODO 9: Save storage directory as a member variable

    val storageDirectory: File
        get() {
            /*context.filesDir
            context.getExternalFilesDir()
            context.cacheDir*/
            if (isExternalStorageWriteable) {
                val directory = context.filesDir
                    //Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                return if (!directory.exists() && !directory.mkdirs()) {
                    context.cacheDir
                } else {
                    directory
                }
            }else {
                return context.cacheDir
            }
        }

    // TODO 10: Check for external storage is writeable

    val isExternalStorageWriteable: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return state == Environment.MEDIA_MOUNTED
        }

    override fun readAllEntries(): MutableList<JournalEntry> {
        //get filelist
        val entries = ArrayList<JournalEntry>()

        //setup arraylist
        //read in files and convert to objects

        for (filename in fileList) {
            val json = readFromFile(filename)
            try {
                entries.add(JournalEntry(JSONObject(json)))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return entries
    }

    // TODO 12: Save fileList as a member variable

    val fileList: ArrayList<String>
        get() {
            val fileNames = arrayListOf<String>()
            val dir = storageDirectory

            val list = dir.list()
            if (list != null) {
                for (name in list) {
                    if (name.contains(".json")) {
                        fileNames.add(name)
                    }
                }
            }
            return fileNames
        }

    // TODO 13: readFromFile helper
    private fun readFromFile(filename: String): String {
        val inputfile = File(storageDirectory, filename)
        //val readData = StringBuilder()
        var readString: String? = null
        var reader: FileReader? = null
        try {
            reader = FileReader(inputfile)
            readString = reader.readText()

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return readString ?: "oops lol"
    }

    // TODO 14: updateEntry implementation

    // TODO 15: deleteEntry implementation


}