package machine7y.grayforce.presentation.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.service.quicksettings.TileService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import machine7y.grayforce.AlertReceiver
import machine7y.grayforce.MainTileService
import machine7y.grayforce.data.DeviceSettingsRepository
import machine7y.grayforce.data.SettingsRepository

private const val ALERT_REQUEST_CODE = 1

// TODO: Refactor
fun switchGrayscale(
    context: Context,
    coroutineScope: CoroutineScope,
    settingsRepository: SettingsRepository,
    deviceSettingsRepository: DeviceSettingsRepository,
) {
    coroutineScope.launch {
        beforeSwitchLaunchAlarmIfNeeded(
            context = context,
            settingsRepository = settingsRepository,
            deviceSettingsRepository = deviceSettingsRepository,
        )
        deviceSettingsRepository.switch()
        // TODO: Fix delay
        delay(100)
        TileService.requestListeningState(context, ComponentName(context, MainTileService::class.java))
    }
}

private suspend fun beforeSwitchLaunchAlarmIfNeeded(
    context: Context,
    settingsRepository: SettingsRepository,
    deviceSettingsRepository: DeviceSettingsRepository,
) {
    val isGrayForceEnable = settingsRepository.grayForceEnabledFlow().first()

    if (!deviceSettingsRepository.isGrayscaleEnabled()) return
    if (!isGrayForceEnable) return

    val alarmManager = context.getSystemService(AlarmManager::class.java)
    val delayInSecond = settingsRepository.delayFlow().first() * 1000
    val intent = Intent(context, AlertReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        /* context = */ context,
        /* requestCode = */ ALERT_REQUEST_CODE,
        /* intent = */ intent,
        /* flags = */ PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )
    val triggerAt = System.currentTimeMillis() + delayInSecond

    try {
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
    } catch (_: SecurityException) {
        // no op
    }
}
