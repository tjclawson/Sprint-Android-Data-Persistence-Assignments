package com.example.readinglist

import androidx.lifecycle.LiveData
import com.example.readinglist.model.Book

interface BookRepoInterface {
    fun createBook(book: Book)
    fun readAllBooks(): LiveData<List<Book>>
    fun updateBook(book: Book)
    fun deleteBook(book: Book)
}