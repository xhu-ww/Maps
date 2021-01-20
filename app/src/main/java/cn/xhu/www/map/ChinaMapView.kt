package cn.xhu.www.map


import android.content.Context
import android.util.AttributeSet

class ChinaMapView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MapView(context, attrs, defStyleAttr) {
    init {
        showMap("china.svg")
    }
}
