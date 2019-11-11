package com.kingbart.radicallydifferent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MatchingIntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matching_intro)
        var readMe = findViewById(R.id.read_me_button) as Button
        var matchingGameStart = findViewById(R.id.matchingStartButton) as Button

        readMe.setOnClickListener{
            val intent = Intent(this, MatchingReadMeActivity::class.java)
            startActivity(intent)
        }

        matchingGameStart.setOnClickListener{
            val intent = Intent(this, MatchingActivity::class.java)
            startActivity(intent)
        }
    }
}
