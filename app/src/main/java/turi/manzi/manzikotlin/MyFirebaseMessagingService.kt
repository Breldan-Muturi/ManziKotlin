package turi.manzi.manzikotlin

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

private const val TAG = "MyFirebaseMsgService"
class MyFirebaseMessagingService : FirebaseMessagingService(){
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage?.from}")

        //Check whether message has data payload
        remoteMessage?.data?.isNotEmpty().let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
        }
        //Check if message has a notification payload
        remoteMessage?.notification?.let {
            Log.d(TAG, "Message notification body: ${it.body}")
            sendNotification(it.body.toString())
        }
    }

    private fun sendNotification(messageBody: String){
        val intent =  Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent =  PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val channelId = getString(R.string.default_notification_channel_id)
        val notificationBuilder = NotificationCompat.Builder(this,channelId)
            .setSmallIcon(R.drawable.ic_stat_ic_notification)
            .setContentTitle(getString(R.string.fcm_message))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //Since android Oreo notification channel is needed
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(channelId,1,notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed Token: $token")
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String){
        //TODO: Implement this method to send token to your app server
    }

}