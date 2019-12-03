package com.kingbart.radicallydifferent

import android.content.ClipData
import android.content.ClipDescription
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.kingbart.radicallydifferent.jsonadapters.Puzzle
import com.kingbart.radicallydifferent.jsonadapters.PuzzleInstruction

class PuzzleActivity : AppCompatActivity(), View.OnTouchListener, View.OnDragListener{
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

    lateinit var a1 : View
    lateinit var a2 : View
    lateinit var a3 : View
    lateinit var a4 : View
    lateinit var d1 : View
    lateinit var d2 : View
    lateinit var d3 : View
    lateinit var d4 : View

    var jsonList:List<PuzzleInstruction> = ArrayList()

    var initialCountDown : Long = 30000
    var timeRemaining : Long = 30000
    val countDownInterval: Long = 1000

    var gameStarted = false
    var gameOverResponse = false //set to lose
    var testString : String = "" //holds val of puzzle piece

    lateinit var puzzleMap: MutableMap<String, List<String>>
    lateinit var solutionKey: MutableList<String>
    var checker = mutableListOf<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzle_main)
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

        //slots
        a1 = findViewById(R.id.acrossFHHW1)
        a2 = findViewById(R.id.acrossFHHW2)
        a3 = findViewById(R.id.acrossHHHW3)
        a4 = findViewById(R.id.acrossHHHW4)
        d1 = findViewById(R.id.downFWHH1)
        d2 = findViewById(R.id.downFWHH2)
        d3 = findViewById(R.id.downHHHW3)
        d4 = findViewById(R.id.downHHHW4)

        val jsonfile = jsonConverter()
        val gsonFile = Gson().fromJson(jsonfile, Puzzle::class.java)
        jsonList = gsonFile.puzzle_instructions
        puzzleMap = mapMaker(jsonList)

        puzzleStart.setOnClickListener{ if (!gameStarted) { startGame()} }
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

        resetVisibility()//sets slots to empty
        resetText() //empties text
        checker.clear()

        randomizer(puzzleMap)
        buttonVisibility(gameStarted)


        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeRemaining = millisUntilFinished
                val timeLeft = millisUntilFinished / 1000
                timeLeftTextView.text = getString(R.string.timer, timeLeft.toString())
            }
            override fun onFinish() {
                checkScore()
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

    private fun resetVisibility() {
        //set all views to invisible
        a1.visibility = View.INVISIBLE
        a2.visibility = View.INVISIBLE
        a3.visibility = View.INVISIBLE
        a4.visibility = View.INVISIBLE
        d1.visibility = View.INVISIBLE
        d2.visibility = View.INVISIBLE
        d3.visibility = View.INVISIBLE
        d4.visibility = View.INVISIBLE
        
        //set all radicals to visible
        radical1.visibility = View.VISIBLE
        radical2.visibility = View.VISIBLE
        radical3.visibility = View.VISIBLE
        radical4.visibility = View.VISIBLE
        radical5.visibility = View.VISIBLE
        radical6.visibility = View.VISIBLE
        radical7.visibility = View.VISIBLE
        radical8.visibility = View.VISIBLE
        radical9.visibility = View.VISIBLE
    }
    
    private fun resetText(){
        radical1.text = ""
        radical2.text = ""
        radical3.text = ""
        radical4.text = ""
        radical5.text = ""
        radical6.text = ""
        radical7.text = ""
        radical8.text = ""
        radical9.text = ""
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

            if (radical3.text == ""){ radical3.visibility = View.INVISIBLE}
            if (radical4.text == ""){ radical4.visibility = View.INVISIBLE}
            if (radical5.text == ""){ radical5.visibility = View.INVISIBLE}
            if (radical6.text == ""){ radical6.visibility = View.INVISIBLE}
            if (radical7.text == ""){ radical7.visibility = View.INVISIBLE}
            if (radical8.text == ""){ radical8.visibility = View.INVISIBLE}
            if (radical9.text == ""){ radical9.visibility = View.INVISIBLE}

            hideStart(puzzleStart)
        }
        else{
            //hides radicals if game not started
            if(radical1.visibility == View.VISIBLE){ showHide(radical1) }
            else if (radical1.visibility == View.INVISIBLE){  }
            if(radical2.visibility == View.VISIBLE){ showHide(radical2) }
            else if(radical2.visibility == View.INVISIBLE){  }
            if(radical3.visibility == View.VISIBLE){ showHide(radical3) }
            else if (radical3.visibility == View.INVISIBLE){  }
            if(radical4.visibility == View.VISIBLE){ showHide(radical4) }
            else if(radical4.visibility == View.INVISIBLE){ }
            if(radical5.visibility == View.VISIBLE){ showHide(radical5) }
            else if(radical5.visibility == View.INVISIBLE){  }
            if(radical6.visibility == View.VISIBLE){ showHide(radical6) }
            else if(radical6.visibility == View.INVISIBLE){  }
            if(radical7.visibility == View.VISIBLE){ showHide(radical7) }
            else if(radical7.visibility == View.INVISIBLE){  }
            if(radical8.visibility == View.VISIBLE){ showHide(radical8) }
            else if(radical8.visibility == View.INVISIBLE){  }
            if(radical9.visibility == View.VISIBLE){ showHide(radical9) }
            else if(radical9.visibility == View.INVISIBLE){ }
        }
    }

    private fun showHide(view: View){
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
        //all radicals were removed
        for (i in checker) {
            if(!i){
                 gameOverResponse = false
                return gameOverResponse
            }
        }
        gameOverResponse = true
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

    private fun randomizer(puzzleMap: MutableMap<String, List<String>>){
        var hanziCopy = puzzleMap.keys.toMutableList()
        var randomHanzi = hanziCopy.shuffled().take(1)[0]
        var instructionList = puzzleMap[randomHanzi]!!
        layoutAssigner(randomHanzi, instructionList)
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

    private fun layoutAssigner(randomHanzi: String, instructions: List<String>){
        var instructions = instructions.toMutableList()
        instructions.removeAll(listOf(""))
        if (instructions[0] == "a" && instructions.size == 3){
            instructions.removeAt(0) //remove all instruction values

            hanzi.text = randomHanzi

            var replacement  = mutableListOf<String>()
            for (i in instructions){
                replacement.add(radicalFlipper(i))
                checker.add(false)
            }
            solutionKey = replacement.toMutableList() //assign values before shuffling

            //make slots visible
            showHide(a1)
            showHide(a2)

            //replacement.shuffle()
            radical1.text = replacement[0]
            radical2.text = replacement[1]

            a1.setOnDragListener(this)
            a2.setOnDragListener(this)

            radical1.setOnTouchListener(this)
            radical2.setOnTouchListener(this)
        }
        else if (instructions[0] == "d" && instructions.size == 3){
            instructions.removeAt(0) //remove all instruction values

            hanzi.text = randomHanzi

            var replacement  = mutableListOf<String>()
            for (i in instructions){
                replacement.add(radicalFlipper(i))
                checker.add(false)
            }
            solutionKey = replacement.toMutableList() //assign values before shuffling

            //make slots visible
            showHide(d1)
            showHide(d2)
            
            radical1.text = replacement[0]
            radical2.text = replacement[1]

            d1.setOnDragListener(this)
            d2.setOnDragListener(this)

            radical1.setOnTouchListener(this)
            radical2.setOnTouchListener(this)
        }
        else if (instructions[0] == "a" && instructions[2] == "d" && instructions.size == 5){
            instructions.removeAt(2) //remove all instruction values in reverse order
            instructions.removeAt(0)


            hanzi.text = randomHanzi

            var replacement  = mutableListOf<String>()
            for (i in instructions){
                replacement.add(radicalFlipper(i))
                checker.add(false)
            }
            solutionKey = replacement.toMutableList() //assign values before shuffling

            //make slots visible
            showHide(a1)
            showHide(d3)
            showHide(d4)

            radical1.text = replacement[0]
            radical2.text = replacement[1]
            radical3.text = replacement[2]

            //set slots to listen for pieces
            a1.setOnDragListener(this)
            d3.setOnDragListener(this)
            d4.setOnDragListener(this)

            //allow pieces to move
            radical1.setOnTouchListener(this)
            radical2.setOnTouchListener(this)
            radical3.setOnTouchListener(this)
        }
        else if (instructions[0] == "d" && instructions[2] == "a" && instructions.size == 5){
            instructions.removeAt(2) //remove all instruction values in reverse order
            instructions.removeAt(0)


            hanzi.text = randomHanzi

            var replacement  = mutableListOf<String>()
            for (i in instructions){
                replacement.add(radicalFlipper(i))
                checker.add(false)
            }
            solutionKey = replacement.toMutableList() //assign values before shuffling

            //make slots visible
            showHide(d1)
            showHide(a3)
            showHide(a4)

            radical1.text = replacement[0]
            radical2.text = replacement[1]
            radical3.text = replacement[2]

            //set slots to listen for pieces
            d1.setOnDragListener(this)
            a3.setOnDragListener(this)
            a4.setOnDragListener(this)

            //allow pieces to move
            radical1.setOnTouchListener(this)
            radical2.setOnTouchListener(this)
            radical3.setOnTouchListener(this)
        }
        else{ //this case isn't currently covered and should therefore be reassigned
            var hanziCopy = puzzleMap.keys.toMutableList()
            var randoHanzi = hanziCopy.shuffled().take(1)[0]
            var instructionList = puzzleMap[randoHanzi]!!
            layoutAssigner(randoHanzi, instructionList)
        }
    }

    override fun onDrag(v: View, event: DragEvent): Boolean {
        when(event.action){
            //allows only the appropriate radical to go in a slot, excluding copies
            DragEvent.ACTION_DROP -> {
                if (v == a1 && testString == solutionKey[0]){
                    showHide(a1)
                    showHide(radical1)
                    testString = ""
                    checker[0] = true
                }
                else if (v == a2 && testString == solutionKey[1]){
                    showHide(a2)
                    showHide(radical2)
                    testString = ""
                    checker[1] = true
                }
                else if (v == a3 && testString == solutionKey[1]){
                    showHide(a3)
                    showHide(radical2)
                    testString = ""
                    checker[1] = true
                }
                else if (v == a4 && testString == solutionKey[2]){
                    showHide(a4)
                    showHide(radical3)
                    testString = ""
                    checker[2] = true
                }
                else if (v == d1 && testString == solutionKey[0]){
                    showHide(d1)
                    showHide(radical1)
                    testString = ""
                    checker[0] = true
                }
                else if (v == d2 && testString == solutionKey[1]){
                    showHide(d2)
                    showHide(radical2)
                    testString = ""
                    checker[1] = true
                }
                else if (v == d3 && testString == solutionKey[1]){
                    showHide(d3)
                    showHide(radical2)
                    testString = ""
                    checker[1] = true
                }
                else if (v == d4 && testString == solutionKey[2]){
                    showHide(d4)
                    showHide(radical3)
                    testString = ""
                    checker[2] = true
                }
            }
        }
        return true
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN){
            val shadowBuilder = View.DragShadowBuilder(v)
            testString = (v as TextView).text.toString()
            val item = ClipData.Item(v.getTag() as CharSequence)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val dragData = ClipData(v.getTag().toString(), mimeTypes, item)
            v.startDragAndDrop(dragData, shadowBuilder, v, 0)
            return true
        }
        return false
    }
}
