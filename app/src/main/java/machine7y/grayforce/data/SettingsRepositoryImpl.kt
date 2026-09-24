package machine7y.grayforce.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import machine7y.grayforce.DEFAULT_DELAY
import machine7y.grayforce.DEFAULT_GRAY_FORCE_ENABLE
import javax.inject.Inject
import javax.inject.Singleton

private const val STORE_NAME = "settings"

private val delayKey = floatPreferencesKey("delay_key")

private val grayForceEnabledKey = booleanPreferencesKey("gray_force_enabled_key")

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : SettingsRepository {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = STORE_NAME)

    override suspend fun setDelay(newDelay: Float) {
        context.dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[delayKey] = newDelay
            }
        }
    }

    override fun delayFlow(): Flow<Float> = context.dataStore.data.map { preferences ->
        preferences[delayKey] ?: DEFAULT_DELAY
    }

    override suspend fun setGrayForceEnable(isEnable: Boolean) {
        context.dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[grayForceEnabledKey] = isEnable
            }
        }
    }

    override fun grayForceEnabledFlow(): Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[grayForceEnabledKey] ?: DEFAULT_GRAY_FORCE_ENABLE
    }
}
