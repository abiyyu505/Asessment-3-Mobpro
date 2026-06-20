package com.abiyyu0003.asessment2mobpro.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.abiyyu0003.asessment2mobpro.model.Catatan
import kotlinx.coroutines.flow.Flow

@Dao
interface CatatanDao {

    @Query("SELECT * FROM catatan ORDER BY id DESC")
    fun getCatatan(): Flow<List<Catatan>>

    @Query("SELECT * FROM catatan WHERE id = :id")
    suspend fun getCatatanById(id: Long): Catatan?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(catatan: Catatan): Long

    @Update
    suspend fun update(catatan: Catatan)

    @Query("DELETE FROM catatan WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT COUNT(*) FROM catatan")
    suspend fun getCount(): Int

    @Query("SELECT MAX(id) FROM catatan")
    suspend fun getMaxId(): Long?

    @Query("""
    SELECT * FROM catatan
    WHERE judul_materi LIKE '%' || :query || '%'
       OR isi_materi LIKE '%' || :query || '%'
    ORDER BY id DESC
""")
    fun searchCatatan(query: String): Flow<List<Catatan>>

    @Query("SELECT COUNT(*) FROM catatan")
    fun getJumlahCatatan(): Flow<Int>
}