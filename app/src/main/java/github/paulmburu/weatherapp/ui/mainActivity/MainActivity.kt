package github.paulmburu.weatherapp.ui.mainActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import github.paulmburu.domain.models.CurrentLocationWeather
import github.paulmburu.weatherapp.R
import github.paulmburu.weatherapp.databinding.ActivityMainBinding
import github.paulmburu.weatherapp.models.WeatherForecastPresentation
import github.paulmburu.weatherapp.ui.adapters.CustomPageAdapter
import github.paulmburu.weatherapp.ui.adapters.WeatherForecastRecyclerAdapter
import github.paulmburu.weatherapp.util.convertKelvinToCelsius
import github.paulmburu.weatherapp.util.convertToTime
import github.paulmburu.weatherapp.util.hideKeyboard

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    // ViewPager items
    private var views = ArrayList<View>()
    private var pageTitles = ArrayList<String>()
    private lateinit var pagerAdapter: CustomPageAdapter

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
        setupViewpager()

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

    private fun setupViewpager() {
        pagerAdapter = CustomPageAdapter(views, pageTitles)
        val locationView: View = binding.weatherForecast
        val viewPager = locationView.findViewById<ViewPager>(R.id.pager)
        val tabLayout = locationView.findViewById<TabLayout>(R.id.tab_layout)

        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
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
                    displayWeatherForecastState(uiState.weatherForecast)
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
            searchLayout.isVisible = false
            noDataImageView.isVisible = false
        }
    }

    private fun displaySuccessState(currentLocationWeather: CurrentLocationWeather) {
        with(binding) {
            progressBar.isVisible = false
            forecastViews.isVisible = true
            noDataImageView.isVisible = false
            setCurrentLocationViews(currentLocationWeather)
        }
    }

    private fun displayWeatherForecastState(weatherForecast: List<WeatherForecastPresentation>) {

        val data = weatherForecast.distinctBy { it.dayOfTheWeek }
        data.forEach { it ->

            val layoutInflater = this.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view: View = layoutInflater.inflate(R.layout.weather_forecast, null)

            val adapter = WeatherForecastRecyclerAdapter()
            view.findViewById<RecyclerView>(R.id.data_recyclerview).adapter = adapter
            view.findViewById<RecyclerView>(R.id.data_recyclerview).addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    LinearLayoutManager.VERTICAL
                )
            )

            val dayForecast = weatherForecast.filter { day -> day.dayOfTheWeek == it.dayOfTheWeek }
            adapter.submitList(dayForecast)

            views.add(views.size, view)
            pageTitles.add(it.dayOfTheWeek)
            pagerAdapter.notifyDataSetChanged()
        }

        with(binding) {
            progressBar.isVisible = false
            forecastViews.isVisible = true
            noDataImageView.isVisible = false
        }
    }

    private fun setCurrentLocationViews(currentLocationWeather: CurrentLocationWeather) {
        val locationView: View = binding.locationLayout

        updateWeatherImage(currentLocationWeather.weatherInfo[0].main)
        locationView.findViewById<TextView>(R.id.tempTextView).text =
            currentLocationWeather.mainInfo.temp.convertKelvinToCelsius()
        locationView.findViewById<TextView>(R.id.cloudsTextView).text =
            currentLocationWeather.weatherInfo[0].description.replaceFirstChar { it.uppercaseChar() }
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

    private fun updateWeatherImage(weatherType: String) {
        val locationView: View = binding.locationLayout
        val weatherImageView = locationView.findViewById<ImageView>(R.id.weatherImageView)

        when (weatherType) {
            "Clouds" -> {
                weatherImageView.setImageDrawable(
                    ContextCompat.getDrawable(applicationContext, R.drawable.icons8_clouds_100)
                )
            }
            "Clear" -> {
                weatherImageView.setImageDrawable(
                    ContextCompat.getDrawable(applicationContext, R.drawable.icons8_sun_100)
                )
            }
            "Rain" -> {
                weatherImageView.setImageDrawable(
                    ContextCompat.getDrawable(applicationContext, R.drawable.icons8_rain_100)
                )
            }
        }
    }

    private fun displayEmptyState() {
        with(binding) {
            noDataImageView.isVisible = true
            progressBar.isVisible = false
            forecastViews.isVisible = false
        }
    }

    private fun displayFailedState() {
        with(binding) {
            noDataImageView.isVisible = true
            progressBar.isVisible = false
            forecastViews.isVisible = false
            searchLayout.isVisible = false
        }
    }

}
