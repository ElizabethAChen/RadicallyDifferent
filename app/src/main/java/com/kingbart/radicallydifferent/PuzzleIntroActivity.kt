package com.kingbart.radicallydifferent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_puzzle_intro.*

class PuzzleIntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzle_intro)

        var puzzleReadMe = findViewById<Button>(R.id.puzzle_read_me_button)
        var puzzleStart = findViewById<Button>(R.id.puzzleStartButton)

        puzzleReadMe.setOnClickListener{
            val intent = Intent(this, PuzzleReadMeActivity::class.java)
            startActivity(intent)
        }
        puzzleStart.setOnClickListener{
            val intent = Intent(this, PuzzleActivity::class.java)
            startActivity(intent)
        }
    }
}
