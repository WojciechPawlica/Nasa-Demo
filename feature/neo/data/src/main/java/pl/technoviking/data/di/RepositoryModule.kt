package pl.technoviking.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.technoviking.data.NeoRepository
import pl.technoviking.data.NeoRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindNeoRepository(repository: NeoRepositoryImpl): NeoRepository
}