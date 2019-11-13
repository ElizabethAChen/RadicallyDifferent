package com.kingbart.radicallydifferent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class AppWelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_welcome)

        var introduction = findViewById<TextView>(R.id.intro)
        var library = findViewById<TextView>(R.id.library)
        var matching = findViewById<TextView>(R.id.matching)
        var roadTrip = findViewById<TextView>(R.id.roadmap)
        var puzzle = findViewById<TextView>(R.id.puzzle)

        introduction.setOnClickListener{
            val intent = Intent(this, AppIntroActivity::class.java)
            startActivity(intent)
        }

        library.setOnClickListener{
            val intent = Intent(this, RecyclerViewActivity::class.java)
            startActivity(intent)
        }

        matching.setOnClickListener{
            val intent = Intent(this, MatchingIntroActivity::class.java)
            startActivity(intent)
        }

        roadTrip.setOnClickListener{
            val intent = Intent(this, RoadTripIntroActivity::class.java)
            startActivity(intent)
        }

        puzzle.setOnClickListener{
            val intent = Intent(this, PuzzleIntroActivity::class.java)
            startActivity(intent)
        }
    }
}
