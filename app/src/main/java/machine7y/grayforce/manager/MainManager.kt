package machine7y.grayforce.manager

interface MainManager {

    fun hasWriteSecureSettingsPermission(): Boolean

    fun enableGrayscale()

    fun disableGrayscale()

    fun isGrayscaleEnabled(): Boolean

    fun switch()
}