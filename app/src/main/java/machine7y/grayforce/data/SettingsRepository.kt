package machine7y.grayforce.data

interface SettingsRepository {

    fun hasWriteSecureSettingsPermission(): Boolean

    fun enableGrayscale()

    fun disableGrayscale()

    fun isGrayscaleEnabled(): Boolean

    fun switch()
}