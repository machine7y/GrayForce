package machine7y.grayforce.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import machine7y.grayforce.R

val LocalAppColors = staticCompositionLocalOf<AppColors> {
    error("AppColors not provided")
}

private val LightColors = lightColorScheme(
    primary = Color(0xFF5A4290),
    secondary = Color(0xFF625B71),
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onSurfaceVariant = Color.LightGray,
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    secondary = Color(0xFFCCC2DC),
    background = Color(0xFF141218),
    surface = Color(0xFF141218),
    onPrimary = Color(0xFF381E72),
    onBackground = Color.White,
    onSurface = Color.White,
    onSurfaceVariant = Color.Gray,
)

val MaterialTheme.appColorScheme: AppColors
    @Composable
    get() = LocalAppColors.current

@Composable
fun Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val materialColors = if (darkTheme) {
        DarkColors
    } else {
        LightColors
    }

    val appColors = if (darkTheme) {
        darkAppColors()
    } else {
        lightAppColors()
    }

    CompositionLocalProvider(LocalAppColors provides appColors) {
        MaterialTheme(
            colorScheme = materialColors,
            content = content,
        )
    }
}

@Composable
fun lightAppColors(): AppColors = AppColors(
    primary = colorResource(R.color.silky_turquoise),
    secondary = colorResource(R.color.chinese_cyan),
    shadow = Color.Black.copy(alpha = 0.1f),
    disabled = Color.DarkGray,
)

@Composable
fun darkAppColors(): AppColors = AppColors(
    primary = colorResource(R.color.silky_turquoise),
    secondary = colorResource(R.color.chinese_cyan),
    shadow = Color.Gray.copy(alpha = 0.5f),
    disabled = Color.DarkGray,
)

data class AppColors(
    val primary: Color,
    val secondary: Color,
    val shadow: Color,
    val disabled: Color,
)
