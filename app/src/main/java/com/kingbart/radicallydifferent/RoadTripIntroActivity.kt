package com.kingbart.radicallydifferent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class RoadTripIntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_road_trip_intro)

        var roadTripReadMe = findViewById(R.id.read_me_button) as Button
        var roadTripGameStart = findViewById(R.id.roadTripStartButton) as Button
        roadTripReadMe.setOnClickListener{
            val intent = Intent(this, RoadTripReadMeActivity::class.java)
            startActivity(intent)
        }

        roadTripGameStart.setOnClickListener{
            val intent = Intent(this, RoadTripActivity::class.java)
            startActivity(intent)
        }
    }
}
