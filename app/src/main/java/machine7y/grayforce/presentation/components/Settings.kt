package machine7y.grayforce.presentation.components

import android.content.ComponentName
import android.service.quicksettings.TileService
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import machine7y.grayforce.MainTileService
import machine7y.grayforce.R
import machine7y.grayforce.data.SettingsRepository
import machine7y.grayforce.presentation.colorChineseCyan
import machine7y.grayforce.presentation.colorLightGray
import machine7y.grayforce.presentation.colorSilkyTurquoise
import machine7y.grayforce.presentation.colorWhite
import machine7y.grayforce.presentation.mockManager
import machine7y.grayforce.presentation.utils.scaleValue
import machine7y.grayforce.presentation.utils.toDurationString

private const val DEFAULT_VALUE = 60f

private val sliderRange = 1f..595f

@Composable
fun Settings(
    settingsRepository: SettingsRepository,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Column(
        modifier = modifier,
    ) {
        var sliderValueState by remember { mutableFloatStateOf(DEFAULT_VALUE) }
        var switcherState by remember { mutableStateOf(false) }

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
            modifier = modifier
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
                valueRange = sliderRange,
                steps = sliderRange.endInclusive.toInt(),
                enabled = !switcherState,
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
                checked = switcherState,
                onCheckedChange = { switcherState = it },
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
                settingsRepository.switch()
                TileService.requestListeningState(context, ComponentName(context, MainTileService::class.java))
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
    Settings(mockManager())
}
