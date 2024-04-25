package com.example.tamagotchi

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import kotlinx.coroutines.*

class MainGame : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_game)

        // Here we have the decrease time it's the delay before hunger,happiness or clean(hygiene) goes down
        val happyDecreaseTime: Long = 1000 // 1 seconds
        val hungerDecreaseTime: Long = 4000 // 4 seconds
        val cleanDecreaseTime: Long = 2500

        // These are the variables responsible of the pet needs
        var happiness = 100
        var hunger = 100
        var clean = 100
        var isStarving = false
        var isSad = false
        var isDead:Boolean

        // This part takes care of our game background music
        val backgrndMusic : MediaPlayer
        backgrndMusic = MediaPlayer.create(this,R.raw.background_music)
        backgrndMusic.setVolume(20f,20f)
        backgrndMusic.isLooping=true
        val showerSound:MediaPlayer
        showerSound = MediaPlayer.create(this,R.raw.shower_sound)
        val eatingSound:MediaPlayer
        eatingSound = MediaPlayer.create(this,R.raw.eating_sound)
        val playingSound:MediaPlayer
        playingSound = MediaPlayer.create(this,R.raw.bird_singing)


        val petName = intent.getStringExtra("name")!!.toString()
        val deathName = petName

        /* This section is responsible of giving us access
        to all of the elements used in our [activity_main_game] layout
         */
        val happinessTxt = findViewById<TextView>(R.id.happyValue)
        happinessTxt.text = happiness.toString()
        val hungerTxt = findViewById<TextView>(R.id.hungerValue)
        hungerTxt.text = hunger.toString()
        val cleanTxt = findViewById<TextView>(R.id.cleanValue)
        cleanTxt.text = clean.toString()
        val playBtn = findViewById<Button>(R.id.playBtn)
        val backgrnd = findViewById<ImageView>(R.id.background)
        val cleanBtn = findViewById<Button>(R.id.cleanBtn)
        val feedBtn = findViewById<Button>(R.id.feedBtn)
        val mainPet = findViewById<ImageView>(R.id.mainPet)
        val needTxt = findViewById<TextView>(R.id.needTxt)
        val petNameTxt = findViewById<TextView>(R.id.petName)
        petNameTxt.text = petName


        // When called this function decreases the pet's happiness
        fun decreaseHappiness() {
            GlobalScope.launch {
                delay(happyDecreaseTime)
                if (happiness > 0) {
                    happiness -= 1

                    if(happiness==1){
                        happiness =0
                    }

                    withContext(Dispatchers.Main) {
                        happinessTxt.text = happiness.toString()

                    }
                    decreaseHappiness()
                }
            }
        }

        // When called this function decreases the pet's hygiene
        fun decreaseClean() {
            GlobalScope.launch {
                delay(cleanDecreaseTime)
                if (clean > 0) {
                    clean -= 1
                    withContext(Dispatchers.Main) {
                        cleanTxt.text = clean.toString()
                    }
                    decreaseClean()
                }

            }

        }

        // When called this function decreases the pet's hunger
        fun decreaseHunger() {
            GlobalScope.launch {
                delay(hungerDecreaseTime)
                if (hunger > 0) {
                    hunger -= 3

                    if(hunger==1){
                        hunger =0
                    }


                    withContext(Dispatchers.Main) {
                        hungerTxt.text = hunger.toString()
                        backgrndMusic.start()
                    }
                    decreaseHunger()
                }


            }

            // When called this function increases the pet hunger(feeds the pet)
            fun feedPet() {

                hunger += 3
                mainPet.isVisible = true
                backgrnd.setImageResource((R.drawable.background))
                mainPet.setImageResource(R.drawable.food_pet)
                needTxt.text = "THANK YOU *EATING*"
                hungerTxt.text = hunger.toString()
                eatingSound.start()

                if (showerSound.isPlaying==true||playingSound.isPlaying==true){
                    showerSound.pause()
                    showerSound.seekTo(0)
                    playingSound.pause()
                    playingSound.seekTo(0)
                }


            }

            // When called this function increases the pet happiness(the pet sing)
            fun playPet() {
                happiness += 10
                mainPet.isVisible = true
                backgrnd.setImageResource((R.drawable.background))
                mainPet.setImageResource(R.drawable.happy)
                needTxt.text = "Let me sing something for you *SINGING*"
                playingSound.start()
                happinessTxt.text = happiness.toString()

                if (showerSound.isPlaying==true||eatingSound.isPlaying==true){
                    showerSound.pause()
                    showerSound.seekTo(0)
                    eatingSound.pause()
                    eatingSound.seekTo(0)
                }

            }



            // When called this function increases the pet hygiene(cleans the pet)
            fun cleanPet() {
                clean += 10
                mainPet.isVisible = false
                backgrnd.setImageResource((R.drawable.bathroom_background))
                needTxt.text = "Are YOU WATCHING ME *SHOWERING*"
                cleanTxt.text = clean.toString()
                showerSound.start()

                if (eatingSound.isPlaying==true||playingSound.isPlaying==true){
                    eatingSound.pause()
                    eatingSound.seekTo(0)
                    playingSound.pause()
                    playingSound.seekTo(0)
                }

            }

            playBtn.setOnClickListener {

                if (happiness<100){
                    playPet()
                }

            }

            feedBtn.setOnClickListener {

                if (hunger<100){
                    feedPet()

                }

            }


            cleanBtn.setOnClickListener {

                if (clean<100){
                    cleanPet()
                }
            }

            //This section handles the different pets states based on the [hunger] and [happiness] variables declared earlier
            if (happiness < 25 || hunger < 25) {
                needTxt.text = "WHY ARE YOU IGNORING ME !!!"
                mainPet.setImageResource(R.drawable.crying_pet)
            } else if (happiness < 50 || hunger < 50) {
                needTxt.text = "I'm feeling a bit off."
                mainPet.setImageResource(R.drawable.sad_pet)
            } else if (happiness < 75 || hunger < 75) {
                needTxt.text = "I'm good,but you should probably \ntake care of me"
                mainPet.setImageResource(R.drawable.welcome_pet)
            } else {
                needTxt.text = "I'm happy and well!"
            }

            // All this section checks if the pet is dead
            if (hunger==0){
                isStarving = true
            }

            if (happiness==0){
                isSad = true
            }

            if (isSad==true&&isStarving==true){
                isDead = true
            }else  {
                isDead = false
            }

            if (isDead==true){
                backgrndMusic.pause()
                backgrndMusic.seekTo(0)
                startActivity(Intent(this,DeathScreen::class.java).putExtra("deathName",deathName.toString()))
            }






        }


        decreaseClean()
        decreaseHappiness()
        decreaseHunger()

    }

}

