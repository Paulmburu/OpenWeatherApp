package github.paulmburu.weatherapp.ui.mainActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import github.paulmburu.common.Resource
import github.paulmburu.domain.models.Coordinates
import github.paulmburu.domain.models.CurrentLocationWeather
import github.paulmburu.domain.usercases.*
import github.paulmburu.weatherapp.mappers.toPresentation
import github.paulmburu.weatherapp.models.WeatherForecastPresentation
import github.paulmburu.weatherapp.util.ConnectivityProvider
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val fetchLocationWeatherUseCase: FetchLocationWeatherUseCase,
    private val fetchCurrentWeatherUseCase: FetchCurrentWeatherUseCase,
    private val fetchWeatherForecastUseCase: FetchWeatherForecastUseCase,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getWeatherForecastUseCase: GetWeatherForecastUseCase,
    connectivityProvider: ConnectivityProvider,
) : ViewModel() {

    private val mutableFetchWeatherResult = MutableLiveData<FetchCurrentWeatherUiState>()
    val fetchWeatherResult: LiveData<FetchCurrentWeatherUiState>
        get() = mutableFetchWeatherResult

    private val mutableWeatherForecastResult = MutableLiveData<FetchWeatherForecastUiState>()
    val weatherForecastResult: LiveData<FetchWeatherForecastUiState>
        get() = mutableWeatherForecastResult


    private val mutableConnectivityStatus = MutableLiveData<Boolean>()
    val connectivityStatus: LiveData<Boolean>
        get() = mutableConnectivityStatus

    init {
        mutableConnectivityStatus.value = connectivityProvider.isNetworkAvailable()
    }

    fun loadNetworkData(location: String) = viewModelScope.launch {
        fetchCurrentWeatherFromInternet(location)
        fetchWeatherForecastFromInternet(location)
    }


    fun loadLocalData() = viewModelScope.launch {
        getCurrentWeatherFromDatabase()
        getWeatherForecastFromDatabase()
    }


    private suspend fun fetchCurrentWeatherFromInternet(location: String) {
        fetchLocationWeatherUseCase(location).onStart {
            mutableFetchWeatherResult.value = FetchCurrentWeatherUiState.Loading
        }.collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    if (resource.data == null) {
                        mutableFetchWeatherResult.value = FetchCurrentWeatherUiState.Empty
                    } else {
                        mutableFetchWeatherResult.value = FetchCurrentWeatherUiState.Success(
                            currentLocationWeather = resource.data!!
                        )
                    }
                }
                is Resource.Error -> {
                    mutableFetchWeatherResult.value = FetchCurrentWeatherUiState.Failure(
                        message = resource.message.toString()
                    )
                }
            }
        }
    }

    private suspend fun fetchWeatherForecastFromInternet(location: String) {
        fetchWeatherForecastUseCase(location).onStart {
            mutableWeatherForecastResult.value = FetchWeatherForecastUiState.Loading
        }.collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    if (resource.data!!.isEmpty()) {
                        mutableWeatherForecastResult.value = FetchWeatherForecastUiState.Empty
                    } else {
                        mutableWeatherForecastResult.value =
                            FetchWeatherForecastUiState.Success(resource.data!!.map { it.toPresentation() })
                    }
                }
                is Resource.Error -> {
                    mutableWeatherForecastResult.value = FetchWeatherForecastUiState.Failure(
                        message = resource.message.toString()
                    )
                }
            }

        }
    }

    private suspend fun getCurrentWeatherFromDatabase() {
        getCurrentWeatherUseCase(Unit).onStart {
            mutableFetchWeatherResult.value = FetchCurrentWeatherUiState.Loading
        }.collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    if (resource.data == null) {
                        mutableFetchWeatherResult.value = FetchCurrentWeatherUiState.Empty
                    } else {
                        mutableFetchWeatherResult.value = FetchCurrentWeatherUiState.Success(
                            currentLocationWeather = resource.data!!
                        )
                    }
                }

                is Resource.Error -> {
                    mutableFetchWeatherResult.value = FetchCurrentWeatherUiState.Failure(
                        message = resource.message.toString()
                    )
                }
            }

        }
    }

    private suspend fun getWeatherForecastFromDatabase() {
        getWeatherForecastUseCase(Unit).onStart {
            mutableWeatherForecastResult.value = FetchWeatherForecastUiState.Loading
        }.collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    if (resource.data!!.isEmpty()) {
                        mutableWeatherForecastResult.value = FetchWeatherForecastUiState.Empty
                    } else {
                        mutableWeatherForecastResult.value =
                            FetchWeatherForecastUiState.Success(resource.data!!.map { it.toPresentation() })
                    }
                }
                is Resource.Error -> {
                    mutableWeatherForecastResult.value = FetchWeatherForecastUiState.Failure(
                        message = resource.message.toString()
                    )
                }
            }

        }
    }


    sealed class FetchCurrentWeatherUiState {

        object Loading : FetchCurrentWeatherUiState()

        object Empty : FetchCurrentWeatherUiState()

        data class Failure(val message: String) : FetchCurrentWeatherUiState()

        data class Success(val currentLocationWeather: CurrentLocationWeather) :
            FetchCurrentWeatherUiState()

    }

    sealed class FetchWeatherForecastUiState {

        object Loading : FetchWeatherForecastUiState()

        object Empty : FetchWeatherForecastUiState()

        data class Failure(val message: String) : FetchWeatherForecastUiState()

        data class Success(val weatherForecast: List<WeatherForecastPresentation>) :
            FetchWeatherForecastUiState()

    }


}