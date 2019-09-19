package com.example.readinglist.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.readinglist.model.Book

//this tells room that this interface is the DAO, generally you will have one Dao for each entity
@Dao
interface BookDAO {

    //we don't specify what the function does, room generates the code
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createBook(book: Book)

    @Query("SELECT * FROM book_table")
    fun readAllBooks(): LiveData<List<Book>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateBook(book: Book)

    @Delete
    fun deleteBook(book: Book)

}