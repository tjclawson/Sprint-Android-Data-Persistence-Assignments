package com.lambdaschool.sharedprefs.ui

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.lambdaschool.sharedprefs.R
import com.lambdaschool.sharedprefs.entity.Journal.Companion.createJournalEntry
import com.lambdaschool.sharedprefs.model.JournalEntry
import com.lambdaschool.sharedprefs.repo
import com.lambdaschool.sharedprefs.viewmodel.EntriesViewModel
import kotlinx.android.synthetic.main.activity_journal_list.*
import kotlinx.android.synthetic.main.content_journal_list.*
import timber.log.Timber.i
import java.lang.ref.WeakReference

class JournalListActivity : AppCompatActivity() {

    companion object {
        const val NEW_ENTRY_REQUEST = 2
        const val EDIT_ENTRY_REQUEST = 1
    }

    // TODO 27a: We don't need entryList anymore, but we do need a viewModel
    //var entryList: MutableList<JournalEntry> = ArrayList()
    lateinit var viewModel: EntriesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal_list)
        setSupportActionBar(toolbar)

        // TODO 27: Get a ViewModel
        viewModel = ViewModelProviders.of(this).get(EntriesViewModel::class.java)

        fab.setOnClickListener { view ->
            val intent = Intent(this@JournalListActivity, DetailsActivity::class.java)
            val entry = createJournalEntry()
            intent.putExtra(JournalEntry.TAG, entry)
            startActivityForResult(
                intent,
                NEW_ENTRY_REQUEST
            )
        }

        i("onCreate")

        // TODO 17: Replace the call here by an AsyncTask
       // entryList = repo.readAllEntries()

        ReadAllEntriesAsyncTask(this).execute()

        // TODO 22: Extract update functionality
       // updateLayout(entryList)

        // TODO 28: Replace the call above by observing LiveData from the ViewModel
    }

    override fun onStart() {
        super.onStart()
        i("onStart")
    }

    override fun onResume() {
        super.onResume()

        i("onResume")

        // TODO 29: Do we need this code anymore?
        //updateLayout(entryList)
    }

    // TODO 22: Extract update functionality
    private fun updateLayout(entries: List<JournalEntry>) {
        listLayout.removeAllViews()
        entries.forEach { entry ->
            listLayout.addView(createEntryView(entry))
        }
    }

    override fun onPause() {
        super.onPause()

        i("onPause")
    }

    override fun onStop() {
        super.onStop()

        i("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()

        i("onDestroy")
    }

    private fun createEntryView(entry: JournalEntry): TextView {
        val view = TextView(this@JournalListActivity)

        view.text = getString(R.string.entry_label, entry.id, entry.date, entry.dayRating)

        view.setPadding(15, 15, 15, 15)
        view.textSize = 22f

        view.setOnClickListener {
            val viewDetailIntent = Intent(this@JournalListActivity, DetailsActivity::class.java)
            viewDetailIntent.putExtra(JournalEntry.TAG, entry)
            startActivityForResult(
                viewDetailIntent,
                EDIT_ENTRY_REQUEST
            )
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == NEW_ENTRY_REQUEST) {
                if (data != null) {
                    val entry = data.getSerializableExtra(JournalEntry.TAG) as JournalEntry
                    //entryList.add(entry)
                    //repo.createEntry(entry) // TODO 16a: Notice the call here, replace with AsyncTask
                    CreateAsyncTask(viewModel).execute(entry)
                }
            } else if (requestCode == EDIT_ENTRY_REQUEST) {
                if (data != null) {
                    val entry = data.getSerializableExtra(JournalEntry.TAG) as JournalEntry
                    //entryList[entry.id] = entry
                    //repo.updateEntry(entry) // TODO 16b. Notice the call here, replace with AsyncTask
                    UpdateAsyncTask(viewModel).execute(entry)
                }
            }
        }
    }

    // TODO 19: Create AsyncTask
    class CreateAsyncTask(val viewModel: EntriesViewModel) : AsyncTask<JournalEntry, Void, Unit>() {
        override fun doInBackground(vararg entries: JournalEntry?) {
            if (entries.isNotEmpty()) {
                entries[0]?.let {
                    viewModel.createEntry(it)
                }
            }
        }
    }
    // TODO 20: Update AsyncTask
    class UpdateAsyncTask(val viewModel: EntriesViewModel) : AsyncTask<JournalEntry, Void, Unit>() {
        override fun doInBackground(vararg entries: JournalEntry?) {
            if (entries.isNotEmpty()) {
                entries[0]?.let {
                    viewModel.updateEntry(it)
                }
            }
        }
    }
    // TODO 21: ReadAll AsyncTask
    class ReadAllEntriesAsyncTask(activity: JournalListActivity) : AsyncTask<Void, Void, LiveData<List<JournalEntry>>>() {

        val actRef = WeakReference(activity)

        override fun doInBackground(vararg voids: Void): LiveData<List<JournalEntry>>? {
            return actRef.get()?.viewModel?.entries
        }

        override fun onPostExecute(result: LiveData<List<JournalEntry>>?) {
            result?.let {
                actRef.get()?.let {act ->
                    result.observe(act,
                        Observer<List<JournalEntry>> { t ->
                            t?.let {
                                act.updateLayout(t)
                            }
                        })
                }
            }
        }
    }
}
