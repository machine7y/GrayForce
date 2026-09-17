package machine7y.grayforce.manager

class MainManagerMock: MainManager {

    override fun hasWriteSecureSettingsPermission(): Boolean = true

    override fun enableGrayscale() {
    }

    override fun disableGrayscale() {
    }

    override fun isGrayscaleEnabled(): Boolean = true

    override fun switch() {
    }
}
