package com.kingbart.radicallydifferent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.kingbart.radicallydifferent.jsonadapters.Puzzle
import com.kingbart.radicallydifferent.jsonadapters.PuzzleInstruction

class PuzzleActivity : AppCompatActivity() {
    lateinit var hanzi: TextView
    lateinit var radical1: TextView
    lateinit var radical2: TextView
    lateinit var radical3: TextView
    lateinit var radical4: TextView
    lateinit var radical5: TextView
    lateinit var radical6: TextView
    lateinit var radical7: TextView
    lateinit var radical8: TextView
    lateinit var radical9: TextView
    lateinit var puzzleStart: Button
    lateinit var timeLeftTextView: TextView
    lateinit var countDownTimer: CountDownTimer

    var jsonList:List<PuzzleInstruction> = ArrayList()

    var initialCountDown : Long = 30000
    var timeRemaining : Long = 30000
    val countDownInterval: Long = 1000

    var gameStarted = false
    var gameOverResponse = false //set to lose

    lateinit var puzzleMap: MutableMap<String, List<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzle)
        hanzi = findViewById(R.id.hanzi)
        radical1 = findViewById(R.id.radical1)
        radical2 = findViewById(R.id.radical2)
        radical3 = findViewById(R.id.radical3)
        radical4 = findViewById(R.id.radical4)
        radical5 = findViewById(R.id.radical5)
        radical6 = findViewById(R.id.radical6)
        radical7 = findViewById(R.id.radical7)
        radical8 = findViewById(R.id.radical8)
        radical9 = findViewById(R.id.radical9)
        puzzleStart = findViewById(R.id.puzzleStart)
        timeLeftTextView = findViewById(R.id.timer)
        initialCountDown = intent.getLongExtra("time", 30000)

        val jsonfile = jsonConverter()
        val gsonFile = Gson().fromJson(jsonfile, Puzzle::class.java)
        jsonList = gsonFile.puzzle_instructions
        puzzleMap = mapMaker(jsonList)

        //startButton.setOnClickListener{ if (!gameStarted) { startGame()} }
        resetGame()
    }

    private fun jsonConverter():String {
        val jsonfile = applicationContext.assets.open("puzzle_instructions.json").bufferedReader().use { it.readText() }
        Log.e("response", jsonfile)

        return jsonfile
    }

    private fun mapMaker(list: List<PuzzleInstruction>):MutableMap<String, List<String>> {
        var breakdownMap = mutableMapOf<String, List<String>>()

        for (index in list.indices){
            var key = list[index].hanzi
            var instruction = list[index].instruction
            var a = list[index].a
            var b = list[index].b
            var c = list[index].c
            var d = list[index].d
            var e = list[index].e
            var f = list[index].f
            var g = list[index].g
            var h = list[index].h
            var i = list[index].i
            var j = list[index].j
            var k = list[index].k
            var l = list[index].l
            var m = list[index].m
            var n = list[index].n
            var o = list[index].o
            var p = list[index].p
            var q = list[index].q
            var r = list[index].r
            var s = list[index].s
            var t = list[index].t
            var value = listOf(instruction, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t)
            breakdownMap[key] = value
        }
        return breakdownMap
    }

    private fun resetGame(){
        gameOverResponse = false
        val initialTimeLeft = initialCountDown / 1000
        timeLeftTextView.text = getString(R.string.timer, initialTimeLeft.toString())

        var puzzleAssigner = randomizer(puzzleMap)

        buttonVisibility(gameStarted)
        puzzleController()

        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeRemaining = millisUntilFinished
                val timeLeft = millisUntilFinished / 1000
                timeLeftTextView.text = getString(R.string.timer, timeLeft.toString())
            }
            override fun onFinish() {
                //checkScore()
                endGame(gameOverResponse)
            }
        }
        gameStarted = false
    }

    private fun startGame(){
        countDownTimer.start()
        gameStarted = true
        gameOverResponse = false

        buttonVisibility(gameStarted)
    }

    private fun puzzleController(list: MutableList<String>){
        //randomly assigns puzzle piece values so each game is different
        list.shuffle()
        radical1.text = list[0]
        radical2.text = list[1]
        radical3.text = list[2]
        radical4.text = list[3]
        radical5.text = list[4]
        radical6.text = list[5]
        radical7.text = list[6]
        radical8.text = list[7]
        radical9.text = list[8]
    }

    private fun buttonVisibility(gameStarted:Boolean){
        if (gameStarted){
            showHide(radical1)
            showHide(radical2)
            showHide(radical3)
            showHide(radical4)
            showHide(radical5)
            showHide(radical6)
            showHide(radical7)
            showHide(radical8)
            showHide(radical9)
            hideStart(puzzleStart)
        }
        else{
            showHide(radical1) //hides radicals if game not started
            showHide(radical2)
            showHide(radical3)
            showHide(radical4)
            showHide(radical5)
            showHide(radical6)
            showHide(radical7)
            showHide(radical8)
            showHide(radical9)
        }
    }

    private fun showHide(view: TextView){
        view.visibility = if (view.visibility == View.VISIBLE) {
            View.INVISIBLE}
        else {
            View.VISIBLE}
    }

    private fun hideStart(view: Button){
        view.visibility = if (view.visibility == View.VISIBLE) {
            View.GONE}
        else {
            View.VISIBLE}
    }

    private fun checkScore(): Boolean {
        //if(something){ gameOverResponse = true}
        return gameOverResponse
    }
    
    private fun endGame(gameOverResponse: Boolean) {
        if (gameOverResponse) {
            Toast.makeText(this, getString(R.string.win), Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, getString(R.string.lose), Toast.LENGTH_LONG).show()
        }
        resetGame()
    }

    private fun randomizer(puzzleMap: MutableMap<String, List<String>>): MutableList<String>{
        var hanziCopy = puzzleMap.keys.toMutableList()
        var randomHanzi = hanziCopy.shuffled().take(1)[0]

        hanzi.text = randomHanzi
        var instructionList = puzzleMap[randomHanzi]!!
        for (i in instructionList){
            if (i =="a"){

            }
            //include all possible instructions

            else {//is a hanzi
                radicalFlipper(i)
                puzzleAssigner.add(i)
            }
        }


        return puzzleAssigner
    }

    private fun radicalFlipper(radical : String): String{
        var flippedRadical = ""
        when(radical){
            "人" -> flippedRadical = "亻"
            "亻" -> flippedRadical = "人"
            "刀" -> flippedRadical = "刂"
            "刂" -> flippedRadical = "刀"
            "言" -> flippedRadical = "讠"
            "讠" -> flippedRadical = "言"
            "犬" -> flippedRadical = "犭"
            "犭" -> flippedRadical = "犬"
            "糸" -> flippedRadical = "纟"
            "纟" -> flippedRadical = "糸"
            "食" -> flippedRadical = "饣"
            "饣" -> flippedRadical = "食"
            "攵" -> flippedRadical = "攴"
            "攴" -> flippedRadical = "攵"
            "心" -> flippedRadical = "忄"
            "忄" -> flippedRadical = "心"
            "衣" -> flippedRadical = "衤"
            "衤" -> flippedRadical = "衣"
            "示" -> flippedRadical = "礻"
            "礻" -> flippedRadical = "示"
            "金" -> flippedRadical = "钅"
            "钅" -> flippedRadical = "金"
            "足" -> flippedRadical = "⻊"
            "⻊" -> flippedRadical = "足"
            "火" -> flippedRadical = "灬"
            "灬" -> flippedRadical = "火"
            "水" -> flippedRadical = "氵"
            "氵" -> flippedRadical = "水"
            "氺" -> flippedRadical = "水"
            "小" -> flippedRadical = "⺌"
            "⺌" -> flippedRadical = "小"
            "⺍" -> flippedRadical = "小"
            "手" -> flippedRadical = "扌"
            "扌" -> flippedRadical = "手"
            "龵" -> flippedRadical = "手"
            "⺮" -> flippedRadical = "竹"
            "罒" -> flippedRadical = "网"
            "艹" -> flippedRadical = "艸"
            else -> flippedRadical = radical
        }
        return flippedRadical
    }
}
