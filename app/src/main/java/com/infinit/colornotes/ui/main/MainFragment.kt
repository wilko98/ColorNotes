package com.infinit.colornotes.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.infinit.colornotes.R
import com.infinit.colornotes.createScreen.CreateNoteActivity
import com.infinit.colornotes.model.Note
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.android.ext.android.inject

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }
    val viewModel: MainViewModel by inject()
    lateinit var adapter: NotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.main_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = NotesAdapter(ArrayList<Note>(0),viewModel)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        viewModel.getNotes()
        adapter.notifyDataSetChanged()

        swipeRefreshLayout.setOnRefreshListener {
                viewModel.getNotes()
        }
        viewModel.errorLiveData.observe(this, Observer {
            swipeRefreshLayout.isRefreshing = false
            showMessage(it)
        })
        viewModel.resultLiveData.observe(this, Observer {
            adapter.items.clear()
            adapter.items.addAll(it)
            adapter.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false

        })

        fab.setOnClickListener {
            val intent = Intent(activity, CreateNoteActivity::class.java)
            startActivity(intent)
        }

    }

    fun showMessage(message:String){
        this.view?.let { Snackbar.make(it,message,Snackbar.LENGTH_SHORT).show() }
    }
}
