package com.example.todoapp2025.data

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "profile_prefs")

object ProfileRepository {
    private val KEY_NAME = stringPreferencesKey("profile_name")

    fun getProfileName(context: Context): Flow<String?> =
        context.dataStore.data.map { prefs ->
            prefs[KEY_NAME]
        }

    suspend fun saveProfileName(context: Context, name: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_NAME] = name
        }
    }

    suspend fun deleteProfile(context: Context) {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_NAME)
        }
    }
}
