package turi.manzi.manzikotlin

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.song_ticket.view.*

private const val TAG = "HomeActivity"

class HomeActivity : AppCompatActivity() {

    var listSongs = ArrayList<SongInfo>()
    var adapter: MySongAdapter? = null
    var mp: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        LoadURLOnline()
        checkUserPermissions()
        var myTracking = mySongTrack()
        myTracking.start()
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
        if (item.itemId == R.id.subscribeButton) {
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
        if (item.itemId == R.id.logTokenButton) {
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

    fun LoadURLOnline() {
        listSongs.add(
            SongInfo(
                "Android Tycoons",
                "Young Koiyo",
                "https://soundcloud.com/search?q=bentley%20migos"
            )
        )
        listSongs.add(
            SongInfo(
                "I Know",
                "Young Koiyo",
                "https://soundcloud.com/search?q=i%20know%20dallask&query_urn=soundcloud%3Asearch-autocomplete%3A3a5bfeab3f5047b6a9723df1701921a0"
            )
        )
        listSongs.add(
            SongInfo(
                "Never Forget You",
                "Young Koiyo",
                "https://soundcloud.com/search?q=never%20forget%20you%20zara%20larsson&query_urn=soundcloud%3Asearch-autocomplete%3Aa665a47292914da78ce064541f9e06cd"
            )
        )
        listSongs.add(
            SongInfo(
                "Needed Me",
                "Nuurl",
                "https://server6.mp3quran.net/thubti/001.mp3"
            )
        )
        listSongs.add(
            SongInfo(
                "4am in calabasas",
                "Drake",
                "https://soundcloud.com/search?q=4am%20in%20calabasas&query_urn=soundcloud%3Asearch-autocomplete%3A6d789d4e3cd94bc3b8ad8ef7137f6420"
            )
        )
        listSongs.add(
            SongInfo(
                "Android Tycoons",
                "Young Koiyo",
                "https://soundcloud.com/search?q=jay%20z"
            )
        )
        listSongs.add(
            SongInfo(
                "20loaded",
                "lil uzi",
                "https://soundcloud.com/search?q=lil%20uzi%20loaded"
            )
        )
    }

    inner class MySongAdapter : BaseAdapter {

        var myListSong = ArrayList<SongInfo>()

        constructor(myListSong: ArrayList<SongInfo>) : super() {
            this.myListSong = myListSong
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val myView = layoutInflater.inflate(R.layout.song_ticket, null)
            val Song = this.myListSong[position]
            myView.songName.text = Song.Title
            myView.authorId.text = Song.Artist

            myView.buttonPlay.setOnClickListener(View.OnClickListener {
                //TODO:Play song
                if (myView.buttonPlay.text.equals("Stop")) {
                    mp!!.stop()
                    myView.buttonPlay.text = "Play"
                } else {
                    mp = MediaPlayer()
                    try {
                        mp!!.setDataSource(Song.SongURL)
                        mp!!.prepare()
                        mp!!.start()
                        myView.buttonPlay.text = "Stop"
                        sbProgress.max = mp!!.duration
                    } catch (ex: Exception) {
                    }
                }
            })

            return myView
        }

        override fun getItem(position: Int): Any {
            return this.myListSong[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return this.myListSong.size
        }
    }

    inner class mySongTrack() : Thread() {

        override fun run() {
            while (true) {
                try {
                    Thread.sleep(1000)
                } catch (ex: java.lang.Exception) {
                }

                runOnUiThread {
                    if (mp != null) {
                        sbProgress.progress = mp!!.currentPosition
                    }
                }
            }
        }
    }

    fun checkUserPermissions(){
        if(Build.VERSION.SDK_INT >= 23){
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_ASK_PERMISSIONS)
                return
            }
        }

        LoadSng()
    }
    //get access to location Permissions
    private val REQUEST_CODE_ASK_PERMISSIONS = 123

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            REQUEST_CODE_ASK_PERMISSIONS -> if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                LoadSng()
            } else {
                //Permission Denied
                Toast.makeText(this,"Permission Denied", Toast.LENGTH_SHORT).show()
            }

            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    fun LoadSng(){
        val allSongsURI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"
        val cursor = contentResolver.query(allSongsURI,null,selection,null,null)

        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    val SongURL= cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val songArtist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val songName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                    listSongs.add(SongInfo(songName, songArtist, SongURL))
                } while (cursor.moveToNext())
            }

            cursor.close()
            adapter = MySongAdapter(listSongs)
            lsListSongs.adapter = adapter
        }
    }
}
