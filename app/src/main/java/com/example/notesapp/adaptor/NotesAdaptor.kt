package com.example.notesapp.adaptor

import android.content.Context
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.models.Note
import java.io.PipedReader
import kotlin.random.Random

class NotesAdaptor(private val context: Context, val listener: NotesClickListener) :
    RecyclerView.Adapter<NotesAdaptor.NoteViewHolder>() {

    private val NotesList = ArrayList<Note>()
    private val fullList = ArrayList<Note>()

    inner class NoteViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        val notes_layout = itemview.findViewById<CardView>(R.id.card_layout)
        val title = itemview.findViewById<TextView>(R.id.tv_title)
        val note = itemview.findViewById<TextView>(R.id.tv_notes)
        val date = itemview.findViewById<TextView>(R.id.tv_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return NotesList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = NotesList[position]
        holder.title.text = currentNote.title
        holder.title.isSelected = true

        holder.note.text = currentNote.note
        holder.date.text = currentNote.date
        holder.date.isSelected = true

        //Generate Random Color for Recycler View
        holder.notes_layout.setCardBackgroundColor(
            holder.itemView.resources.getColor(
                randomcolor(),
                null
            )
        )

        //Here Update your note
        holder.notes_layout.setOnClickListener {
            listener.onItemClick(NotesList[holder.adapterPosition])
        }

        //Here Delete your notes
        holder.notes_layout.setOnLongClickListener {
            listener.onLongItemClick(NotesList[holder.adapterPosition], holder.notes_layout)
            true
        }
    }

    fun randomcolor(): Int {

        val list = ArrayList<Int>()
        list.add(R.color.notecolor1)
        list.add(R.color.notecolor2)
        list.add(R.color.notecolor3)
        list.add(R.color.notecolor4)
        list.add(R.color.notecolor5)
        list.add(R.color.notecolor6)

        val seed = System.currentTimeMillis().toInt()
        val randomIndex = Random(seed).nextInt(list.size)
        return list[randomIndex]
    }

    interface NotesClickListener {
        fun onItemClick(note: Note)
        fun onLongItemClick(note: Note, cardView: CardView)
        abstract fun popUpDisplay(cardView: CardView)
    }

    fun updateList(newList: List<Note>) {

        fullList.clear()
        fullList.addAll(newList)

        NotesList.clear()
        NotesList.addAll(fullList)
        notifyDataSetChanged()
    }

    fun filterList(search: String) {

        NotesList.clear()
        for (item in fullList) {

            if (item.title?.lowercase()
                    ?.contains(search.lowercase()) == true || item.note?.lowercase()
                    ?.contains(search.lowercase()) == true
            ){
                NotesList.add(item)
            }
        }
        notifyDataSetChanged()
    }
}