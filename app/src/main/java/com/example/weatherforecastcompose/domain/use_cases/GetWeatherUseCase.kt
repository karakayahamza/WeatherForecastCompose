package com.example.weatherforecastcompose.domain.use_cases

import com.example.weatherforecastcompose.data.remote.dto.toWeatherList
import com.example.weatherforecastcompose.domain.model.WeatherModel
import com.example.weatherforecastcompose.domain.repository.WeatherRepository
import com.example.weatherforecastcompose.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOError
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(private val repository: WeatherRepository) {
    fun executeGetWeatherDetail(placeName: String): Flow<Resource<WeatherModel>> = flow {
        try {
            emit(Resource.Loading())
            val weather = repository.getWeatherDetails(placeName)
            if (weather.message == 0) {
                emit(Resource.Success(weather.toWeatherList()))
            } else {
                emit(Resource.Error(weather.message.toString()))
            }
        } catch (e: IOError) {
            emit(Resource.Error(message = "Internet bağlantısı bulunamadı"))
        } catch (e: HttpException) {
            emit(Resource.Error(message = e.localizedMessage ?: " Error."))
        }
    }
}