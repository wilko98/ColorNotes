package com.infinit.colornotes.MainScreen

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
import com.infinit.colornotes.db.NotesDao
import com.infinit.colornotes.model.Note
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.android.ext.android.inject

class MainFragment : Fragment() {

    val viewModel: MainViewModel by inject()
    val notesDao: NotesDao by inject()
    lateinit var adapter: NotesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        setupObservers()
        setupListeners()
    }

    fun showMessage(message:String){
        this.view?.let { Snackbar.make(it,message,Snackbar.LENGTH_SHORT).show() }
    }

    private fun setupListeners() {
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.getNotes()
        }
        fab.setOnClickListener {
            val intent = Intent(activity, CreateNoteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        adapter = NotesAdapter(ArrayList(0),viewModel)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        viewModel.getNotes()
        adapter.notifyDataSetChanged()
    }

    private fun setupObservers() {
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
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.main_fragment, container, false)
}
