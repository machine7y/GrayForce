package machine7y.grayforce.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import machine7y.grayforce.DEFAULT_DELAY
import machine7y.grayforce.DEFAULT_GRAY_FORCE_ENABLE
import machine7y.grayforce.R
import machine7y.grayforce.data.DeviceSettingsRepository
import machine7y.grayforce.data.SettingsRepository
import machine7y.grayforce.delayRange
import machine7y.grayforce.presentation.colorChineseCyan
import machine7y.grayforce.presentation.colorLightGray
import machine7y.grayforce.presentation.colorSilkyTurquoise
import machine7y.grayforce.presentation.colorWhite
import machine7y.grayforce.presentation.mockDeviceSettingsRepository
import machine7y.grayforce.presentation.mockSettingsRepository
import machine7y.grayforce.presentation.utils.scaleValue
import machine7y.grayforce.presentation.utils.switchGrayscale
import machine7y.grayforce.presentation.utils.toDurationString

@Composable
fun Settings(
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
    var sliderValueState by remember { mutableFloatStateOf(delayState) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(delayState) {
        sliderValueState = delayState
    }
    Column(
        modifier = modifier,
    ) {

        Text(
            text = stringResource(R.string.setup_countdown_timer),
            fontSize = 20.sp,
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
            Text(
                text = sliderValueState.toInt().scaleValue().toDurationString(context),
                fontSize = 20.sp,
            )
            Slider(
                value = sliderValueState,
                onValueChange = { sliderValueState = it },
                onValueChangeFinished = {
                    coroutineScope.launch {
                        settingsRepository.setDelay(sliderValueState)
                    }
                },
                valueRange = delayRange,
                steps = delayRange.endInclusive.toInt(),
                enabled = !grayForceState.value,
                colors = SliderDefaults.colors(
                    thumbColor = colorSilkyTurquoise,
                    activeTrackColor = colorWhite,
                    inactiveTrackColor = colorWhite,
                    inactiveTickColor = colorSilkyTurquoise,
                    activeTickColor = colorSilkyTurquoise,
                    disabledThumbColor = colorLightGray,
                    disabledActiveTrackColor = colorWhite,
                    disabledInactiveTrackColor = colorWhite,
                    disabledInactiveTickColor = colorLightGray,
                    disabledActiveTickColor = colorLightGray,
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
                        // TODO: Handle launch alarm
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colorWhite,
                    checkedTrackColor = colorSilkyTurquoise,
                    uncheckedThumbColor = colorSilkyTurquoise,
                    uncheckedTrackColor = colorWhite,
                    uncheckedBorderColor = colorSilkyTurquoise,
                    checkedBorderColor = colorSilkyTurquoise,
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
                    color = colorSilkyTurquoise,
                    shape = CircleShape,
                )
                .align(Alignment.CenterHorizontally)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_circle_arrows),
                contentDescription = null,
                tint = colorChineseCyan,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    Settings(mockDeviceSettingsRepository(), mockSettingsRepository())
}
