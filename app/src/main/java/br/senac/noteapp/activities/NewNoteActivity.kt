package br.senac.noteapp.activities

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.room.Room
import br.senac.noteapp.R
import br.senac.noteapp.databinding.ActivityNewNoteBinding
import br.senac.noteapp.db.AppDatabase
import br.senac.noteapp.model.Note

class NewNoteActivity : AppCompatActivity() {
    lateinit var binding: ActivityNewNoteBinding

    val sharedName = "user_preferences"
    val userParam = "user_name"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val desc = binding.etDesc.text.toString()


            val user = getSharedPreferences(sharedName, Context.MODE_PRIVATE)
                .getString(userParam, "") as String


            val pref = PreferenceManager.getDefaultSharedPreferences(this)
            val color = pref.getInt("noteColor", R.color.noteDefaultColor)

            val note = Note(title = title, description = desc, user = user, color = color)

            Thread {
                saveNote(note)
                finish()
            }.start()
        }

    }

    fun saveNote(note: Note) {
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "AppDb").build()
        db.noteDao().insert(note)
    }

}