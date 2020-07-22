package org.elvor.dogs.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import org.elvor.dogs.backend.DogsService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class MainModule {

    @Provides
    @Singleton
    fun provideRetrofitService() : Retrofit {

        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/")
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideDogsService(retrofit: Retrofit) : DogsService {
        return retrofit.create(DogsService::class.java)
    }
}