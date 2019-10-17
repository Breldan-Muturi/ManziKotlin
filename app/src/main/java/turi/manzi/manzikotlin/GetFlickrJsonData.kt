package turi.manzi.manzikotlin

import android.os.AsyncTask
import android.util.Log
import org.json.JSONObject

private const val TAG = "GetFlickrJsonData"
class GetFlickrJsonData(private val listener:  OnDataAvailable) : AsyncTask<String, Void, ArrayList<Photo>>() {

    interface OnDataAvailable{
        fun onDataAvailable(data: List<Photo>)
        fun onError(exception: Exception)
    }

    override fun doInBackground(vararg p0: String?): ArrayList<Photo> {
        Log.d(TAG,"doInBackground starts")
        try{
            val jsonData = JSONObject(p0[0])
            val itemsArray = jsonData.getJSONArray("items")

            for (i in 0 until itemsArray.length()){
                val jsonPhoto =itemsArray.getJSONObject(i)
                val title = jsonPhoto.getString("title")
                val author = jsonPhoto.getString("author")
                val authorId = jsonPhoto.getString("author_id")
                val tags = jsonPhoto.getString("tags")

                val jsonMedia = jsonPhoto.getJSONObject("media")
                val photoUrl = jsonMedia.getString("m")
                val link = photoUrl.replaceFirst("_m.jpg","_j.jpg")
                }
        }
    }

    override fun onPostExecute(result: ArrayList<Photo>?) {
        Log.d(TAG,"onPostExecute starts")
        super.onPostExecute(result)
    }
}