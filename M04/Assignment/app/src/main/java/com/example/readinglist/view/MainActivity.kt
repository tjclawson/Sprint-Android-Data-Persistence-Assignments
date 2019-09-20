package com.example.readinglist.view

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.readinglist.model.Book
import com.example.readinglist.R
import com.example.readinglist.repo
import com.example.readinglist.viewmodel.BookViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {

    companion object {
        const val ADD_INTENT_KEY = "add"
        const val EDIT_INTENT_KEY = "edit"
        const val REQUEST_CODE = 42
        const val PREF_KEY = "PREFERENCES"
    }

    lateinit var viewModel: BookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(BookViewModel::class.java)

        ReadAllBooksAsyncTask(this).execute()

        button_add.setOnClickListener {
            val intent = Intent(this, EditBookActivity::class.java)
            val addId = ll_booklist.childCount + 1
            Log.i("Debug", addId.toString())
            intent.putExtra(ADD_INTENT_KEY, addId)
            startActivityForResult(intent,
                REQUEST_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            var bookCsv = data?.getStringExtra("book")
            if (bookCsv != null) {
                var returnBook = Book(bookCsv)
                Log.i("Debug", "RESULT_OK: " + returnBook.id.toString())
                //checkBookId(returnBook)
                CreateAsyncTask(viewModel).execute(returnBook)
            }
        } else if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_FIRST_USER) {
            var bookCsv = data?.getStringExtra("book")
            if (bookCsv != null) {
                var returnBook = Book(bookCsv)
                Log.i("Debug", "RESULT_FIRST_USER: " + returnBook.id.toString())
                //checkBookId(returnBook)
                UpdateAsyncTask(viewModel).execute(returnBook)
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
            startActivityForResult(editIntent,
                REQUEST_CODE
            )
        }
        return newView
    }

    fun updateLayout(books: List<Book>) {
        ll_booklist.removeAllViews()
        books.forEach { book ->
            ll_booklist.addView(buildItemView(book))
        }
    }

    class CreateAsyncTask(val viewModel: BookViewModel) : AsyncTask<Book, Void, Unit>() {
        override fun doInBackground(vararg books: Book?) {
            if (books.isNotEmpty()) {
                books[0]?.let {
                    viewModel.createBook(it)
                }
            }
        }
    }

    class UpdateAsyncTask(val viewModel: BookViewModel) : AsyncTask<Book, Void, Unit>() {
        override fun doInBackground(vararg books: Book?) {
            if (books.isNotEmpty()) {
                books[0]?.let {
                    viewModel.updateBook(it)
                }
            }
        }
    }

    class ReadAllBooksAsyncTask(activity: MainActivity) : AsyncTask<Void, Void, LiveData<List<Book>>>() {

        val actRef = WeakReference(activity)

        override fun doInBackground(vararg voids: Void): LiveData<List<Book>>? {
            return actRef.get()?.viewModel?.books
        }

        override fun onPostExecute(result: LiveData<List<Book>>?) {
            result?.let {
                actRef.get()?.let {act ->
                    result.observe(act,
                        Observer<List<Book>> { t ->
                            t?.let {
                                act.updateLayout(t)
                            }
                        })
                }
            }
        }
    }
}

