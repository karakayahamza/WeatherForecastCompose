package com.example.weatherforecastcompose.di

import com.example.weatherforecastcompose.repository.WeatherRepository
import com.example.weatherforecastcompose.service.WeatherAPI
import com.example.weatherforecastcompose.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideWeatherRepository(
        api: WeatherAPI
    ) = WeatherRepository(api)


    @Singleton
    @Provides
    fun provideWeatherApi(): WeatherAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(WeatherAPI::class.java)
    }

}