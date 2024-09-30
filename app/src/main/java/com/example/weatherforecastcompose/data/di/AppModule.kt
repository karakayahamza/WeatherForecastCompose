package com.example.weatherforecastcompose.data.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.weatherforecastcompose.data.local.SharedPreferencesDataSource
import com.example.weatherforecastcompose.data.remote.WeatherAPI
import com.example.weatherforecastcompose.data.repository.CityRepositoryImpl
import com.example.weatherforecastcompose.data.repository.PreferencesRepositoryImpl
import com.example.weatherforecastcompose.data.repository.WeatherRepositoryImpl
import com.example.weatherforecastcompose.domain.repository.CityRepository
import com.example.weatherforecastcompose.domain.repository.PreferencesRepository
import com.example.weatherforecastcompose.domain.repository.WeatherRepository
import com.example.weatherforecastcompose.util.Constants.BASE_URL
import com.example.weatherforecastcompose.util.JsonParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideWeatherRepository(api: WeatherAPI): WeatherRepository {
        return WeatherRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideCryptoApi(): WeatherAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(WeatherAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    // Provide SharedPreferencesDataSource
    @Provides
    @Singleton
    fun provideSharedPreferencesDataSource(sharedPreferences: SharedPreferences): SharedPreferencesDataSource {
        return SharedPreferencesDataSource(sharedPreferences)
    }

    // Provide PreferencesRepository
    @Provides
    @Singleton
    fun providePreferencesRepository(dataSource: SharedPreferencesDataSource): PreferencesRepository {
        return PreferencesRepositoryImpl(dataSource)
    }


    @Provides
    fun provideJsonParser(@ApplicationContext context: Context): JsonParser {
        return JsonParser(context)
    }

    @Provides
    fun provideCityRepository(jsonParser: JsonParser): CityRepository {
        return CityRepositoryImpl(jsonParser)
    }

}