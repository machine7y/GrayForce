package machine7y.grayforce

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import machine7y.grayforce.manager.MainManagerImpl

class MainTileService : TileService() {

    private val mainManager by lazy {
        MainManagerImpl(applicationContext)
    }

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
