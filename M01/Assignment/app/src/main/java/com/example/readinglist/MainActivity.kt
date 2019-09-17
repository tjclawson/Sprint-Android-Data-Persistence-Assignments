package com.example.readinglist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val ADD_INTENT_KEY = "add"
        const val EDIT_INTENT_KEY = "edit"
        const val REQUEST_CODE = 42
        const val PREF_KEY = "PREFERENCES"
    }
    lateinit var preferences: SharedPreferences
    var bookList = mutableListOf<Book>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferences = this.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)

        bookList.add(Book("test1", "test1", false, "0"))
        bookList.add(Book("test2", "test2", false, "1"))
        bookList.add(Book("test3", "test3", false, "2"))
        bookList.add(Book("test4", "test4", false, "3"))
        bookList.add(Book("test5", "test5", false, "4"))


        for (i in 0 until bookList.size) {
            ll_booklist.addView(buildItemView(bookList[i]))
        }

        button_add.setOnClickListener {
            val intent = Intent(this, EditBookActivity::class.java)
            val addId = ll_booklist.childCount.toString()
            intent.putExtra(ADD_INTENT_KEY, addId)
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    override fun onResume() {
        super.onResume()
       // bookList = prefs.readAllBooks()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            var bookCsv = data?.getStringExtra("book")
            if (bookCsv != null) {
                var returnBook = Book(bookCsv)
                checkBookId(returnBook)
            }
        }
    }

    private fun buildItemView(book: Book): TextView {
        var newView = TextView(this)
        newView.text = book.title
        var id = book.id
        var newId = id?.toInt()
        if (newId != null){
            newView.id = newId
        }
        newView.textSize = 32f
        newView.setOnClickListener {
            var editIntent = Intent(this, EditBookActivity::class.java)
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
                count++
            }
        }
        if (count == 0) {
            ll_booklist.addView(buildItemView(book))
        }
    }
}
