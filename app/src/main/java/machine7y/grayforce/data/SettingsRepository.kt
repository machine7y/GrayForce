package machine7y.grayforce.data

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    suspend fun setDelay(newDelay: Int)

    fun delayFlow(): Flow<Int>

    suspend fun setGrayForceEnable(isEnable: Boolean)

    fun grayForceEnabledFlow(): Flow<Boolean>
}
