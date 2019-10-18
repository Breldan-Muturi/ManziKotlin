package turi.manzi.manzikotlin

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "RecyclerItemClickListen"
class RecyclerItemClickListener (context: Context, recyclerView: RecyclerView, private val listener: OnRecyclerClickListener)
    :RecyclerView.SimpleOnItemTouchListener(){

    interface OnRecyclerClickListener{
        fun onItemClick(view: View, position: Int)
        fun onItemLongClick(view: View, position: Int)
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        Log.d(TAG, ".onInterceptTouchEvent: starts $e")
//        return super.onInterceptTouchEvent(rv, e)
        return true
    }
}