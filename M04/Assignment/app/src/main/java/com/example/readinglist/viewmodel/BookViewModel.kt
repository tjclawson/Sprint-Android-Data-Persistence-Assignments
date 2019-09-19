package com.example.readinglist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.readinglist.model.Book
import com.example.readinglist.repo

class BookViewModel : ViewModel() {

    val books: LiveData<List<Book>> by lazy {
        readAllBooks()
    }

    fun createBook(book: Book) {
        repo.createBook(book)

    }

    fun readAllBooks(): LiveData<List<Book>> {
        return repo.readAllBooks()
    }

    fun updateBook(book: Book) {
        repo.updateBook(book)
    }

    fun deleteBook(book: Book) {
        repo.deleteBook(book)
    }
}