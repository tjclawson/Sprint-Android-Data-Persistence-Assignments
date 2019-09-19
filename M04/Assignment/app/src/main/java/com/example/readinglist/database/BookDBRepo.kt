package com.example.readinglist.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.readinglist.BookRepoInterface
import com.example.readinglist.model.Book

class BookDBRepo (context: Context) : BookRepoInterface {

    val contxt = context.applicationContext

    override fun createBook(book: Book) {
        database.bookDao().createBook(book)
    }

    override fun readAllBooks(): LiveData<List<Book>> {
        return database.bookDao().readAllBooks()
    }

    override fun updateBook(book: Book) {
        database.bookDao().updateBook(book)
    }

    override fun deleteBook(book: Book) {
        database.bookDao().deleteBook(book)
    }

    private val database by lazy {
        Room.databaseBuilder(contxt, BookDB::class.java, "book_database")
            .fallbackToDestructiveMigration()
            .build()
    }

}