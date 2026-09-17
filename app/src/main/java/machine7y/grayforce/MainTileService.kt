package machine7y.grayforce

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import dagger.hilt.android.AndroidEntryPoint
import machine7y.grayforce.data.SettingsRepository
import javax.inject.Inject

@AndroidEntryPoint
class MainTileService : TileService() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onTileAdded() {
        settingsRepository.disableGrayscale()
        updateTile()
    }

    override fun onStartListening() {
        super.onStartListening()
        updateTile()
    }

    override fun onClick() {
        if (!settingsRepository.hasWriteSecureSettingsPermission()) {
            updateTile()
            return
        }

        settingsRepository.switch()
        updateTile()
    }

    private fun updateTile() {
        qsTile.state = if (settingsRepository.hasWriteSecureSettingsPermission() && settingsRepository.isGrayscaleEnabled()) {
            Tile.STATE_ACTIVE
        } else {
            Tile.STATE_INACTIVE
        }
        qsTile.updateTile()
    }
}
