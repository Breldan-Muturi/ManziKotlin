package turi.manzi.manzikotlin

import android.os.Bundle
import android.util.Log

private const val TAG = "SearchActivity"
class SearchActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,".onCreate starts")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        activateToolbar(true)
        Log.d(TAG,".onCreate ends")
    }

}
