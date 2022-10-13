package github.paulmburu.weatherapp.ui.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

class CustomPageAdapter(
    private val views: List<View>,
    private val pageTitles: List<String>
) : PagerAdapter() {

    fun getView(position: Int): View {
        return views[position]
    }

    override fun getCount(): Int {
        return views.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(views[position])
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = views[position]
        container.addView(view)
        return view
    }

    override fun getItemPosition(`object`: Any): Int {
        for (index in 0 until count) {
            if (`object` as View === views[index]) {
                return index
            }
        }
        return POSITION_NONE
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when(position){
            0 -> "Today"
            1 -> "Tomorrow"
            else -> pageTitles[position]
        }
    }
}