package com.kingbart.radicallydifferent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import kotlinx.android.synthetic.main.activity_matching_intro.*

class RoadTripIntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_road_trip_intro)

        var roadTripReadMe = findViewById<Button>(R.id.read_me_button)
        var roadTripGameStart = findViewById<Button>(R.id.roadTripStartButton)
        roadTripReadMe.setOnClickListener{
            val intent = Intent(this, RoadTripReadMeActivity::class.java)
            startActivity(intent)
        }

        val radioGroup : RadioGroup = findViewById(R.id.radioGroup)
        var level = checkLevel()

        roadTripGameStart.setOnClickListener{
            level = checkLevel()
            val intent = Intent(this, RoadTripActivity::class.java)
            intent.putExtra("time", level)
            startActivity(intent)
        }
    }

    private fun checkLevel(): Long{
        //checks the value of the radio button to send to the game
        var radioId = radioGroup.getCheckedRadioButtonId()
        var radioButton = findViewById<RadioButton>(radioId)
        var level: Long = 1000


        when (radioButton){
            easy -> level = 120000
            medium -> level = 60000
            hard -> level = 30000
        }

        return level
    }
}
