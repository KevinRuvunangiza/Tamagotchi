package com.example.tamagotchi

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //Creates a media player that allows us to play audios in our app
        var backgrndMusic: MediaPlayer
        backgrndMusic = MediaPlayer.create(this,R.raw.background_music)
        backgrndMusic.start()
        backgrndMusic.isLooping = true
        backgrndMusic.seekTo(2000)

        // This variables gives us access to our different UI elements like the text field for example
        val nameField = findViewById<EditText>(R.id.nameField)
        val startBtn = findViewById<Button>(R.id.startBtn)
        val startMusic : MediaPlayer
        startMusic = MediaPlayer.create(this,R.raw.startsound)

        // When the start button is pressed the app gives our pet a name and switches to the next screen
        startBtn.setOnClickListener {
            startActivity(Intent(this,MainGame::class.java).putExtra("name",nameField.text.toString()))
            if (startBtn.isPressed){
                startMusic.start()
                backgrndMusic.stop()

            }


        }

    }
}