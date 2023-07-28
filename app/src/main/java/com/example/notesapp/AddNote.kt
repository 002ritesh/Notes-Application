package com.example.notesapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Note
import android.widget.Toast
import com.example.notesapp.databinding.ActivityAddNoteBinding
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date


class AddNote : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var note: com.example.notesapp.models.Note
    private lateinit var oldNote: com.example.notesapp.models.Note
    var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Perform try and catch block to check exception
        try {

            oldNote = intent.getSerializableExtra("current_note") as com.example.notesapp.models.Note
            binding.editTextText.setText(oldNote.title)
            binding.editTextText2.setText(oldNote.note)
            isUpdate = true

        }catch (e: Exception){
            e.printStackTrace()
        }


        //Check image button to add your notes
        binding.imgCheck.setOnClickListener {
            val title = binding.editTextText.text.toString()
            val noteDesc = binding.editTextText2.text.toString()

            if (title.isNotEmpty() || noteDesc.isNotEmpty()){
                val formatter = SimpleDateFormat("EEE, d MM yyyy HH:mm a")

                if (isUpdate){
                    note = com.example.notesapp.models.Note(oldNote.id, title,noteDesc,formatter.format(
                        Date()
                    ))
                }else{
                    note = com.example.notesapp.models.Note(null, title, noteDesc, formatter.format(
                        Date()
                    ))

                }

                val intent = Intent()
                intent.putExtra("note", note)
                setResult(Activity.RESULT_OK,intent)
                finish()

            }else{
                Toast.makeText(this@AddNote, "Please Add Some Data...?", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }


        //If Press back button you can go back Activity
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

    }
}
