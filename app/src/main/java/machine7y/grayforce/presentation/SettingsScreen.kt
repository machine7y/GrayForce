package machine7y.grayforce.presentation

import android.content.ComponentName
import android.service.quicksettings.TileService
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import machine7y.grayforce.MainTileService
import machine7y.grayforce.R
import machine7y.grayforce.data.SettingsRepository
import kotlin.jvm.java

@Composable
fun SettingsScreen(settingsRepository: SettingsRepository) {
    var hasWriteSecureSettingsPermissionGranted by remember {
        mutableStateOf(settingsRepository.hasWriteSecureSettingsPermission())
    }

    ObserveOnResume {
        hasWriteSecureSettingsPermissionGranted = settingsRepository.hasWriteSecureSettingsPermission()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            if (hasWriteSecureSettingsPermissionGranted) {
                Settings(settingsRepository)
            } else {
                PermissionErrorDialog()
            }
        }
    }
}

@Composable
private fun Settings(settingsRepository: SettingsRepository) {
    val context = LocalContext.current
    IconButton(
        onClick = {
            settingsRepository.switch()
            TileService.requestListeningState(context, ComponentName(context, MainTileService::class.java))
        },
        modifier = Modifier
            .background(
                color = colorSilkyTurquoise,
                shape = CircleShape,
            )
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_circle_arrows),
            contentDescription = null,
            tint = colorChineseCyan,
        )
    }
}

@Composable
private fun PermissionErrorDialog() {
    val packageName = LocalContext.current.packageName

    AlertDialog(
        onDismissRequest = { },
        title = {
            Text(stringResource(R.string.needed_permission))
        },
        text = {
            Text(
                text = stringResource(
                    R.string.use_command_on_pc_adb_shell_pm_grant_android_permission_write_secure_settings,
                    packageName,
                )
            )
        },
        confirmButton = { },
        dismissButton = { },
    )
}

@Composable
private fun ObserveOnResume(onResume: () -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                onResume()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(mockManager())
}

private fun mockManager() = object : SettingsRepository {

    override fun hasWriteSecureSettingsPermission() = true

    override fun enableGrayscale() {}

    override fun disableGrayscale() {}

    override fun isGrayscaleEnabled() = true

    override fun switch() {}
}