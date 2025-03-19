package dev.pedroayon.pdm10_abpj.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.pedroayon.pdm10_abpj.domain.model.Person

@Dao
interface PersonDao {
    @Insert
    suspend fun insert(person: Person)

    @Query("SELECT * FROM person")
    suspend fun getAll(): List<Person>
}
