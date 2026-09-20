package machine7y.grayforce.presentation

import android.content.ComponentName
import android.service.quicksettings.TileService
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import machine7y.grayforce.MainTileService
import machine7y.grayforce.R
import machine7y.grayforce.data.SettingsRepository
import machine7y.grayforce.presentation.components.ClickableText
import machine7y.grayforce.presentation.lifecycle.OnResumeEffect
import machine7y.grayforce.presentation.utils.copyToClipboard

@Composable
fun SettingsScreen(settingsRepository: SettingsRepository) {
    val context = LocalContext.current
    val clipboard: Clipboard = LocalClipboard.current
    val githubLink = stringResource(R.string.github_link)
    val copiedMessageFormat = stringResource(R.string.copied_format)

    var hasWriteSecureSettingsPermissionGranted by remember {
        mutableStateOf(settingsRepository.hasWriteSecureSettingsPermission())
    }

    val onTextClicked: suspend (String) -> Unit = {
        val message = String.format(copiedMessageFormat, it)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        clipboard.copyToClipboard(it)
    }
    OnResumeEffect {
        hasWriteSecureSettingsPermissionGranted = settingsRepository.hasWriteSecureSettingsPermission()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(TopAppBarDefaults.TopAppBarExpandedHeight),
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    fontSize = 26.sp,
                    modifier = Modifier
                        .align(Alignment.Center),
                )
            }
            if (hasWriteSecureSettingsPermissionGranted) {
                Settings(settingsRepository)
            } else {
                ClickableText(
                    text = stringResource(R.string.installation_instructions),
                    clickableTextList = listOf(
                        stringResource(R.string.installation_instructions_clickable1),
                        stringResource(R.string.installation_instructions_clickable2),
                        stringResource(R.string.installation_instructions_clickable3),
                    ),
                    onTextClicked = onTextClicked,
                    modifier = Modifier
                        .weight(1f)
                        .padding(20.dp, 0.dp, 20.dp, 0.dp)
                        .verticalScroll(rememberScrollState()),
                )
            }
            ClickableText(
                text = githubLink,
                clickableTextList = listOf(githubLink),
                onTextClicked = onTextClicked,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(),
            )
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
