package com.rekoj134.readaudiofilefromdevicesdemo

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rekoj134.readaudiofilefromdevicesdemo.databinding.ActivityMainBinding
import com.rekoj134.readaudiofilefromdevicesdemo.util.MediaUtil
import com.rekoj134.readaudiofilefromdevicesdemo.util.PermissionUtil

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                initData()
            } else {
                Toast.makeText(this@MainActivity, "Please give us permission", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
    }

    private fun initData() {
        if (PermissionUtil.checkReadAudioPermission(this@MainActivity)) {
            val allAudio = MediaUtil.getAllAudio(this@MainActivity)
            var audiosName = ""
            allAudio.forEach {
                audiosName += "$it/n"
            }
            binding.tvAudiosName.text = audiosName
        } else requestPermission()
    }

    private fun requestPermission() {
        if (PermissionUtil.shouldShowRequestPermissionRationaleForReadAudio(
            this@MainActivity)) {
            // start intent go to setting to give permission
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)
            } else {
                requestPermissionLauncher.launch(
                    android.Manifest.permission.READ_MEDIA_AUDIO)
            }
        }
    }
}