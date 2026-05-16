package com.abiyyu0003.asessment2mobpro.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abiyyu0003.asessment2mobpro.database.CatatanDao
import com.abiyyu0003.asessment2mobpro.model.Catatan
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailViewModel(
    private val dao: CatatanDao
) : ViewModel() {

    suspend fun getCatatan(id: Long): Catatan? {
        return dao.getCatatanById(id)
    }

    fun insert(judul: String, isi: String) {
        val catatan = Catatan(
            judul_materi = judul,
            isi_materi = isi,
            tanggal = getCurrentDate()
        )

        viewModelScope.launch {
            dao.insert(catatan)
        }
    }

    fun update(id: Long, judul: String, isi: String) {
        val catatan = Catatan(
            id = id,
            judul_materi = judul,
            isi_materi = isi,
            tanggal = getCurrentDate()
        )

        viewModelScope.launch {
            dao.update(catatan)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch {
            dao.deleteById(id)
        }
    }

    private fun getCurrentDate(): String {
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
        return formatter.format(Date())
    }
}