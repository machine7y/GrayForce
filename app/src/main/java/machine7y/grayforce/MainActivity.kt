package machine7y.grayforce

import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }

        enableGrayscale()
    }

    fun enableGrayscale() {
        check(hasWriteSecureSettingsPermission()) {
            "WRITE_SECURE_SETTINGS is not granted"
        }

        val resolver = contentResolver

        Settings.Secure.putInt(resolver, DALTONIZER, MONOCHROMACY)
        Settings.Secure.putInt(resolver, DALTONIZER_ENABLED, 1)
    }

    fun disableGrayscale() {
        Settings.Secure.putInt(contentResolver, DALTONIZER_ENABLED, 0)
    }

    fun isGrayscaleEnabled(): Boolean {
        val enabled = Settings.Secure.getInt(contentResolver, DALTONIZER_ENABLED, 0) != 0
        val mode = Settings.Secure.getInt(contentResolver, DALTONIZER, -1)

        return enabled && mode == MONOCHROMACY
    }

    private fun hasWriteSecureSettingsPermission(): Boolean = checkSelfPermission(
        android.Manifest.permission.WRITE_SECURE_SETTINGS
    ) == android.content.pm.PackageManager.PERMISSION_GRANTED

    companion object {

        private const val DALTONIZER_ENABLED = "accessibility_display_daltonizer_enabled"
        private const val DALTONIZER = "accessibility_display_daltonizer"

        private const val MONOCHROMACY = 0
    }
}