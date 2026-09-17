package machine7y.grayforce.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import machine7y.grayforce.manager.MainManager
import machine7y.grayforce.manager.MainManagerImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class Module {

    @Binds
    abstract fun bindMainManager(mainManagerImpl: MainManagerImpl): MainManager
}
