package machine7y.grayforce

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import dagger.hilt.android.AndroidEntryPoint
import machine7y.grayforce.manager.MainManager
import javax.inject.Inject

@AndroidEntryPoint
class MainTileService : TileService() {

    @Inject
    lateinit var mainManager: MainManager

    override fun onTileAdded() {
        mainManager.disableGrayscale()
        updateTile()
    }

    override fun onClick() {
        if (!mainManager.hasWriteSecureSettingsPermission()) {
            updateTile()
            return
        }

        mainManager.switch()
        updateTile()
    }

    private fun updateTile() {
        qsTile.state = if (mainManager.hasWriteSecureSettingsPermission() && mainManager.isGrayscaleEnabled()) {
            Tile.STATE_ACTIVE
        } else {
            Tile.STATE_INACTIVE
        }

        qsTile.updateTile()
    }
}
