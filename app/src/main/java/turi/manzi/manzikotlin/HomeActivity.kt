package turi.manzi.manzikotlin

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging

private const val TAG = "HomeActivity"

class HomeActivity : AppCompatActivity() {

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

        //Handle possible data accompanying notification message.
//        intent.extras?.let{
//            for(key in it.keySet()){
//                val value = intent.extras.get(key)
//                Log.d(TAG,"Key: $key Value: $value")
//            }
//        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        if (item.itemId == R.id.disconnectButton) {
            //Log the user out!
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, CreateAccountActivity::class.java))
            finish()
        }

        if (item.itemId == R.id.btnPlaySingle) {
            //take user to settingsActivity
            startActivity(Intent(this, VideoActivity::class.java))

        }
        if (item.itemId == R.id.btnStandAlone) {
            //take user to settingsActivity
            startActivity(Intent(this, StandAloneActivity::class.java))

        }
        if (item.itemId == R.id.btnViewPhotos) {
            //take user to settingsActivity
            startActivity(Intent(this, MainActivity::class.java))

        }
        if (item.itemId == R.id.btnSavePlaces) {
            //take user to settingsActivity
            startActivity(Intent(this, PlacesActivity::class.java))

        }
        if (item.itemId == R.id.btnHackerArticles) {
            //take user to settingsActivity
            startActivity(Intent(this, HackerActivity::class.java))

        }
        if (item.itemId == R.id.logTokenButton) {
            //take user to settingsActivity
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
        if (item.itemId == R.id.btnHackerArticles) {
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

        return true
    }

}
