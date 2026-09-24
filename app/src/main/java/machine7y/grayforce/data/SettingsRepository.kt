package machine7y.grayforce.data

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    suspend fun setDelay(newDelay: Float)

    fun delayFlow(): Flow<Float>

    suspend fun setGrayForceEnable(isEnable: Boolean)

    fun grayForceEnabledFlow(): Flow<Boolean>
}
