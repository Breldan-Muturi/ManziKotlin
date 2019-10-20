package turi.manzi.manzikotlin

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

const val YOUTUBE_VIDEO_ID = "Libvk8HUxak"
const val YOUTUBE_PLAYLIST ="LPnDCTqW7zw&list=RDMMLPnDCTqW7zw&start_radio"
private const val TAG ="VideoActivity"
class VideoActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_video)
//        val layout = findViewById<ConstraintLayout>(R.id.activity_video)
        val layout = layoutInflater.inflate(R.layout.activity_video, null) as ConstraintLayout
        setContentView(layout)
//        val button1 = Button(this)
//        button1.layoutParams = ConstraintLayout.LayoutParams(600, 180)
//        button1.text = "Button Added"
//        layout.addView(button1)
        val playerView = YouTubePlayerView(this)
        playerView.layoutParams = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        layout.addView(playerView)

        playerView.initialize(getString(R.string.GOOGLE_API_KEY), this)
    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider?,
        youTubePlayer: YouTubePlayer?,
        wasRestored: Boolean
    ) {
        Log.d(TAG,".onInitializationSuccess: provider is ${provider?.javaClass}")
        Log.d(TAG,".onInitializationSuccess: youTubePlayer is ${youTubePlayer?.javaClass}")
        Toast.makeText(this,"Initialized YouTube Player successfully", Toast.LENGTH_SHORT).show()
        youTubePlayer?.setPlayerStateChangeListener(playerStateChangeListener)
        youTubePlayer?.setPlaybackEventListener(playbackEventListener)
        if(!wasRestored){
            youTubePlayer?.loadVideo(YOUTUBE_VIDEO_ID)
        } else {
            youTubePlayer?.play()
        }
    }

    override fun onInitializationFailure(
        provider: YouTubePlayer.Provider?,
        youTubeInitializationResult: YouTubeInitializationResult?
    ) {
        val REQUEST_CODE = 0
        if(youTubeInitializationResult?.isUserRecoverableError == true){
            youTubeInitializationResult.getErrorDialog(this,REQUEST_CODE).show()
        } else{
            val errorMessage = "There was an error initializing youTubePlayer"
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    private val playbackEventListener = object : YouTubePlayer.PlaybackEventListener{
        override fun onSeekTo(p0: Int) {

        }

        override fun onBuffering(p0: Boolean) {
        }

        override fun onPlaying() {
            Toast.makeText(this@VideoActivity, "Good, video is playing okay", Toast.LENGTH_LONG).show()
        }

        override fun onStopped() {
            Toast.makeText(this@VideoActivity, "Video has stopped", Toast.LENGTH_LONG).show()
        }

        override fun onPaused() {
            Toast.makeText(this@VideoActivity, "Video has paused", Toast.LENGTH_LONG).show()
        }
    }

    private val playerStateChangeListener = object : YouTubePlayer.PlayerStateChangeListener{
        override fun onAdStarted() {
            Toast.makeText(this@VideoActivity, "Click Ad now, make the video creator rich!", Toast.LENGTH_LONG).show()
        }

        override fun onLoading() {
        }

        override fun onVideoStarted() {
            Toast.makeText(this@VideoActivity, "Video has started", Toast.LENGTH_LONG).show()
            }

        override fun onLoaded(p0: String?) {
        }

        override fun onVideoEnded() {
            Toast.makeText(this@VideoActivity, "Video has been completed.", Toast.LENGTH_LONG).show()
        }

        override fun onError(p0: YouTubePlayer.ErrorReason?) {
        }
    }
}
