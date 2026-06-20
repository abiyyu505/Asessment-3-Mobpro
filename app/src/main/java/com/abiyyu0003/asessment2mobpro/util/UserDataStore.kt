package com.abiyyu0003.asessment2mobpro.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.abiyyu0003.asessment2mobpro.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.userDataStore by preferencesDataStore(
    name = "user_preferences"
)

class UserDataStore(
    private val context: Context
) {

    companion object {

        private val NAME =
            stringPreferencesKey("name")

        private val EMAIL =
            stringPreferencesKey("email")

        private val PHOTO =
            stringPreferencesKey("photo")
    }

    val userFlow: Flow<User> =
        context.userDataStore.data.map {

            User(
                name = it[NAME] ?: "",
                email = it[EMAIL] ?: "",
                photoUrl = it[PHOTO] ?: ""
            )
        }

    suspend fun saveData(
        user: User
    ) {

        context.userDataStore.edit {

            it[NAME] = user.name
            it[EMAIL] = user.email
            it[PHOTO] = user.photoUrl
        }
    }

    suspend fun clearData() {

        context.userDataStore.edit {

            it.clear()
        }
    }
}