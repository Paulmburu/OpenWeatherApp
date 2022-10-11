package github.paulmburu.weatherapp.ui.mainActivity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.AndroidEntryPoint
import github.paulmburu.domain.models.CurrentLocationWeather
import github.paulmburu.weatherapp.R
import github.paulmburu.weatherapp.databinding.ActivityMainBinding
import github.paulmburu.weatherapp.models.WeatherForecastPresentation
import github.paulmburu.weatherapp.ui.adapters.WeatherForecastRecyclerAdapter
import github.paulmburu.weatherapp.util.convertKelvinToCelsius
import github.paulmburu.weatherapp.util.convertToTime
import github.paulmburu.weatherapp.util.hideKeyboard

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: WeatherForecastRecyclerAdapter

    // AppBar items
    private lateinit var searchView: SearchView
    private lateinit var searchItem: MenuItem
    private lateinit var refreshItem: MenuItem


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setObservers()
        setupAppBar()
        setDisplayRecyclerView()
    }

    private fun setupAppBar() {
        binding.mainToolbar.apply {
            title = getString(R.string.menu_search)
            inflateMenu(R.menu.menu_search)
        }

        searchItem = binding.mainToolbar.menu.findItem(R.id.search_item)
        searchView = searchItem.actionView as SearchView
        searchView.queryHint = resources.getString(R.string.menu_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.loadNetworkData(query.toString())
                searchView.hideKeyboard()
                searchItem.collapseActionView()
                binding.mainToolbar.title = query.toString()
                return true
            }
        })

        refreshItem = binding.mainToolbar.menu.findItem(R.id.refresh_item)
        refreshItem.setOnMenuItemClickListener {
            true
        }
    }

    private fun setDisplayRecyclerView() {
        adapter = WeatherForecastRecyclerAdapter()
        binding.forecastRecyclerview.adapter = adapter
    }


    private fun setObservers() {
        viewModel.connectivityStatus.observe(this) {
            when (it) {
                false -> {
                    viewModel.loadLocalData()
                }
            }
        }

        viewModel.fetchWeatherResult.observe(this) { uiState ->
            when (uiState) {
                is MainViewModel.FetchCurrentWeatherUiState.Loading -> {
                    displayLoadingState()
                }

                is MainViewModel.FetchCurrentWeatherUiState.Failure -> {
                    displayFailedState()
                }

                is MainViewModel.FetchCurrentWeatherUiState.Success -> {
                    displaySuccessState(uiState.currentLocationWeather)
                }

                is MainViewModel.FetchCurrentWeatherUiState.Empty -> {
                    displayEmptyState()
                }
            }
        }

        viewModel.weatherForecastResult.observe(this) { uiState ->
            when (uiState) {
                is MainViewModel.FetchWeatherForecastUiState.Loading -> {
                    displayLoadingState()
                }

                is MainViewModel.FetchWeatherForecastUiState.Failure -> {
                    displayEmptyState()
                }

                is MainViewModel.FetchWeatherForecastUiState.Success -> {
                    val data = uiState.weatherForecast.distinctBy { it.dayOfTheWeek }
                    displayWeatherForecastState(data)
                }

                is MainViewModel.FetchWeatherForecastUiState.Empty -> {
                    displayEmptyState()
                }
            }
        }

    }

    private fun displayLoadingState() {
        with(binding) {
            progressBar.isVisible = true
            forecastViews.isVisible = false
            noDataImageView.isVisible = false
        }
    }

    private fun displaySuccessState(currentLocationWeather: CurrentLocationWeather) {
        with(binding) {

            progressBar.isVisible = false
            forecastViews.isVisible = true
            noDataImageView.isVisible = false
            failedTextView.isVisible = false
            setCurrentLocationViews(currentLocationWeather)
            tempTextView.text = currentLocationWeather.mainInfo.temp.convertKelvinToCelsius()
            tempMinTextView.text = currentLocationWeather.mainInfo.temp_min.convertKelvinToCelsius()
            currentTempTextView.text = currentLocationWeather.mainInfo.temp.convertKelvinToCelsius()
            tempMaxTextView.text = currentLocationWeather.mainInfo.temp_max.convertKelvinToCelsius()
            weatherTypeTextView.text = currentLocationWeather.weatherInfo[0].main

        }
    }

    private fun displayWeatherForecastState(weatherForecast: List<WeatherForecastPresentation>) {
        adapter.submitList(weatherForecast)
        with(binding) {
            progressBar.isVisible = false
            forecastViews.isVisible = true
            noDataImageView.isVisible = false
            failedTextView.isVisible = false
        }
    }

    private fun setCurrentLocationViews(currentLocationWeather: CurrentLocationWeather) {
        val locationView: View = binding.locationLayout

        locationView.findViewById<TextView>(R.id.tempTextView).text =
            "${currentLocationWeather.mainInfo.temp.convertKelvinToCelsius()} c"
        locationView.findViewById<TextView>(R.id.cloudsTextView).text =
            currentLocationWeather.weatherInfo[0].description
        locationView.findViewById<TextView>(R.id.windSpeedTextView).text =
            "${currentLocationWeather.wind.speed} m/s"
        locationView.findViewById<TextView>(R.id.pressureAmountTextView).text =
            "${currentLocationWeather.mainInfo.pressure} Pa"
        locationView.findViewById<TextView>(R.id.humidityAmountTextView).text =
            currentLocationWeather.mainInfo.humidity.toString()
        locationView.findViewById<TextView>(R.id.sunriseTimeStampTextView).text =
            currentLocationWeather.sys.sunrise.convertToTime()
        locationView.findViewById<TextView>(R.id.sunsetTimestampTextView).text =
            currentLocationWeather.sys.sunset.convertToTime()
        locationView.findViewById<TextView>(R.id.lastUpdateTextView).text =
            currentLocationWeather.timeStamp.toLong().convertToTime()
    }

    private fun displayEmptyState() {
        adapter.submitList(null)

        with(binding) {
            noDataImageView.isVisible = true
            failedTextView.isVisible = false
            progressBar.isVisible = false
            forecastViews.isVisible = false
        }
    }

    private fun displayFailedState() {

        with(binding) {
            noDataImageView.setImageDrawable(
                ContextCompat.getDrawable(applicationContext, R.drawable.failure_image)
            )
            failedTextView.isVisible = true
            noDataImageView.isVisible = true
            progressBar.isVisible = false
            forecastViews.isVisible = false
        }
    }

}
