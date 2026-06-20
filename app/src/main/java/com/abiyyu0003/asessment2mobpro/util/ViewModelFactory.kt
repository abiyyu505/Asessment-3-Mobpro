package com.abiyyu0003.asessment2mobpro.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.abiyyu0003.asessment2mobpro.database.CatatanDao
import com.abiyyu0003.asessment2mobpro.ui.screen.DetailViewModel
import com.abiyyu0003.asessment2mobpro.ui.screen.MainViewModel

class ViewModelFactory(
    private val dao: CatatanDao
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(dao) as T
        }

        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(dao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}