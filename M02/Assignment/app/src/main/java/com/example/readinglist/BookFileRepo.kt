package com.example.readinglist

import android.content.Context
import android.os.Environment
import org.json.JSONException
import org.json.JSONObject
import java.io.*

class BookFileRepo(var context: Context): BookRepoInterface {

    override fun createBook(book: Book) {
        val bookString = book.toJsonObject()
        val filename = "book${book.id}.json"
        writeToFile(filename, bookString.toString())
    }

    override fun readAllBooks(): MutableList<Book> {
        val books = ArrayList<Book>()

        for (filename in fileList) {
            val json = readFromFile(filename)
            try {
                books.add(Book(JSONObject(json)))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return books
    }


    override fun updateBook(book: Book) {
        createBook(book)
    }

    override fun deleteBook(book: Book) {

    }

    private fun writeToFile(filename: String, bookString: String) {
        val dir = storageDirectory
        val outputFile = File(dir, filename)
        var writer: FileWriter? = null
        try {
            writer = FileWriter(outputFile)
            writer.write(bookString)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (writer != null) {
                try {
                    writer.close()
                } catch (e2: IOException) {
                    e2.printStackTrace()
                }
            }
        }
    }

    val storageDirectory: File
        get() {
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

    val isExternalStorageWriteable: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return state == Environment.MEDIA_MOUNTED
        }

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

    private fun readFromFile(filename: String): String {
        val inputfile = File(storageDirectory, filename)
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
}