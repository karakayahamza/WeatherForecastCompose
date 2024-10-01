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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
abstract class AppModule {


    companion object {

        @Provides
        @Singleton
        fun provideApplicationContext(@ApplicationContext context: Context): Context {
            return context
        }

        @Provides
        @Singleton
        fun provideWeatherRepository(api: WeatherAPI): WeatherRepository {
            return WeatherRepositoryImpl(api)
        }

        @Provides
        @Singleton
        fun provideWeatherApi(): WeatherAPI {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL) // Make sure BASE_URL is defined correctly
                .build()
                .create(WeatherAPI::class.java)
        }

        @Provides
        @Singleton
        fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
            return PreferenceManager.getDefaultSharedPreferences(context)
        }

        @Provides
        @Singleton
        fun provideSharedPreferencesDataSource(sharedPreferences: SharedPreferences): SharedPreferencesDataSource {
            return SharedPreferencesDataSource(sharedPreferences)
        }

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
}
