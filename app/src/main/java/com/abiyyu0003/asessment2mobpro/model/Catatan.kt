package com.abiyyu0003.asessment2mobpro.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "catatan")
data class Catatan(

    @PrimaryKey
    val id: Long,

    val judul_materi: String,

    val isi_materi: String,

    val tanggal: String
)