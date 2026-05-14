package com.abiyyu0003.asessment2mobpro.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(
    name = "settings"
)

class SettingsDataStore(private val context: Context) {

    companion object {
        private val LAYOUT_KEY = booleanPreferencesKey("layout_key")
    }

    val layoutFlow: Flow<Boolean> = context.dataStore.data.map {
        it[LAYOUT_KEY] ?: true
    }

    suspend fun saveLayout(isLinearLayout: Boolean) {
        context.dataStore.edit {
            it[LAYOUT_KEY] = isLinearLayout
        }
    }
}