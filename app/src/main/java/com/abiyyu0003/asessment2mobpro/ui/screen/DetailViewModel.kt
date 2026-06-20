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

        viewModelScope.launch {

            val newId = (dao.getMaxId() ?: 0L) + 1

            val catatan = Catatan(
                id = newId,
                judul_materi = judul,
                isi_materi = isi,
                tanggal = getCurrentDate()
            )

            dao.insert(catatan)
        }
    }

    fun update(
        id: Long,
        judul: String,
        isi: String
    ) {

        viewModelScope.launch {

            val catatan = Catatan(
                id = id,
                judul_materi = judul,
                isi_materi = isi,
                tanggal = getCurrentDate()
            )

            dao.update(catatan)
        }
    }

    fun delete(id: Long) {

        viewModelScope.launch {
            dao.deleteById(id)
        }
    }

    private fun getCurrentDate(): String {

        val formatter = SimpleDateFormat(
            "dd MMMM yyyy",
            Locale("id", "ID")
        )

        return formatter.format(Date())
    }
}