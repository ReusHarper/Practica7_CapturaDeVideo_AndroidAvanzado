package dgtic.unam.grabar

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import android.view.View
import android.widget.ImageButton
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import dgtic.unam.grabar.databinding.ActivityMainBinding
import android.Manifest
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {

    private lateinit var video: VideoView
    private lateinit var button: ImageButton
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        button = binding.play

        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                100
            )
        }

        video = binding.video
        button.setOnClickListener(View.OnClickListener {
            var intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            activityResultLauncher.launch(intent)
        })

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result : ActivityResult? ->
                if(result!!.resultCode == Activity.RESULT_OK) {
                    val videoUri = result!!.data!!.data
                    video.setVideoURI(videoUri)
                    video.setMediaController(MediaController(this))
                    video.requestFocus()
                    video.start()
                }
                else {
                    Toast.makeText(
                        this,
                        "Error al capturar el video",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}