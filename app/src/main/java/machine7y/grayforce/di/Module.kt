package machine7y.grayforce.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import machine7y.grayforce.data.SettingsRepository
import machine7y.grayforce.data.SettingsRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class Module {

    @Binds
    abstract fun bindSettingsRepository(settingsRepositoryImpl: SettingsRepositoryImpl): SettingsRepository
}
