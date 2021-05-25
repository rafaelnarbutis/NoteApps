package br.senac.noteapp.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.room.Room
import br.senac.noteapp.R
import br.senac.noteapp.databinding.ActivityListNotesBinding
import br.senac.noteapp.databinding.ActivityNewNoteBinding
import br.senac.noteapp.databinding.CardNoteBinding
import br.senac.noteapp.db.AppDatabase
import br.senac.noteapp.model.Note
import java.lang.Exception
import java.util.prefs.Preferences

class ListNotesActivity : AppCompatActivity() {
    lateinit var binding: ActivityListNotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(this, NewNoteActivity::class.java))
        }

        refreshNotes()
    }


    override fun onResume() {
        super.onResume()
        refreshNotes()
    }

    fun refreshNotes() {
        Thread{

            val pref = PreferenceManager.getDefaultSharedPreferences(this)

            var titleMaxSize = 20
            var descriptionMaxSize = 40

            try {
                titleMaxSize = pref.getString("title_size", "20")?.toInt() ?: 20
                descriptionMaxSize = pref.getString("description_size", "40")?.toInt() ?: 40
            } catch (e: Exception) {
                e.message
            }

            val db = Room.databaseBuilder(this, AppDatabase::class.java, "AppDb").build()

            val noteList = db.noteDao().findByMaxSize(titleMaxSize, descriptionMaxSize)

            runOnUiThread{
                updateUI(noteList, pref)
            }

        }.start()
    }


    fun updateUI(notes: List<Note>, pref: SharedPreferences) {
        binding.containerNote.removeAllViews()

        val colorTitle = pref.getInt("title_color", R.color.textDefaultColor)
        val colorDescription = pref.getInt("description_color", R.color.textDefaultColor)

        notes.forEach {
            val card = CardNoteBinding.inflate(layoutInflater)

            card.txtTitle.text = it.title
            card.txtTitle.setTextColor(colorTitle)
            card.txtDesc.text = it.description
            card.txtDesc.setTextColor(colorDescription)
            card.txtAuthor.text = it.user
            card.root.setCardBackgroundColor(it.color)

            val id = it.id
            val title = it.title
            val desc = it.description

            card.root.setOnClickListener {
                val intent = Intent(this, InfoNoteActivity::class.java)

                intent.putExtra("id", id)
                intent.putExtra("title", title)
                intent.putExtra("description", desc)

                startActivity(intent)
            }

            binding.containerNote.addView(card.root)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.user -> startActivity(Intent(this, UserActivity::class.java))
            R.id.config -> startActivity(Intent(this, SettingsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}