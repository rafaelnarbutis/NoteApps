package br.senac.noteapp.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceManager
import androidx.room.Room
import br.senac.noteapp.R
import br.senac.noteapp.databinding.ActivityInfoNoteBinding
import br.senac.noteapp.db.AppDatabase
import br.senac.noteapp.db.NoteDao
import br.senac.noteapp.model.Note

class InfoNoteActivity : AppCompatActivity() {

    lateinit var binding: ActivityInfoNoteBinding

    val sharedName = "user_preferences"
    val userParam = "user_name"
    var id: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getValues()

        val db = Room.databaseBuilder(this, AppDatabase::class.java, "AppDb").build()
        val noteDao = db.noteDao()

        binding.btnUpdate.setOnClickListener {
            val title = binding.etTitleInfo.text.toString()
            val desc = binding.etDescInfo.text.toString()


            val user = getSharedPreferences(sharedName, Context.MODE_PRIVATE)
                .getString(userParam, "") as String


            val pref = PreferenceManager.getDefaultSharedPreferences(this)
            val color = pref.getInt("noteColor", R.color.noteDefaultColor)

            val note = Note(id = id, title = title, description = desc, user = user, color = color)


            Thread {
                updateNote(note, noteDao)
                finish()
            }.start()
        }


        binding.btnDelete.setOnClickListener {

            Thread {
                delete(id, noteDao)
                finish()
            }.start()

        }

    }

    fun updateNote(note: Note, noteDao: NoteDao) {
        noteDao.update(note)
    }

    fun delete(id: Long, noteDao: NoteDao) {
        noteDao.delete(id)
    }

    fun getValues() {
        id = intent.getLongExtra("id",-1L)
        binding.etTitleInfo.setText(intent.getStringExtra("title"))
        binding.etDescInfo.setText(intent.getStringExtra("description"))
    }
}