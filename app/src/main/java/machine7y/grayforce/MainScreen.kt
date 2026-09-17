package machine7y.grayforce

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import machine7y.grayforce.manager.MainManager
import machine7y.grayforce.manager.MainManagerMock

@Composable
fun MainScreen(mainManager: MainManager) {
    var hasWriteSecureSettingsPermissionGranted by remember {
        mutableStateOf(mainManager.hasWriteSecureSettingsPermission())
    }

    ObserveOnResume {
        hasWriteSecureSettingsPermissionGranted = mainManager.hasWriteSecureSettingsPermission()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = Color.LightGray,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            if (hasWriteSecureSettingsPermissionGranted) {
                Button(
                    onClick = {
                        mainManager.switch()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.switch_),
                        fontSize = 18.sp,
                    )
                }
            } else {
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
                                packageName
                            )
                        )
                    },
                    confirmButton = {},
                    dismissButton = {},
                )
            }
        }
    }
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
fun MainScreenPreview() {
    MainScreen(MainManagerMock())
}