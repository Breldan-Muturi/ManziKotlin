package turi.manzi.manzikotlin

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "RecyclerItemClickListen"

class RecyclerItemClickListener(
    context: Context,
    recyclerView: RecyclerView,
    private val listener: OnRecyclerClickListener
) : RecyclerView.SimpleOnItemTouchListener() {

    interface OnRecyclerClickListener {
        fun onItemClick(view: View, position: Int)
        fun onItemLongClick(view: View, position: Int)
    }

    //    add the gesture detector
    private val gestureDetector =
        GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                Log.d(TAG, ".onSingleTapUp: starts")
                val childView = recyclerView.findChildViewUnder(e.x, e.y)
                if (childView != null) {
                    Log.d(TAG, ".onSingleTapUp calling listener .onItemClick")
                    listener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView))
                    return true
                } else {
                    return false
                }
            }

            override fun onLongPress(e: MotionEvent) {
                Log.d(TAG, ".onLongPress: starts")
                val childView = recyclerView.findChildViewUnder(e.x, e.y)
                if(childView != null){
                    Log.d(TAG, ".onLongPress calling listener .onItemClick")
                    listener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView))
                }
            }
        })

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        Log.d(TAG, ".onInterceptTouchEvent: starts $e")
        val result = gestureDetector.onTouchEvent(e)
        Log.d(TAG, ".onInterceptTouchEvent() returning: $result")
//        return super.onInterceptTouchEvent(rv, e)
        return result
    }
}