package br.senac.noteapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note (
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    val title: String,
    val description: String,
    val user: String,
    val color: Int
)