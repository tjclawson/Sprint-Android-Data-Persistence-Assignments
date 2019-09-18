package com.example.readinglist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val ADD_INTENT_KEY = "add"
        const val EDIT_INTENT_KEY = "edit"
        const val REQUEST_CODE = 42
        const val PREF_KEY = "PREFERENCES"
    }

    private var bookList = mutableListOf<Book>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bookList = repo.readAllBooks()

        for (i in 0 until bookList.size) {
            ll_booklist.addView(buildItemView(bookList[i]))
        }

        button_add.setOnClickListener {
            val intent = Intent(this, EditBookActivity::class.java)
            val addId = ll_booklist.childCount
            Log.i("Debug", addId.toString())
            intent.putExtra(ADD_INTENT_KEY, addId)
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            var bookCsv = data?.getStringExtra("book")
            if (bookCsv != null) {
                var returnBook = Book(bookCsv)
                Log.i("Debug", "on result id: " + returnBook.id.toString())
                checkBookId(returnBook)
            }
        }
    }

    private fun buildItemView(book: Book): TextView {
        var newView = TextView(this)
        newView.text = book.title
        newView.id = book.id
        newView.textSize = 32f
        newView.setOnClickListener {
            val editIntent = Intent(this, EditBookActivity::class.java)
            editIntent.putExtra(EDIT_INTENT_KEY, book.toCsvString())
            startActivityForResult(editIntent, REQUEST_CODE)
        }
        return newView
    }

    private fun checkBookId(book: Book) {
        var count = 0
        for (i in 0 until bookList.size) {
            if (book.id == bookList[i].id) {
                bookList[i] = book
                ll_booklist.removeViewAt(i)
                ll_booklist.addView(buildItemView(book), i)
                repo.updateBook(book)
                count++
            }
        }
        if (count == 0) {
            ll_booklist.addView(buildItemView(book))
            repo.createBook(book)
            bookList.add(book)
        }
    }
}
