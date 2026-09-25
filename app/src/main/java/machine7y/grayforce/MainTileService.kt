package machine7y.grayforce

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import machine7y.grayforce.data.DeviceSettingsRepository
import machine7y.grayforce.data.SettingsRepository
import machine7y.grayforce.presentation.utils.switchGrayscale
import javax.inject.Inject

@AndroidEntryPoint
class MainTileService : TileService() {

    @Inject
    lateinit var deviceSettingsRepository: DeviceSettingsRepository
    @Inject
    lateinit var settingsRepository: SettingsRepository

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onTileAdded() {
        deviceSettingsRepository.disableGrayscale()
        updateTile()
    }

    override fun onStartListening() {
        updateTile()
    }

    override fun onClick() {
        if (!deviceSettingsRepository.hasWriteSecureSettingsPermission()) {
            updateTile()
            return
        }

        switchGrayscale(
            context = this,
            coroutineScope = coroutineScope,
            settingsRepository = settingsRepository,
            deviceSettingsRepository = deviceSettingsRepository,
        )
    }

    private fun updateTile() {
        val hasWriteSecureSettingsPermission = deviceSettingsRepository.hasWriteSecureSettingsPermission()
        val isGrayscaleEnabled = deviceSettingsRepository.isGrayscaleEnabled()

        qsTile.state = if (hasWriteSecureSettingsPermission && isGrayscaleEnabled) {
            Tile.STATE_ACTIVE
        } else {
            Tile.STATE_INACTIVE
        }
        qsTile.updateTile()
    }
}
