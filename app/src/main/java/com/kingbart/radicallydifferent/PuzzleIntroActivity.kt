package com.kingbart.radicallydifferent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import kotlinx.android.synthetic.main.activity_matching_intro.*
import kotlinx.android.synthetic.main.activity_puzzle_intro.*
import kotlinx.android.synthetic.main.activity_puzzle_intro.easy
import kotlinx.android.synthetic.main.activity_puzzle_intro.hard
import kotlinx.android.synthetic.main.activity_puzzle_intro.medium
import kotlinx.android.synthetic.main.activity_puzzle_intro.radioGroup

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

        var level = checkLevel()
        val radioGroup : RadioGroup = findViewById(R.id.radioGroup)
        puzzleStart.setOnClickListener{
            val intent = Intent(this, PuzzleActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkLevel(): Long{
        //checks the value of the radio button to send to the game
        var radioId = radioGroup.getCheckedRadioButtonId()
        var radioButton = findViewById<RadioButton>(radioId)
        var level: Long = 1000


        when (radioButton){
            easy -> level = 60000
            medium -> level = 30000
            hard -> level =  15000
        }

        return level
    }
}
