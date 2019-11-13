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
import com.kingbart.radicallydifferent.jsonadapters.CharacterEntry
import com.kingbart.radicallydifferent.jsonadapters.Chinese
import kotlin.collections.mutableListOf

class MatchingActivity : AppCompatActivity() {

    lateinit var char1Button: Button
    lateinit var char2Button: Button
    lateinit var char3Button: Button
    lateinit var char4Button: Button
    lateinit var char5Button: Button
    lateinit var char6Button: Button
    lateinit var char7Button: Button
    lateinit var char8Button: Button
    lateinit var char9Button: Button
    lateinit var timeLeftTextView: TextView
    lateinit var countDownTimer: CountDownTimer
    lateinit var radicalView: TextView
    lateinit var startButton: Button

    var jsonList:List<CharacterEntry> = ArrayList()

    var easy: Long = 120000
    var medium: Long = 60000
    var hard: Long = 30000

    val initialCountDown: Long = easy
    val countDownInterval: Long = 1000
    var timeRemaining: Long = easy

    var gameStarted = false
    var gameOverResponse = false //set to lose

    var yesChar1 : Boolean = false
    var yesChar2 : Boolean = false
    var yesChar3 : Boolean = false
    var yesChar4 : Boolean = false
    var yesChar5 : Boolean = false
    var yesChar6 : Boolean = false
    var noChar1 : Boolean = false
    var noChar2 : Boolean = false
    var noChar3 : Boolean = false

    lateinit var chineseMap: MutableMap<String, List<String>>
    lateinit var solutionKey: MutableList<String> //0-5 are correct 6-8 are incorrect

