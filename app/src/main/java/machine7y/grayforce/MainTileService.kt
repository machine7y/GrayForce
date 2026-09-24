package machine7y.grayforce

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import dagger.hilt.android.AndroidEntryPoint
import machine7y.grayforce.data.DeviceSettingsRepository
import javax.inject.Inject

@AndroidEntryPoint
class MainTileService : TileService() {

    @Inject
    lateinit var deviceSettingsRepository: DeviceSettingsRepository

    override fun onTileAdded() {
        deviceSettingsRepository.disableGrayscale()
        updateTile()
    }

    override fun onStartListening() {
        super.onStartListening()
        updateTile()
    }

    override fun onClick() {
        if (!deviceSettingsRepository.hasWriteSecureSettingsPermission()) {
            updateTile()
            return
        }

        deviceSettingsRepository.switch()
        updateTile()
    }

    private fun updateTile() {
        qsTile.state = if (deviceSettingsRepository.hasWriteSecureSettingsPermission() && deviceSettingsRepository.isGrayscaleEnabled()) {
            Tile.STATE_ACTIVE
        } else {
            Tile.STATE_INACTIVE
        }
        qsTile.updateTile()
    }
}
