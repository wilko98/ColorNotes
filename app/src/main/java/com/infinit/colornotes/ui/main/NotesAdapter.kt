package com.infinit.colornotes.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infinit.colornotes.R
import com.infinit.colornotes.model.Note
import kotlinx.android.synthetic.main.item_note.view.*
import java.util.*

class NotesAdapter(var items: ArrayList<Note>,val viewModel: MainViewModel): RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    companion object{
        const val VIEW_TYPE_NOTE = 1
        const val VIEW_TYPE_PHOTO = 2
        const val VIEW_TYPE_AUDIO = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
            return NotesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_note,parent,false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val item = items[position]
        holder.itemView.itemTitle.text = item.title
        holder.itemView.itemText.text = item.text
        holder.itemView.itemDate.text = item.date
        holder.itemView.setOnClickListener {
            items[position].Id?.let { it1 -> viewModel.deleteNote(it1) }
            items.removeAt(position)
            notifyDataSetChanged()
        }
    }

    class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }
}