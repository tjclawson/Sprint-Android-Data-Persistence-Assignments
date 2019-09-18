package com.example.readinglist

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context): BookRepoInterface {

    companion object {
        private const val BOOK_PREFERENCES = "BookPreferences"

        private const val ID_LIST_KEY = "id_list"
        private const val NEXT_ID_KEY = "next_id"
        private const val ENTRY_ID_KEY_PREFIX = "entry_"
    }

    val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(BOOK_PREFERENCES, Context.MODE_PRIVATE)

    // create a new entry
    override fun createBook(book: Book) {
        // read list of book ids
        val ids = getListOfIds()

        if (book.id != Book.INVALID_ID && !ids.contains(book.id.toString())) {
            // new book
            val editor = sharedPrefs.edit()

            var nextId = sharedPrefs.getInt(NEXT_ID_KEY, 0)
            book.id = nextId
            // store updated next id
            editor.putInt(NEXT_ID_KEY, ++nextId)

            // add id to list of ids

            ids.add(Integer.toString(book.id))
            // store updated id list
            val newIdList = StringBuilder()
            for (id in ids) {
                newIdList.append(id).append(",")
            }

            editor.putString(ID_LIST_KEY, newIdList.toString())

            // store new book
            editor.putString(ENTRY_ID_KEY_PREFIX + book.id, book.toCsvString())
            editor.apply()
        } else {
            updateBook(book)
        }
    }

    private fun getListOfIds(): java.util.ArrayList<String> {
        val idList = sharedPrefs.getString(ID_LIST_KEY, "")
        val oldList = idList!!.split(",")

        val ids = ArrayList<String>(oldList.size)
        if (idList.isNotBlank()) {
            ids.addAll(oldList)
        }
        return ids
    }

    // read an existing book
    private fun readBook(id: Int): Book? {
        val bookCsv = sharedPrefs.getString(ENTRY_ID_KEY_PREFIX + id, "invalid")!!
        return if (bookCsv != "invalid") {
            Book(bookCsv)
        } else {
            null
        }
    }

    // read all entries
    override fun readAllBooks(): MutableList<Book> {
        // read list of book ids
        val listOfIds = getListOfIds()

        // step through that list and reach each book
        val bookList = java.util.ArrayList<Book>()
        for (id in listOfIds) {
            if (id.isNotBlank()) {
                readBook(id.toInt())?.let {
                    bookList.add(it)
                }
            }
        }
        return bookList
    }

    // edit an existing book
    override fun updateBook(book: Book) {
        val editor = sharedPrefs.edit()
        editor.putString(ENTRY_ID_KEY_PREFIX + book.id, book.toCsvString())
        editor.apply()
    }

    override fun deleteBook(book: Book) {
        val editor = sharedPrefs.edit()
        editor.remove(ENTRY_ID_KEY_PREFIX + book.id)
        editor.apply()
    }
}