package turi.manzi.manzikotlin

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_home.*

private const val TAG = "HomeActivity"

class HomeActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Create channel to Show notifications
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_channel_notification_name)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_LOW
                )
            )
        }
        subscribeButton.setOnClickListener {
            Log.d(TAG, "Subscribing to my topic")
            FirebaseMessaging.getInstance().subscribeToTopic("myTopic")
                .addOnCompleteListener { task ->
                    var msg = getString(R.string.msg_subscribed)
                    if (!task.isSuccessful) {
                        msg = getString(R.string.msg_subscribed_failed)
                    }
                    Log.d(TAG, msg)
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                }
        }

        logTokenButton.setOnClickListener {
            //Get Token
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(TAG, "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }
                    //Get new instance Id Token
                    val token = task.result?.token
                    //Log and Toast
                    val msg = getString(R.string.msg_token_fmt, token)
                    Log.d(TAG, msg)
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                })
        }
        btnPlaySingle.setOnClickListener(this)
        btnStandAlone.setOnClickListener(this)
        btnViewPhotos.setOnClickListener(this)
        btnSavePlaces.setOnClickListener(this)
        btnHackerArticles.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        val intent = when (view.id) {
            R.id.btnPlaySingle -> Intent(this, VideoActivity::class.java)
            R.id.btnStandAlone -> Intent(this, StandAloneActivity::class.java)
            R.id.btnViewPhotos -> Intent(this, MainActivity::class.java)
            R.id.btnSavePlaces -> Intent(this, PlacesActivity::class.java)
            R.id.btnHackerArticles -> Intent(this, HackerActivity::class.java)
            else -> throw IllegalArgumentException("Undefined button clicked")
        }
        startActivity(intent)
    }
}
