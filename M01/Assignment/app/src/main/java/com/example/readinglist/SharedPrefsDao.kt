package com.example.readinglist

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsDao(context: Context) {

    companion object{
        const val LIST_ID = "LISTID"
        const val NEXT_ID = "NEXTID"
        const val PREF_KEY = "PREFERENCES"
        const val INVALID_ID = "-1"
    }

    val sharedPrefs: SharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)

    fun createBook(book: Book) {
        val ids = getAllBookIds()

        if (book.id == INVALID_ID && !ids.contains(book.id.toString())) {
            // new book
            val editor = sharedPrefs.edit()

            var nextId = sharedPrefs.getString(NEXT_ID, "0")
            book.id = nextId
            // store updated next id
            var newNextId = nextId!!.toInt()
            editor.putInt(NEXT_ID, ++newNextId)

            // add id to list of ids

            if (book.id != null) {
                ids.add(book.id.toString())
            }
            // store updated id list
            val newIdList = StringBuilder()
            for (id in ids) {
                newIdList.append(id).append(",")
            }

            editor.putString(LIST_ID, newIdList.toString())

            // store new book
            editor.putString(book.id, book.toCsvString())
            editor.apply()
        } else {
            updateBook(book)
        }
    }

    fun getAllBookIds(): ArrayList<String>{
        val idList = sharedPrefs.getString(LIST_ID, "")
        val oldList = idList!!.split(",")

        val ids = ArrayList<String>(oldList.size)
        if (idList.isNotBlank()) {
            ids.addAll(oldList)
        }
        return ids
    }

    fun getNextId(): String {
        var nextId = sharedPrefs.getString(NEXT_ID, "0")
        return nextId.toString()
    }

    fun readBook(id: String): Book? {
        val bookCsv = sharedPrefs.getString(id, "invalid")!!
        return if (bookCsv != "invalid") {
            Book(bookCsv)
        } else {
            null
        }
    }

    fun updateBook(book: Book) {
        val editor = sharedPrefs.edit()
        editor.putString(book.id, book.toCsvString())
        editor.apply()
    }

    fun readAllBooks(): MutableList<Book> {
        val listOfIds = getAllBookIds()

        val bookList = java.util.ArrayList<Book>()
        for (id in listOfIds) {
            if (id.isNotBlank()) {
                readBook(id)?.let {
                    bookList.add(it)
                }
            }
        }
        return bookList
    }
}