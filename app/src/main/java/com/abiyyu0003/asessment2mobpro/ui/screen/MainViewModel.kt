package com.abiyyu0003.asessment2mobpro.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abiyyu0003.asessment2mobpro.database.CatatanDao
import com.abiyyu0003.asessment2mobpro.network.CatatanApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val dao: CatatanDao
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val searchQuery = MutableStateFlow("")

    val data = combine(
        dao.getCatatan(),
        searchQuery
    ) { list, query ->

        if (query.isBlank()) {
            list
        } else {
            list.filter {
                it.judul_materi.contains(
                    query,
                    ignoreCase = true
                ) ||
                        it.isi_materi.contains(
                            query,
                            ignoreCase = true
                        )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    val jumlahCatatan = dao
        .getJumlahCatatan()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = 0
        )

    init {
        insertDataFromApi()
    }

    fun insertDataFromApi() {

        viewModelScope.launch {

            _isLoading.value = true

            delay(2000)

            if (dao.getCount() == 0) {

                try {

                    val apiData =
                        CatatanApi.service.getCatatan()

                    apiData.forEach { catatan ->
                        dao.insert(catatan
                        )
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            _isLoading.value = false
        }
    }

    fun search(query: String) {
        searchQuery.value = query
    }
}