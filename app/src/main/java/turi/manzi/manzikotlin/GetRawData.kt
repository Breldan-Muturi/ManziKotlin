package turi.manzi.manzikotlin

import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

enum class DownloadStatus{
    OK, IDLE, NOT_INITIALISED, FAILED_OR_EMPTY, PERMISSIONS_ERROR, ERROR
}
private const val TAG = "GetRawData"

class GetRawData : AsyncTask<String, Void, String>(){
    private var downloadStatus = DownloadStatus.IDLE

    override fun onPostExecute(result: String?) {
        Log.d(TAG, "onPostExecute called: parameter is $result")
    }

    override fun doInBackground(vararg params: String?): String {
        if(params[0] == null){
            downloadStatus = DownloadStatus.NOT_INITIALISED
            return "No Url is specified"
        }
        try {
            downloadStatus = DownloadStatus.OK
            return URL(params[0]).readText()
        } catch (e: Exception){
            val errorMessage = when(e) {
                is MalformedURLException ->{
                    downloadStatus = DownloadStatus.NOT_INITIALISED
                    "doInBackground: Invalid URL ${e.message}"
                }
                is IOException ->{
                    downloadStatus = DownloadStatus.FAILED_OR_EMPTY
                    "doInBackground: IOException reading data: ${e.message}"
                }
                is SecurityException ->{
                    downloadStatus = DownloadStatus.PERMISSIONS_ERROR
                    "doInBackground: SecurityException needs permisson: ${e.message}"
                } else -> {
                    downloadStatus = DownloadStatus.ERROR
                    "Unknown error: ${e.message}"
                }

        }
            Log.e(TAG, "errorMessage")
            return errorMessage
        }
    }
    }