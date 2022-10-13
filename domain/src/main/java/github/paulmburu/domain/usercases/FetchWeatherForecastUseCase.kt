package github.paulmburu.domain.usercases

import github.paulmburu.common.Resource
import github.paulmburu.domain.models.Coordinates
import github.paulmburu.domain.models.WeatherForecast
import github.paulmburu.domain.repository.WeatherRepository
import github.paulmburu.domain.usercases.base.FlowBaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

typealias FetchWeatherForecastBaseUseCase = FlowBaseUseCase<String, Flow<Resource<List<WeatherForecast>>>>

class FetchWeatherForecastUseCase constructor(private val weatherRepository: WeatherRepository) :
    FetchWeatherForecastBaseUseCase {
    override fun invoke(location: String): Flow<Resource<List<WeatherForecast>>> = flow {
        val result = weatherRepository.fetchWeatherForecast(location)
        result.collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    val data = resource.data!!
                    emit(
                        Resource.Success(data)
                    )
                }
                is Resource.Error -> {
                    emit(Resource.Error<List<WeatherForecast>>(message = resource.message))
                }
            }
        }
    }
}