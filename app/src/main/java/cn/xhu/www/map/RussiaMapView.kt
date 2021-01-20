package cn.xhu.www.map


import android.content.Context
import android.util.AttributeSet

class RussiaMapView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MapView(context, attrs, defStyleAttr) {
    init {
        showMap("russia.svg")
    }
}
