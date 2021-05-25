package br.senac.noteapp.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.senac.noteapp.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {
    lateinit var binding : ActivityUserBinding

    val sharedName = "user_preferences"
    val userParam = "user_name"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val shared = getSharedPreferences(sharedName, Context.MODE_PRIVATE)

        setUser(shared)

        binding.btnSave.setOnClickListener {
            val username = binding.etUsername.text.toString()
            shared.edit()
                .putString(userParam, username)
                .commit()
            finish()
        }

    }

    fun setUser(shared: SharedPreferences) {
         binding.etUsername.setText(shared.getString(userParam, ""))
    }

}