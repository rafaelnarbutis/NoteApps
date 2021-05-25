package br.senac.noteapp.db

import androidx.room.*
import br.senac.noteapp.model.Note

@Dao
interface NoteDao {

    @Insert
    fun insert(note: Note)

    @Query("DELETE FROM Note WHERE ID = :id")
    fun delete(id: Long)

    @Query("SELECT * FROM NOTE")
    fun findAll(): List<Note>

    @Query("SELECT * FROM NOTE WHERE ID = :id")
    fun findById(id: Long) : Note

    @Query("SELECT * FROM NOTE WHERE LENGTH(description) <= :titleMax AND LENGTH(description) <= :descriptionMax")
    fun findByMaxSize(titleMax: Int, descriptionMax: Int): List<Note>

    @Update
    fun update(note: Note)
}