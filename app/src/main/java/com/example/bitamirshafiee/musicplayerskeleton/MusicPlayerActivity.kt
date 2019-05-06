package com.example.bitamirshafiee.musicplayerskeleton

import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Toast

class MusicPlayerActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_READ_EXTERNAL_STORAGE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)

        if (Build.VERSION.SDK_INT >= 23)
            checkPermissions()
    }

    private fun checkPermissions() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            //read the songs
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
            } else {
                Toast.makeText(this, "Permission Is not Granted", Toast.LENGTH_SHORT).show()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}
