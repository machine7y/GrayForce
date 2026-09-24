package machine7y.grayforce.data

interface DeviceSettingsRepository {

    fun hasWriteSecureSettingsPermission(): Boolean

    fun enableGrayscale()

    fun disableGrayscale()

    fun isGrayscaleEnabled(): Boolean

    fun switch()
}
