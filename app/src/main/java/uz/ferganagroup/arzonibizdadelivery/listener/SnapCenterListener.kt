package uz.ferganagroup.arzonibizdadelivery.listener
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class SnapCenterListener @JvmOverloads constructor(private val `object`: Any? = null) : RecyclerView.OnScrollListener() {

    var mLastSnappedItemPosition = -1

    interface CenterChangeListener {
        fun onCenterViewChange(view: View?, position: Int)
    }

    override fun onScrollStateChanged(rv: RecyclerView, state: Int) {
        super.onScrollStateChanged(rv, state)
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            val lm = rv.layoutManager as LinearLayoutManager?
            setScroll(rv, lm!!, lm.orientation == LinearLayoutManager.HORIZONTAL)
        }
    }

    private fun setScroll(rv: RecyclerView, lm: LinearLayoutManager, horizontal: Boolean) {
        val mid = if (horizontal) rv.width / 2 else rv.height / 2
        var minDistance = Integer.MAX_VALUE
        var resultView: View? = null
        var position = 0
        var result = 0
        for (i in lm.findFirstVisibleItemPosition()..lm.findLastVisibleItemPosition()) {
            val view = lm.findViewByPosition(i) ?: break
            val difference = getDiff(view, mid, horizontal)
            val distance = Math.abs(difference)
            if (distance < minDistance) {
                minDistance = distance
                resultView = view
                result = difference
                position = lm.getPosition(view!!)
            }
        }

        val adapter = rv.adapter
        if (adapter is CenterChangeListener) {
            (adapter as CenterChangeListener).onCenterViewChange(resultView, position)
        }

        if (`object` != null && position != mLastSnappedItemPosition) {
            (`object` as CenterChangeListener).onCenterViewChange(resultView, position)
            mLastSnappedItemPosition = position
        }

        // to compensate rounding errors
        if (position == 0 && result == -1) {
            result = 0
        }

        if (horizontal) {
            rv.smoothScrollBy(result, 0)
        } else {
            rv.smoothScrollBy(0, result)
        }
    }

    private fun getDiff(v: View?, mid: Int, horizontal: Boolean): Int {
        if (v == null) return 0
        return if (horizontal)
            v.left + (v.right - v.left) / 2 - mid
        else
            v.top + (v.bottom - v.top) / 2 - mid
    }

}