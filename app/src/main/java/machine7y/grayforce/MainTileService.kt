package machine7y.grayforce

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import machine7y.grayforce.manager.MainManagerImpl

class MainTileService : TileService() {

    private val mainManager by lazy {
        MainManagerImpl(applicationContext)
    }

    override fun onClick() {
        super.onClick()
        val tile = qsTile
        val newTileState = if (mainManager.hasWriteSecureSettingsPermission()) {
            mainManager.switch()
            if (mainManager.isGrayscaleEnabled()) {
                Tile.STATE_ACTIVE
            } else {
                Tile.STATE_INACTIVE
            }
        } else {
            Tile.STATE_INACTIVE
        }

        tile.state = newTileState
        tile.updateTile()
    }
}
