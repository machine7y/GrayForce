package machine7y.grayforce.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import machine7y.grayforce.DEFAULT_DELAY
import machine7y.grayforce.DEFAULT_GRAY_FORCE_ENABLE
import machine7y.grayforce.R
import machine7y.grayforce.data.DeviceSettingsRepository
import machine7y.grayforce.data.SettingsRepository
import machine7y.grayforce.delayRange
import machine7y.grayforce.presentation.components.ClickableText
import machine7y.grayforce.presentation.components.DebouncedIconButton
import machine7y.grayforce.presentation.lifecycle.OnResumeEffect
import machine7y.grayforce.presentation.modifier.bottomShadow
import machine7y.grayforce.presentation.theme.Theme
import machine7y.grayforce.presentation.theme.appColorScheme
import machine7y.grayforce.presentation.utils.copyToClipboard
import machine7y.grayforce.presentation.utils.launchAlarmIfNeeded
import machine7y.grayforce.presentation.utils.scaleValue
import machine7y.grayforce.presentation.utils.switchGrayscale
import machine7y.grayforce.presentation.utils.toDurationString
import machine7y.grayforce.presentation.utils.unscaleValue

@Composable
fun SettingsScreen(deviceSettingsRepository: DeviceSettingsRepository, settingsRepository: SettingsRepository) {
    val context = LocalContext.current
    val clipboard: Clipboard = LocalClipboard.current
    val githubLink = stringResource(R.string.github_link)
    val copiedMessageFormat = stringResource(R.string.copied_format)

    var hasWriteSecureSettingsPermissionGranted by remember {
        mutableStateOf(deviceSettingsRepository.hasWriteSecureSettingsPermission())
    }

    val onTextClicked: suspend (String) -> Unit = {
        val message = String.format(copiedMessageFormat, it)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        clipboard.copyToClipboard(it)
    }
    OnResumeEffect {
        hasWriteSecureSettingsPermissionGranted = deviceSettingsRepository.hasWriteSecureSettingsPermission()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .bottomShadow(
                        height = 6.dp,
                        color = MaterialTheme.appColorScheme.shadow,
                    )
                    .height(TopAppBarDefaults.TopAppBarExpandedHeight),
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    fontSize = 26.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.Center),
                )
            }
            if (hasWriteSecureSettingsPermissionGranted) {
                Settings(
                    deviceSettingsRepository = deviceSettingsRepository,
                    settingsRepository = settingsRepository,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(20.dp),
                )
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
private fun Settings(
    deviceSettingsRepository: DeviceSettingsRepository,
    settingsRepository: SettingsRepository,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val grayForceState = settingsRepository
        .grayForceEnabledFlow()
        .collectAsStateWithLifecycle(initialValue = DEFAULT_GRAY_FORCE_ENABLE)
    val delayState by settingsRepository
        .delayFlow()
        .collectAsStateWithLifecycle(initialValue = DEFAULT_DELAY)
    var sliderValueState by remember { mutableFloatStateOf(delayState.unscaleValue().toFloat()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(delayState) {
        sliderValueState = delayState.unscaleValue().toFloat()
    }
    Column(
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.setup_countdown_timer),
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
        )
        Spacer(
            modifier = Modifier
                .height(30.dp),
        )
        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                val incDecTint = if (grayForceState.value) {
                    MaterialTheme.appColorScheme.disabled
                } else {
                    MaterialTheme.appColorScheme.secondary
                }
                val incDecBackground = if (grayForceState.value) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.appColorScheme.primary
                }

                DebouncedIconButton(
                    onClick = {
                        sliderValueState--
                        coroutineScope.launch {
                            settingsRepository.setDelay(sliderValueState.toInt().scaleValue())
                        }
                    },
                    enabled = !grayForceState.value,
                    modifier = Modifier
                        .background(
                            color = incDecBackground,
                            shape = CircleShape,
                        )
                        .size(26.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = null,
                        tint = incDecTint,
                    )
                }
                Text(
                    text = sliderValueState.toInt().scaleValue().toDurationString(context),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f),
                )
                DebouncedIconButton(
                    onClick = {
                        sliderValueState++
                        coroutineScope.launch {
                            settingsRepository.setDelay(sliderValueState.toInt().scaleValue())
                        }
                    },
                    enabled = !grayForceState.value,
                    modifier = Modifier
                        .background(
                            color = incDecBackground,
                            shape = CircleShape,
                        )
                        .size(26.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = incDecTint,
                    )
                }
            }

            Slider(
                value = sliderValueState,
                onValueChange = { sliderValueState = it },
                onValueChangeFinished = {
                    coroutineScope.launch {
                        settingsRepository.setDelay(sliderValueState.toInt().scaleValue())
                    }
                },
                valueRange = delayRange,
                steps = delayRange.endInclusive.toInt() - delayRange.start.toInt(),
                enabled = !grayForceState.value,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.appColorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.surface,
                    inactiveTrackColor = MaterialTheme.colorScheme.surface,
                    inactiveTickColor = MaterialTheme.appColorScheme.primary,
                    activeTickColor = MaterialTheme.appColorScheme.primary,
                    disabledThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledActiveTrackColor = MaterialTheme.colorScheme.surface,
                    disabledInactiveTrackColor = MaterialTheme.colorScheme.surface,
                    disabledInactiveTickColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledActiveTickColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            )
            Spacer(
                modifier = Modifier
                    .height(20.dp),
            )
            Switch(
                checked = grayForceState.value,
                onCheckedChange = { checked ->
                    coroutineScope.launch {
                        settingsRepository.setGrayForceEnable(checked)
                        launchAlarmIfNeeded(
                            context = context,
                            settingsRepository = settingsRepository,
                            deviceSettingsRepository = deviceSettingsRepository,
                        )
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.surface,
                    checkedTrackColor = MaterialTheme.appColorScheme.primary,
                    uncheckedThumbColor = MaterialTheme.appColorScheme.primary,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surface,
                    uncheckedBorderColor = MaterialTheme.appColorScheme.primary,
                    checkedBorderColor = MaterialTheme.appColorScheme.primary,
                ),
            )
        }

        IconButton(
            onClick = {
                switchGrayscale(
                    context = context,
                    coroutineScope = coroutineScope,
                    settingsRepository = settingsRepository,
                    deviceSettingsRepository = deviceSettingsRepository,
                )
            },
            modifier = Modifier
                .background(
                    color = MaterialTheme.appColorScheme.primary,
                    shape = CircleShape,
                )
                .align(Alignment.CenterHorizontally)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_circle_arrows),
                contentDescription = null,
                tint = MaterialTheme.appColorScheme.secondary,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    Theme {
        SettingsScreen(mockDeviceSettingsRepository(), mockSettingsRepository())
    }
}

fun mockSettingsRepository() = object : SettingsRepository {

    override suspend fun setDelay(newDelay: Int) {}

    override fun delayFlow(): Flow<Int> = flowOf()

    override suspend fun setGrayForceEnable(isEnable: Boolean) {}

    override fun grayForceEnabledFlow(): Flow<Boolean> = flowOf()
}

fun mockDeviceSettingsRepository() = object : DeviceSettingsRepository {

    override fun hasWriteSecureSettingsPermission() = true

    override fun enableGrayscale() {}

    override fun disableGrayscale() {}

    override fun isGrayscaleEnabled() = true

    override fun switch() {}
}
