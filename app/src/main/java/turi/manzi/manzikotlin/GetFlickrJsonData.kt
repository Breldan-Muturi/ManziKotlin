package turi.manzi.manzikotlin

import android.os.AsyncTask

class GetFlickrJsonData(private val listener: onDataAvailable) : AsyncTask<String, Void, ArrayList<Photo>>() {
}