package dev.pedroayon.pdm10_abpj.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import dev.pedroayon.pdm10_abpj.domain.model.Person

@Dao
interface PersonDao {
    @Insert
    suspend fun insert(person: Person)

    @Update
    suspend fun update(person: Person)

    @Delete
    suspend fun delete(person: Person)

    @Query("SELECT * FROM person")
    suspend fun getAll(): List<Person>

    @Query("SELECT * FROM person WHERE id = :id")
    suspend fun getById(id: Int): Person?
}
