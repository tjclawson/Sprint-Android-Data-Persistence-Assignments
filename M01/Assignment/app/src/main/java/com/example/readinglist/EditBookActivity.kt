package com.example.readinglist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.readinglist.MainActivity.Companion.ADD_INTENT_KEY
import com.example.readinglist.MainActivity.Companion.EDIT_INTENT_KEY
import kotlinx.android.synthetic.main.activity_edit_book.*

class EditBookActivity : AppCompatActivity() {

    lateinit var editBook: Book

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_book)

        val newBookCsv = intent.getStringExtra(EDIT_INTENT_KEY)

        if (newBookCsv != null) {
            editBook = Book(newBookCsv)
            et_title.setText(editBook.title)
            et_reason.setText(editBook.reasonToRead)
            if (editBook.hasBeenRead == false) {
                button_read.isChecked = false
            } else {
                button_read.isChecked = true
            }
        } else {
            editBook = Book("","", false, intent.getStringExtra(ADD_INTENT_KEY) )
        }

        button_read.setOnClickListener{
            if (button_read.isChecked == false) {
                editBook.hasBeenRead = false
                Log.i("Debug", editBook.hasBeenRead.toString())
            } else {
                editBook.hasBeenRead = true
                Log.i("Debug", editBook.hasBeenRead.toString())
            }
        }

        button_submit.setOnClickListener {
            returnData()
        }

        button_cancel.setOnClickListener {
            cancelData()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        returnData()
    }

    private fun returnData() {
        val returnBook = Book(et_title.text.toString(), et_reason.text.toString(), editBook.hasBeenRead, editBook.id)
        val returnIntent = Intent()
        returnIntent.putExtra("book", returnBook.toCsvString())
        setResult(RESULT_OK, returnIntent)
        finish()
    }

    private fun cancelData() {
        val cancelIntent = Intent()
        setResult(RESULT_CANCELED, cancelIntent)
        finish()
    }
}
