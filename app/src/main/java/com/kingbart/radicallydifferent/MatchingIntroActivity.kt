package com.kingbart.radicallydifferent

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import kotlinx.android.synthetic.main.activity_matching_intro.*

class MatchingIntroActivity : AppCompatActivity() {

    //lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matching_intro)
        val matchingReadMe = findViewById<Button>(R.id.read_me_button)
        val matchingGameStart = findViewById<Button>(R.id.matchingStartButton)

        matchingReadMe.setOnClickListener{
            val intent = Intent(this, MatchingReadMeActivity::class.java)
            startActivity(intent)
        }

        var level= checkLevel()
        val radioGroup : RadioGroup= findViewById(R.id.radioGroup)

        //currently doesn't work because I don't know how to utilize the sharedprefs
        //sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)

        //var level = sharedPreferences.getLong("LEVEL", 0)

        matchingGameStart.setOnClickListener{
            level = checkLevel()
            val intent = Intent(this, MatchingActivity::class.java)
            intent.putExtra("time", level)
            startActivity(intent)

            //var editor : SharedPreferences.Editor = sharedPreferences.edit()
            //editor.putLong("LEVEL", level)
            //editor.apply()
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