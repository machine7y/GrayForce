package machine7y.grayforce

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import machine7y.grayforce.data.DeviceSettingsRepository
import machine7y.grayforce.data.SettingsRepository
import machine7y.grayforce.presentation.utils.switchGrayscale
import javax.inject.Inject

@AndroidEntryPoint
class AlertReceiver : BroadcastReceiver() {

    @Inject
    lateinit var deviceSettingsRepository: DeviceSettingsRepository
    @Inject
    lateinit var settingsRepository: SettingsRepository

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onReceive(context: Context, intent: Intent) {
        if (!deviceSettingsRepository.isGrayscaleEnabled()) {
            switchGrayscale(
                context,
                coroutineScope,
                settingsRepository,
                deviceSettingsRepository
            )
        }
    }
}
