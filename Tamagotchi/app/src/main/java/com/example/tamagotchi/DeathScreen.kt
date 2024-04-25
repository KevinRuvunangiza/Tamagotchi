package com.example.tamagotchi

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class DeathScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_death_screen)

        val deathScreenMusic: MediaPlayer
        deathScreenMusic = MediaPlayer.create(this,R.raw.death_sound)
        deathScreenMusic.start()


        val restartSfx:MediaPlayer
        restartSfx = MediaPlayer.create(this,R.raw.yay_sfx)

        val deathTxt = findViewById<TextView>(R.id.deathTxt)

        val  restartBtn = findViewById<Button>(R.id.restartBtn)
        val deathName = intent.getStringExtra("deathName")!!.toString()

        deathTxt.text = "REST IN PEACE "+deathName+" \nLITTLE ANGEL GONE TOO SOON"
        deathScreenMusic.isLooping = true

        restartBtn.setOnClickListener {
            restartSfx.seekTo(1000)
            restartSfx.start()
            deathScreenMusic.pause()
            deathScreenMusic.isLooping = false
            deathScreenMusic.seekTo(0)
            startActivity(Intent(this,MainActivity::class.java))
        }

    }
}