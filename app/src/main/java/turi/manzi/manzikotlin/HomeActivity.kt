package turi.manzi.manzikotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(),View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        btnPlaySingle.setOnClickListener(this)
        btnStandAlone.setOnClickListener(this)
        btnViewPhotos.setOnClickListener(this)
        btnSavePlaces.setOnClickListener(this)
        btnHackerArticles.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        val intent = when(view.id){
            R.id.btnPlaySingle -> Intent(this, VideoActivity::class.java)
            R.id.btnStandAlone -> Intent(this,StandAloneActivity::class.java)
            R.id.btnViewPhotos -> Intent(this, MainActivity::class.java)
            R.id.btnSavePlaces -> Intent(this, PlacesActivity::class.java)
            R.id.btnHackerArticles -> Intent(this, HackerActivity::class.java)
            else -> throw IllegalArgumentException("Undefined button clicked")
        }
        startActivity(intent)
    }
}
