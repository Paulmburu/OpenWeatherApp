package github.paulmburu.weatherapp.util


import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import github.paulmburu.weatherapp.R

@BindingAdapter("imageDrawable")
fun bindImage(imgView: ImageView, weatherType: String?) {
    when {
        weatherType.equals("Clouds") -> imgView.setImageDrawable(
            ResourcesCompat.getDrawable(imgView.resources, R.drawable.icons8_clouds_100, null)
        )
        weatherType.equals("Clear") -> imgView.setImageDrawable(
            ResourcesCompat.getDrawable(imgView.resources, R.drawable.icons8_sun_100, null)
        )
        weatherType.equals("Rain") -> imgView.setImageDrawable(
            ResourcesCompat.getDrawable(imgView.resources, R.drawable.icons8_rain_100, null)
        )
    }
}