package com.example.notesapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.adaptor.NotesAdaptor
import com.example.notesapp.database.NoteDatabase
import com.example.notesapp.databinding.ActivityMainBinding
import com.example.notesapp.models.Note
import com.example.notesapp.models.NoteViewModel

class MainActivity : AppCompatActivity(), NotesAdaptor.NotesClickListener, PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: NoteDatabase
    lateinit var viewModel: NoteViewModel
    lateinit var adaptor: NotesAdaptor
    lateinit var selectedNote: Note

    private val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->

        if (result.resultCode == Activity.RESULT_OK){

            val note = result.data?.getSerializableExtra("note") as? Note
            if (note != null){
                viewModel.updateNote(note)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initializing the UI
        initUI()


        //Set the ViewModel
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModel::class.java)
        viewModel.allnotes.observe(this) { list ->
            list?.let {
                adaptor.updateList(list)
            }
        }
        database = NoteDatabase.getDatabase(this)

    }

    private fun initUI() {

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        adaptor = NotesAdaptor(this, this)
        binding.recyclerView.adapter = adaptor

        val getContent =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {

                    val note = result.data?.getSerializableExtra("note") as? Note
                    if (note != null){
                        viewModel.insertNote(note)
                    }
                }
            }

        //Click here to Add your Note
        binding.fbAddNote.setOnClickListener {
            val intent = Intent(this, AddNote::class.java)
            getContent.launch(intent)
        }

        //Search View To Fetch data for notes
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText != null){
                    adaptor.filterList(newText)
                }
                return true
            }

        })
    }


    override fun onItemClick(note: Note) {
        val intent = Intent(this@MainActivity, AddNote::class.java)
        intent.putExtra("current_note", note)

        updateNote.launch(intent)
    }

    override fun onLongItemClick(note: Note, cardView: CardView) {

        selectedNote = note
       popUpDisplay(cardView)
    }

    override fun popUpDisplay(cardView: CardView) {

        val popup = PopupMenu(this, cardView)
        popup.setOnMenuItemClickListener(this@MainActivity)
        popup.inflate(R.menu.pop_up_menu)
        popup.show()
    }


    override fun onMenuItemClick(item: MenuItem?): Boolean {

        if (item?.itemId == R.id.delete_note){
            viewModel.deleteNote(selectedNote)
            return true
        }
        return false
    }
}