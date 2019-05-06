package com.example.bitamirshafiee.musicplayerskeleton

import android.content.pm.PackageManager
import android.database.Cursor
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_music_player.*

class MusicPlayerActivity : AppCompatActivity() {

    private var mediaplayer: MediaPlayer? = null
    private lateinit var musicList: MutableList<Music>
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: MusicAdapter
    private var currPosition: Int = 0
    private var state = false
    //false -> stopped
    //true -> play

    companion object {
        private const val REQUEST_CODE_READ_EXTERNAL_STORAGE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)

        musicList = mutableListOf()

        if (Build.VERSION.SDK_INT >= 23)
            checkPermissions()

        fab_play.setOnClickListener {

            play(currPosition)
        }
    }

    private fun play(currPosition: Int) {

        if (!state) {
            fab_play.setImageDrawable(resources.getDrawable(R.drawable.ic_stop))

            state = true

            mediaplayer = MediaPlayer().apply {

                setAudioStreamType(AudioManager.STREAM_MUSIC)
                setDataSource(this@MusicPlayerActivity, Uri.parse(musicList[currPosition].songUri))
                prepare()
                start()
            }

        } else {
            state = false
            mediaplayer?.stop()
            fab_play.setImageDrawable(resources.getDrawable(R.drawable.ic_play_arrow))
        }

    }

    private fun getSongs() {

        val selection = MediaStore.Audio.Media.IS_MUSIC
        val projection = arrayOf(
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA
        )
        val cursor : Cursor? = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection, null, null)

        while (cursor!!.moveToNext()) {

            musicList.add(Music(cursor.getString(0), cursor.getString(1), cursor.getString(2)))

        }

        cursor.close()

        linearLayoutManager = LinearLayoutManager(this)

        adapter = MusicAdapter(musicList)

        recycler_view.layoutManager = linearLayoutManager

        recycler_view.adapter = adapter

    }

    private fun checkPermissions() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            //read the songs
            getSongs()
        } else {
            //false -> user asked not to ask me any more/permission disabled
            //true -> rejected before want to use the feature again
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "Music Player needs Access to your Files", Toast.LENGTH_SHORT).show()
            }
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_READ_EXTERNAL_STORAGE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when(requestCode) {
            REQUEST_CODE_READ_EXTERNAL_STORAGE -> if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //read songs
                getSongs()
            } else {
                Toast.makeText(this, "Permission Is not Granted", Toast.LENGTH_SHORT).show()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}
