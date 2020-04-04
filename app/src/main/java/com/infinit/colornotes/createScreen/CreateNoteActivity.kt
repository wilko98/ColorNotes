package com.infinit.colornotes.createScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.infinit.colornotes.ui.main.MainActivity
import com.infinit.colornotes.R
import com.infinit.colornotes.model.Note
import com.infinit.colornotes.ui.main.MainViewModel
import kotlinx.android.synthetic.main.activity_create_note.*
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.android.ext.android.inject

class CreateNoteActivity : AppCompatActivity() {

    val viewModel: MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)
        backIcon.setOnClickListener {
            onBackPressed()
        }
        btnAdd.setOnClickListener {
            if (etTitle.text?.isEmpty() == true) {
                etTitle.error = "Title must not be empty"
            } else {
                viewModel.createNote(
                    Note(
                        10, "yellow",
                        "25.03.2020", 0,
                        etNoteText.text.toString(),
                        etTitle.text.toString()
                    )
                )
            }
            btnAdd.isEnabled = false
        }
        viewModel.errorLiveData.observe(this, Observer {
            showMessage(it)
            btnAdd.isEnabled = true
        })
        viewModel.resultValueLiveData.observe(this, Observer {
            if (it) {
                showMessage("Note successfully created")
                supportFragmentManager.popBackStack()
                finish()
            } else {
                showMessage("Error")
            }
        })

    }

    fun showMessage(message:String){
        Snackbar.make(constraintLayout,message,Snackbar.LENGTH_SHORT).show()
    }
}
