package machine7y.grayforce.manager

import android.Manifest
import android.content.Context
import android.provider.Settings
import androidx.core.content.PermissionChecker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainManagerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
): MainManager {

    private val contentResolver
        get() = context.contentResolver

    override fun hasWriteSecureSettingsPermission(): Boolean = PermissionChecker.checkSelfPermission(
        /* context = */ context,
        /* permission = */ Manifest.permission.WRITE_SECURE_SETTINGS,
    ) == PermissionChecker.PERMISSION_GRANTED

    override fun enableGrayscale() {
        val resolver = contentResolver

        Settings.Secure.putInt(resolver, DALTONIZER, MONOCHROMACY)
        Settings.Secure.putInt(resolver, DALTONIZER_ENABLED, 1)
    }

    override fun disableGrayscale() {
        Settings.Secure.putInt(contentResolver, DALTONIZER_ENABLED, 0)
    }

    override fun isGrayscaleEnabled(): Boolean {
        val isEnabled = Settings.Secure.getInt(contentResolver, DALTONIZER_ENABLED, 0) != 0
        val mode = Settings.Secure.getInt(contentResolver, DALTONIZER, -1)

        return isEnabled && mode == MONOCHROMACY
    }

    override fun switch() {
        if (isGrayscaleEnabled()) {
            disableGrayscale()
        } else {
            enableGrayscale()
        }
    }

    companion object {

        private const val DALTONIZER_ENABLED = "accessibility_display_daltonizer_enabled"
        private const val DALTONIZER = "accessibility_display_daltonizer"

        private const val MONOCHROMACY = 0
    }
}