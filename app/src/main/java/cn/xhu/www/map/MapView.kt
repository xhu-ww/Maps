package cn.xhu.www.map


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Xml
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.PathParser
import androidx.core.graphics.toRect
import org.xmlpull.v1.XmlPullParser
import kotlin.math.max
import kotlin.math.min

data class Province(
        val id: String,
        var name: String,
        var region: Region,
)


open class MapView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var selectedProvince: Province? = null
    private val provinces = mutableListOf<Province>()
    private val paint = Paint()

    private var svgTop = 0f
    private var svgBottom = 0f
    private var svgLeft = 0f
    private var svgRight = 0f
    private var scale = 1f

    fun showMap(svgName: String) {
        val list = context.resources.assets
                .open(svgName)
                .use {
                    val parser = Xml.newPullParser().apply {
                        setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                        setInput(it, null)
                    }

                    val list = mutableListOf<Province>()
                    while (parser.eventType != XmlPullParser.END_DOCUMENT) {
                        parser.run {
                            if (eventType == XmlPullParser.START_TAG && name == "path") {
                                val id = getAttributeValue(null, "id")
                                val title = getAttributeValue(null, "title")
                                val pathString = getAttributeValue(null, "d")

                                val region = computeRegion(pathString)
                                list.add(Province(id, title, region))
                            }
                            next()
                        }
                    }
                    list
                }
        provinces.clear()
        provinces.addAll(list)
        postInvalidate()
    }

    private fun computeRegion(pathString: String): Region {
        val path = PathParser.createPathFromPathData(pathString)
        val rectF = RectF()
        path.computeBounds(rectF, true)

        svgTop = min(svgTop, rectF.top)
        svgBottom = max(svgBottom, rectF.bottom)
        svgLeft = min(svgLeft, rectF.left)
        svgRight = max(svgRight, rectF.right)

        return Region().apply { setPath(path, Region(rectF.toRect())) }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (provinces.isEmpty()) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        } else {
            var measuredWidth = MeasureSpec.getSize(widthMeasureSpec)
            var measuredHeight = MeasureSpec.getSize(heightMeasureSpec)

            val resizeWidth = MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY
            val resizeHeight = MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY

            val svgWidth = svgRight - svgLeft
            val svgHeight = svgBottom - svgTop
            val svgAspectRatio = svgHeight / svgWidth

            if (resizeWidth && resizeHeight) {
                measuredWidth = svgWidth.toInt()
                measuredHeight = svgHeight.toInt()
            } else if (resizeWidth) {
                measuredWidth = (measuredHeight / svgAspectRatio).toInt()
            } else if (resizeHeight) {
                measuredHeight = (measuredWidth * svgAspectRatio).toInt()
            }

            val widthScale = measuredWidth / svgWidth
            val heightScale = measuredHeight / svgHeight
            scale = min(widthScale, heightScale)

            setMeasuredDimension(measuredWidth, measuredHeight)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.scale(scale, scale)

        for (province in provinces) {
            val path = province.region.boundaryPath
            paint.run {
                color = (if (selectedProvince == province) Color.CYAN else Color.GRAY)
                style = Paint.Style.FILL
                canvas.drawPath(path, this)

                strokeWidth = 0.25f
                color = Color.DKGRAY
                style = Paint.Style.STROKE
                canvas.drawPath(path, this)
            }
        }
        canvas.restore()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val relativeX = event.x / scale
            val relativeY = event.y / scale

            for (province in provinces) {
                if (province.region.contains(relativeX.toInt(), relativeY.toInt())) {
                    selectedProvince = province
                    postInvalidate()
                }
            }
            return true
        }
        return super.onTouchEvent(event)
    }
}
