package com.example.readinglist.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.readinglist.model.Book
import com.example.readinglist.R
import com.example.readinglist.view.MainActivity.Companion.ADD_INTENT_KEY
import com.example.readinglist.view.MainActivity.Companion.EDIT_INTENT_KEY
import kotlinx.android.synthetic.main.activity_edit_book.*

class EditBookActivity : AppCompatActivity() {

    lateinit var editBook: Book
    var editOrAdd: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_book)

        val newBookCsv = intent.getStringExtra(EDIT_INTENT_KEY)


        if (newBookCsv != null) {
            editOrAdd = 1
            Log.i("Debug", "newbookcsv: " + newBookCsv)
            editBook = Book(newBookCsv)
            Log.i("Debug", "editbook id: " + editBook.id.toString())
            et_title.setText(editBook.title)
            et_reason.setText(editBook.reasonToRead)
            if (editBook.hasBeenRead == false) {
                button_read.isChecked = false
            } else {
                button_read.isChecked = true
            }
        } else {
            editOrAdd = 0
            editBook = Book(
                "",
                "",
                false,
                intent.getIntExtra(ADD_INTENT_KEY, 5000)
            )
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
            Log.i("Debug", "editbook id when submitted: " + editBook.id.toString())
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
        val returnBook = Book(
            et_title.text.toString(),
            et_reason.text.toString(),
            editBook.hasBeenRead,
            editBook.id
        )
        val returnIntent = Intent()
        returnIntent.putExtra("book", returnBook.toCsvString())
        if (editOrAdd == 0) {
            Log.i("Debug", "editOrAdd = $editOrAdd")
            setResult(RESULT_OK, returnIntent)
        } else {
            Log.i("Debug", "editOrAdd = $editOrAdd")
            setResult(Activity.RESULT_FIRST_USER, returnIntent)
        }
        finish()
    }

    private fun cancelData() {
        val cancelIntent = Intent()
        setResult(RESULT_CANCELED, cancelIntent)
        finish()
    }
}