    private val radical = listOf("人", "儿", "力",  "刀", "亠", "冖", "厂", "阝", "言", "又", "十",
        "子", "几", "厶", "勹", "彳", "宀", "水", "冫", "广", "口", "囗", "士", "土", "犬", "艹", "糸",
        "辶", "门", "食", "马", "女", "大", "小", "寸", "山", "工", "巾", "干", "日", "月", "夕", "弓",
        "攵",  "户", "木","贝", "见", "王", "犬", "车", "心",  "手", "火", "欠", "斤", "止", "牛", "白",
        "禾", "衣", "示", "目", "罒",  "金", "鸟", "田", "石", "疒", "立", "皿", "虫", "竹", "米", "羊",
        "舌", "耳", "羽", "舟", "足", "酉", "鱼", "隹", "雨", "青")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matching)
        radicalView = findViewById(R.id.radical)
        timeLeftTextView = findViewById(R.id.timer)
        startButton =findViewById(R.id.startbutton)

        char1Button = findViewById(R.id.char1)
        char2Button = findViewById(R.id.char2)
        char3Button = findViewById(R.id.char3)
        char4Button = findViewById(R.id.char4)
        char5Button = findViewById(R.id.char5)
        char6Button = findViewById(R.id.char6)
        char7Button = findViewById(R.id.char7)
        char8Button = findViewById(R.id.char8)
        char9Button = findViewById(R.id.char9)

        val jsonfile = jsonConverter()
        val gsonFile = Gson().fromJson(jsonfile, Chinese::class.java)
        jsonList = gsonFile.character_entry
        chineseMap = mapMaker(jsonList)

        startButton.setOnClickListener{
            if (!gameStarted) { startGame()}
        }
        resetGame()
    }

    private fun mapMaker(list: List<CharacterEntry>):MutableMap<String, List<String>> {
        var breakdownMap = mutableMapOf<String, List<String>>()

        for (i in list.indices){
            var key = list[i].character
            var radical1 = list[i].radical_1
            var radical2 = list[i].radical_2
            var radical3 = list[i].radical_3
            var radical4 = list[i].radical_4
            var radical5 = list[i].radical_5
            var radical6 = list[i].radical_6
            var radical7 = list[i].radical_7
            var radical8 = list[i].radical_8
            var radical9 = list[i].radical_9
            var radical10 = list[i].radical_10
            var radical11 = list[i].radical_11
            var radical12 = list[i].radical_12
            var value = listOf(radical1, radical2, radical3, radical4, radical5, radical6, radical7, radical8, radical9, radical10, radical11,radical12)
            breakdownMap[key] = value
        }
        return breakdownMap
    }

    private fun solutionChecker(character: String):Boolean{

        //sets the value of the selected char to the opposite
        if (character == solutionKey[0]) {
            yesChar1 = !yesChar1
            return yesChar1}
        else if (character == solutionKey[1]) {
            yesChar2 = !yesChar2
            return yesChar2}
        else if (character == solutionKey[2]) {
            yesChar3 = !yesChar3
            return yesChar3}
        else if (character == solutionKey[3]) {
            yesChar4 = !yesChar4
            return yesChar4}
        else if (character == solutionKey[4]) {
            yesChar5 = !yesChar5
            return yesChar5}
        else if (character == solutionKey[5]) {
            yesChar6 = !yesChar6
            return yesChar6}
        else if (character == solutionKey[6]) {
            noChar1 = !noChar1
            return noChar1}
        else if (character == solutionKey[7]) {
            noChar2 = !noChar2
            return noChar2}
        else  {
            noChar3 = !noChar3
            return noChar3}
    }

    private fun buttonController(list: MutableList<String>){
        //randomly assigns buttons values so each game is different
        list.shuffle()
        char1Button.text = list[0]
        char2Button.text = list[1]
        char3Button.text = list[2]
        char4Button.text = list[3]
        char5Button.text = list[4]
        char6Button.text = list[5]
        char7Button.text = list[6]
        char8Button.text = list[7]
        char9Button.text = list[8]
    }

    private fun resetGame() {
        gameOverResponse = false
        val initialTimeLeft = initialCountDown / 1000
        timeLeftTextView.text = getString(R.string.timer, initialTimeLeft.toString())

        var buttonAssigner= randomizer(chineseMap)
        solutionKey = buttonAssigner.toMutableList()
        buttonVisibility(gameStarted)
        buttonController(buttonAssigner)

        //ensures no buttons are selected upon restart
        char1Button.setSelected(false)
        char2Button.setSelected(false)
        char3Button.setSelected(false)
        char4Button.setSelected(false)
        char5Button.setSelected(false)
        char6Button.setSelected(false)
        char7Button.setSelected(false)
        char8Button.setSelected(false)
        char9Button.setSelected(false)


        //reassign the values now that they've been checked and used for corrections
        yesChar1 = false
        yesChar2 = false
        yesChar3 = false
        yesChar4 = false
        yesChar5 = false
        yesChar6 = false
        noChar1 = false
        noChar2 = false
        noChar3 = false

        //controls each button individually
        char1Button.setOnClickListener{
            char1Button.setSelected(!char1Button.isSelected)
            solutionChecker(char1Button.text.toString())
        }
        char2Button.setOnClickListener{
            char2Button.setSelected(!char2Button.isSelected)
            solutionChecker(char2Button.text.toString())
        }
        char3Button.setOnClickListener{
            char3Button.setSelected(!char3Button.isSelected)
            solutionChecker(char3Button.text.toString())
        }
        char4Button.setOnClickListener{
            char4Button.setSelected(!char4Button.isSelected)
            solutionChecker(char4Button.text.toString())
        }
        char5Button.setOnClickListener{
            char5Button.setSelected(!char5Button.isSelected)
            solutionChecker(char5Button.text.toString())
        }
        char6Button.setOnClickListener{
            char6Button.setSelected(!char6Button.isSelected)
            solutionChecker(char6Button.text.toString())
        }
        char7Button.setOnClickListener{
            char7Button.setSelected(!char7Button.isSelected)
            solutionChecker(char7Button.text.toString())
        }
        char8Button.setOnClickListener{
            char8Button.setSelected(!char8Button.isSelected)
            solutionChecker(char8Button.text.toString())
        }
        char9Button.setOnClickListener{
            char9Button.setSelected(!char9Button.isSelected)
            solutionChecker(char9Button.text.toString())
        }

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

    private fun endGame(gameOverResponse: Boolean) {
        if (gameOverResponse) {
            Toast.makeText(this, getString(R.string.win), Toast.LENGTH_LONG).show()
            resetGame()
        } else {
            var message = getString(R.string.lose)
            message += " " + radicalView.text.toString()
            message += makeCorrections()

            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            resetGame()
        }
    }

    private fun makeCorrections() : String{
        var correctionsString = " "
        if (!yesChar1 || !yesChar2 || !yesChar3 || !yesChar4 || !yesChar5 || !yesChar6){
            correctionsString += "includes "
            if (!yesChar1){ correctionsString += solutionKey[0] }
            if (!yesChar2){ correctionsString += solutionKey[1] }
            if (!yesChar3){ correctionsString += solutionKey[2] }
            if (!yesChar4){ correctionsString += solutionKey[3] }
            if (!yesChar5){ correctionsString += solutionKey[4] }
            if (!yesChar6){ correctionsString += solutionKey[5] }
        }
        if (noChar1 || noChar2 || noChar3){
            correctionsString += " doesn't include "
            if (noChar1){ correctionsString += solutionKey[6] }
            if (noChar2){ correctionsString += solutionKey[7] }
            if (noChar3){ correctionsString += solutionKey[8] }
        }

        return correctionsString
    }

    private fun checkScore(): Boolean {
        if (yesChar1 && yesChar2 && yesChar3 && yesChar4 && yesChar5 && yesChar6 && !noChar1 && !noChar2 && !noChar3){
            gameOverResponse = true
        }
        return gameOverResponse
    }

    private fun randomizer(chineseMap: MutableMap<String, List<String>>): MutableList<String>{
        var radicalCopy = radical.toMutableList()
        var randomRadical = radicalCopy.shuffled().take(1)[0]//selects one random radical
        radicalCopy.remove(randomRadical)
        var randomContrast = radicalCopy.shuffled().take(1)[0]
        radicalView.text = randomRadical

        var radicalChecker = radicalExpander(randomRadical) //see if there are multiples
        var contrastChecker = radicalExpander(randomContrast)

        var filteredRadicals = mutableMapOf<String, List<String>>()
        var filteredContrast = mutableMapOf<String, List<String>>()

        for (i in radicalChecker){
            for (j in contrastChecker){
                filteredRadicals.putAll(chineseMap.filterValues { it.contains(i) && !it.contains(j)})
            }
        }

        for (i in contrastChecker){
            for (j in radicalChecker){
                filteredContrast.putAll(chineseMap.filterValues { it.contains(i) && !it.contains(j)})
            }
        }

        var radicalArray  = filteredRadicals.keys.toMutableList()
        var contrastArray  = filteredContrast.keys.toMutableList()

        //assign and return the 9 button values
        var buttonAssigner = mutableListOf<String>()
        for (i in radicalArray.shuffled().take(6)){
            buttonAssigner.add(i)
        }
        for (i in  contrastArray.shuffled().take(3)){
            buttonAssigner.add(i)
        }
        return buttonAssigner
    }

    private fun jsonConverter():String {
        val jsonfile = applicationContext.assets.open("chinese_breakdown.json").bufferedReader().use { it.readText() }
        Log.e("response", jsonfile)

        return jsonfile
    }

    private fun buttonVisibility(gameStarted:Boolean){
        if (gameStarted){
            showHide(char1Button)
            showHide(char2Button)
            showHide(char3Button)
            showHide(char4Button)
            showHide(char5Button)
            showHide(char6Button)
            showHide(char7Button)
            showHide(char8Button)
            showHide(char9Button)
            hideStart(startButton)
        }
        else{
            showHide(char1Button) //hides chars if game not started
            showHide(char2Button)
            showHide(char3Button)
            showHide(char4Button)
            showHide(char5Button)
            showHide(char6Button)
            showHide(char7Button)
            showHide(char8Button)
            showHide(char9Button)
        }
    }

    private fun showHide(view: Button){
        view.visibility = if (view.visibility == View.VISIBLE) {View.INVISIBLE}
        else {View.VISIBLE}
    }

    private fun hideStart(view:Button){
        view.visibility = if (view.visibility == View.VISIBLE) {View.GONE}
        else {View.VISIBLE}
    }

    private fun radicalExpander(char:String) : List<String> {
        var returnString = mutableListOf(char)
        when (char) {
            "人" -> returnString.add("亻")
            "刀" -> returnString.add("刂")
            "言" -> returnString.add("讠")
            "犬" -> returnString.add("犭")
            "糸" -> returnString.add("纟")
            "食" -> returnString.add("饣")
            "攵" -> returnString.add("攴")
            "心" -> returnString.add("忄")
            "衣" -> returnString.add("衤")
            "示" -> returnString.add("礻")
            "金" -> returnString.add("钅")
            "足" -> returnString.add("⻊")
            "火" -> returnString.add("灬")
            "水" -> {returnString.add("氵")
                returnString.add("氺")}
            "小" -> {returnString.add("⺌")
                returnString.add("⺍")}
            "手" -> {returnString.add("扌")
                returnString.add("龵")}
        }
        return returnString
    }
}
