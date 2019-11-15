package com.kingbart.radicallydifferent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.*
import com.google.gson.Gson
import com.kingbart.radicallydifferent.animationactivities.TryAgain
import com.kingbart.radicallydifferent.animationactivities.YouWin
import com.kingbart.radicallydifferent.jsonadapters.CharacterEntry
import com.kingbart.radicallydifferent.jsonadapters.Chinese
import kotlinx.android.synthetic.main.activity_road_trip.*

class RoadTripActivity : AppCompatActivity() {
    lateinit var pitStop1 : TextView
    lateinit var pitStop2 : TextView
    lateinit var pitStop3 : TextView
    lateinit var pitStop4 : TextView
    lateinit var pitStop5 : TextView
    lateinit var pitStop6 : TextView
    lateinit var startTextView: TextView
    lateinit var endTextView: TextView
    lateinit var startButton: Button
    lateinit var endButton: Button

    lateinit var timeLeftTextView: TextView
    lateinit var countDownTimer: CountDownTimer

    val initialCountDown: Long = 120000 //make level vars this is 2 mins
    val countDownInterval: Long = 1000
    var timeRemaining: Long = 120000

    var gameStarted = false
    var gameOverResponse = false //set to lose

    var jsonList: List<CharacterEntry> = ArrayList() //is entire JSON file
    lateinit var chineseMap: MutableMap<String, List<String>> //map of JSON file
    lateinit var solutionKey: MutableList<String>

    private val totalRadicals = listOf("人","亻","儿","力","刂","刀","亠","冖","厂","阝","讠","言","又",
        "十","子","几","厶","勹","彳","宀","氵","水","氺","冫","广","口","囗","士","土","犭","犬","艹",
        "纟","糸","辶","门","饣","食","马","女","大","小","⺌","⺍","寸","山","工","巾","干","日","月",
        "夕","弓","攵","攴","户","戶","木","贝","见","王","车","心","忄","手","扌","龵","火","灬","欠",
        "斤","止","牛","白","禾","衤","衣","礻","示","目","罒","钅","金","鸟","田","石","疒","立","皿",
        "虫","竹","米","羊","舌","耳","羽","舟","足","⻊","酉","鱼","隹","雨","青")
    var dropDownOptions = arrayOf(" ", " ", " ", " ", " ", " ", " ")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_road_trip)
        timeLeftTextView = findViewById(R.id.timer)

        startTextView = findViewById(R.id.start)//do not enable drag
        endTextView = findViewById(R.id.end)

        pitStop1 = findViewById(R.id.pitstop1)//these are slots for the hanzi to drop into
        pitStop2 = findViewById(R.id.pitstop2)
        pitStop3 = findViewById(R.id.pitstop3)
        pitStop4 = findViewById(R.id.pitstop4)
        pitStop5 = findViewById(R.id.pitstop5)
        pitStop6 = findViewById(R.id.pitstop6)

        startButton = findViewById(R.id.startButton)
        endButton = findViewById(R.id.endGame)

        var jsonfile = jsonConverter()
        val gsonFile = Gson().fromJson(jsonfile, Chinese::class.java)
        jsonList = gsonFile.character_entry
        chineseMap = mapMaker(jsonList)

        startButton.setOnClickListener{ if (!gameStarted) { startGame()} }

        resetGame()
    }

    private fun jsonConverter(): String {
        val jsonfile = applicationContext.assets.open("chinese_breakdown.json").bufferedReader()
            .use { it.readText() }
        Log.e("response", jsonfile)

        return jsonfile
    }

    private fun mapMaker(list: List<CharacterEntry>): MutableMap<String, List<String>> {
        var breakdownMap = mutableMapOf<String, List<String>>()

        for (i in list.indices) {
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
            var value = listOf(radical1,radical2,radical3,radical4,radical5,radical6,
                radical7,radical8,radical9,radical10,radical11,radical12)
            breakdownMap[key] = value
        }
        return breakdownMap
    }

    private fun resetGame() {
        gameOverResponse = false
        val initialTimeLeft = initialCountDown / 1000
        timeLeftTextView.text = getString(R.string.timer, initialTimeLeft.toString())

        var hanziAssigner = randomizer(chineseMap)

        buttonVisibility(gameStarted) //hides the Hanzi while the timer is off
        hanziController(hanziAssigner)

        makeSpinners()

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

    private fun startGame() {
        countDownTimer.start()
        gameStarted = true
        gameOverResponse = false

        buttonVisibility(gameStarted)
    }

    private fun makeSpinners(){
        //this creates the ability to have a drop down menu
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dropDownOptions)
        arrayAdapter.setDropDownViewResource(R.layout.spinner_text)

        //pitstop1
        val spinner1 = findViewById<Spinner>(R.id.spinner1)
        spinner1.adapter = arrayAdapter
        spinner1.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                pitStop1.text = dropDownOptions[position]
                when(solutionKey.indexOf(dropDownOptions[position])){
                    2 -> {spinner2?.setSelection(0)
                        solutionKey[2] = ""}
                    3 -> {spinner3?.setSelection(0)
                        solutionKey[3] = ""}
                    4 -> {spinner4?.setSelection(0)
                        solutionKey[4] = ""}
                    5 -> {spinner5?.setSelection(0)
                        solutionKey[5] = ""}
                    6 -> {spinner6?.setSelection(0)
                        solutionKey[6] = ""}
                }
                solutionKey[1] = dropDownOptions[position]
            }
        }

        //pitstop2
        val spinner2 = findViewById<Spinner>(R.id.spinner2)
        spinner2.adapter = arrayAdapter
        spinner2.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                pitStop2.text = dropDownOptions[position]
                when(solutionKey.indexOf(dropDownOptions[position])){
                    1 -> {spinner1?.setSelection(0)
                        solutionKey[1] = ""}
                    3 -> {spinner3?.setSelection(0)
                        solutionKey[3] = ""}
                    4 -> {spinner4?.setSelection(0)
                        solutionKey[4] = ""}
                    5 -> {spinner5?.setSelection(0)
                        solutionKey[5] = ""}
                    6 -> {spinner6?.setSelection(0)
                        solutionKey[6] = ""}
                }
                solutionKey[2] = dropDownOptions[position]
            }
        }

        //pitstop3
        val spinner3 = findViewById<Spinner>(R.id.spinner3)
        spinner3.adapter = arrayAdapter
        spinner3.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                pitStop3.text = dropDownOptions[position]
                when(solutionKey.indexOf(dropDownOptions[position])){
                    1 -> {spinner1?.setSelection(0)
                        solutionKey[1] = ""}
                    2 -> {spinner2?.setSelection(0)
                        solutionKey[2] = ""}
                    4 -> {spinner4?.setSelection(0)
                        solutionKey[4] = ""}
                    5 -> {spinner5?.setSelection(0)
                        solutionKey[5] = ""}
                    6 -> {spinner6?.setSelection(0)
                        solutionKey[6] = ""}
                }
                solutionKey[3] = dropDownOptions[position]
            }
        }

        //pitstop4
        val spinner4 = findViewById<Spinner>(R.id.spinner4)
        spinner4.adapter = arrayAdapter
        spinner4.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                pitStop4.text = dropDownOptions[position]
                when(solutionKey.indexOf(dropDownOptions[position])){
                    1 -> {spinner1?.setSelection(0)
                        solutionKey[1] = ""}
                    2 -> {spinner2?.setSelection(0)
                        solutionKey[2] = ""}
                    3 -> {spinner3?.setSelection(0)
                        solutionKey[3] = ""}
                    5 -> {spinner5?.setSelection(0)
                        solutionKey[5] = ""}
                    6 -> {spinner6?.setSelection(0)
                        solutionKey[6] = ""}
                }
                solutionKey[4] = dropDownOptions[position]
            }
        }

        //pitstop5
        val spinner5 = findViewById<Spinner>(R.id.spinner5)
        spinner5.adapter = arrayAdapter
        spinner5.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                pitStop5.text = dropDownOptions[position]
                when(solutionKey.indexOf(dropDownOptions[position])){
                    1 -> {spinner1?.setSelection(0)
                        solutionKey[1] = ""}
                    2 -> {spinner2?.setSelection(0)
                        solutionKey[2] = ""}
                    3 -> {spinner3?.setSelection(0)
                        solutionKey[3] = ""}
                    4 -> {spinner4?.setSelection(0)
                        solutionKey[4] = ""}
                    6 -> {spinner6?.setSelection(0)
                        solutionKey[6] = ""}
                }
                solutionKey[5] = dropDownOptions[position]
            }

        }

        //pitstop6
        val spinner6 = findViewById<Spinner>(R.id.spinner6)
        spinner6.adapter = arrayAdapter
        spinner6.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                pitStop6.text = dropDownOptions[position]
                when(solutionKey.indexOf(dropDownOptions[position])){
                    1 -> {spinner1?.setSelection(0)
                        solutionKey[1] = ""}
                    2 -> {spinner2?.setSelection(0)
                        solutionKey[2] = ""}
                    3 -> {spinner3?.setSelection(0)
                        solutionKey[3] = ""}
                    4 -> {spinner4?.setSelection(0)
                        solutionKey[4] = ""}
                    5 -> {spinner5?.setSelection(0)
                        solutionKey[5] = ""}
                }
                solutionKey[6] = dropDownOptions[position]
            }
        }
    }

    private fun checkScore(): Boolean {
        //these all indicate some overlap between the 2 Hanzi
        //if this allows just random answers, restrict to only overlapping with totalRadicals
        solutionKey[0] = startTextView.text.toString()
        solutionKey[7] = startTextView.text.toString()

        for (i in 0..6){
            if(chineseMap[solutionKey[i]]!!.toSet().intersect(chineseMap[solutionKey[i+1]]!!.toSet()).isNotEmpty()) {
                gameOverResponse = true
            }
        }
        return gameOverResponse
    }

    private fun endGame(gameOverResponse: Boolean) {
        hideButton(endButton)

        val intent: Intent
        when (gameOverResponse) {
            true -> intent = Intent(this, YouWin::class.java)
            false -> intent = Intent(this, TryAgain::class.java)
        }

        endButton.setOnClickListener { startActivity(intent) }
    }

    private fun hanziController(list: MutableList<String>) {
        //randomly assigns buttons values so each game is different
        startTextView.text = list[0]
        list.removeAt(0) // so it's not selected 2x
        endTextView.text = list[6]
        list.removeAt(6)

        list.shuffle()
        dropDownOptions[0] = " "
        dropDownOptions[1] = list[0]
        dropDownOptions[2] = list[1]
        dropDownOptions[3] = list[2]
        dropDownOptions[4] = list[3]
        dropDownOptions[5] = list[4]
        dropDownOptions[6] = list[5]
    }

    private fun buttonVisibility(gameStarted: Boolean) {
        if (gameStarted) {
            hideButton(startButton)
        }
    }

    private fun hideButton(view: Button) {
        view.visibility = if (view.visibility == View.VISIBLE) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun randomizer(chineseMap: MutableMap<String, List<String>>): MutableList<String> {
        var mapCopy = chineseMap.toMutableMap() // so we can remove keys
        var hanziAssigner = mutableListOf<String>()
        var includedRadicalList = mutableListOf<String>()

        //create a list of keys to select randomly from
        var keyList = mapCopy.keys.toMutableList()

        while (hanziAssigner.size < 1){
            var randomHanzi = keyList.shuffled().take(1)[0]//select one random Hanzi
            var values = mapCopy[randomHanzi] as List<String>

            var miniRadicalList = values.intersect(totalRadicals)
            if (miniRadicalList.size >= 2) {
                includedRadicalList.addAll(miniRadicalList) // be able to use all of these radicals
                hanziAssigner.add(randomHanzi) //add it to the answers
                mapCopy.keys.remove(randomHanzi) //don't risk picking the same hanzi
                includedRadicalList = radicalExpander(includedRadicalList)
            }

        }

        while (hanziAssigner.size < 2){
            var filteredHanzi = mutableMapOf<String, List<String>>()
            for (i in includedRadicalList) { //must have at least one value from previous Hanzi
                filteredHanzi.putAll(mapCopy.filterValues { it.contains(i) })
            }
            keyList = filteredHanzi.keys.toMutableList()
            var randomHanzi = keyList.shuffled().take(1)[0]//select one random Hanzi
            var values = mapCopy[randomHanzi] as List<String>
            var miniRadicalList = values.intersect(totalRadicals)
            if (miniRadicalList.size >= 2) {
                includedRadicalList.clear()
                includedRadicalList.addAll(miniRadicalList)
                hanziAssigner.add(randomHanzi)
                mapCopy.keys.remove(randomHanzi)
                includedRadicalList = radicalExpander(includedRadicalList)
            }
        }

        while (hanziAssigner.size < 3){
            var filteredHanzi = mutableMapOf<String, List<String>>()
            for (i in includedRadicalList) { //must have at least one value from previous Hanzi
                filteredHanzi.putAll(mapCopy.filterValues { it.contains(i) })
            }
            keyList = filteredHanzi.keys.toMutableList()
            var randomHanzi = keyList.shuffled().take(1)[0]//select one random Hanzi
            var values = mapCopy[randomHanzi] as List<String>
            var miniRadicalList = values.intersect(totalRadicals)
            if (miniRadicalList.size >= 2) {
                includedRadicalList.clear()
                includedRadicalList.addAll(miniRadicalList)
                hanziAssigner.add(randomHanzi)
                mapCopy.keys.remove(randomHanzi)
                includedRadicalList = radicalExpander(includedRadicalList)
            }
        }

        while (hanziAssigner.size < 4){
            var filteredHanzi = mutableMapOf<String, List<String>>()
            for (i in includedRadicalList) { //must have at least one value from previous Hanzi
                filteredHanzi.putAll(mapCopy.filterValues { it.contains(i) })
            }
            keyList = filteredHanzi.keys.toMutableList()
            var randomHanzi = keyList.shuffled().take(1)[0]//select one random Hanzi
            var values = mapCopy[randomHanzi] as List<String>
            var miniRadicalList = values.intersect(totalRadicals)
            if (miniRadicalList.size >= 2) {
                includedRadicalList.clear()
                includedRadicalList.addAll(miniRadicalList)
                hanziAssigner.add(randomHanzi)
                mapCopy.keys.remove(randomHanzi)
                includedRadicalList = radicalExpander(includedRadicalList)
            }
        }

        while (hanziAssigner.size < 5){
            var filteredHanzi = mutableMapOf<String, List<String>>()
            for (i in includedRadicalList) { //must have at least one value from previous Hanzi
                filteredHanzi.putAll(mapCopy.filterValues { it.contains(i) })
            }
            keyList = filteredHanzi.keys.toMutableList()
            var randomHanzi = keyList.shuffled().take(1)[0]//select one random Hanzi
            var values = mapCopy[randomHanzi] as List<String>
            var miniRadicalList = values.intersect(totalRadicals)
            if (miniRadicalList.size >= 2) {
                includedRadicalList.clear()
                includedRadicalList.addAll(miniRadicalList)
                hanziAssigner.add(randomHanzi)
                mapCopy.keys.remove(randomHanzi)
                includedRadicalList = radicalExpander(includedRadicalList)
            }
        }

        while (hanziAssigner.size < 6){
            var filteredHanzi = mutableMapOf<String, List<String>>()
            for (i in includedRadicalList) { //must have at least one value from previous Hanzi
                filteredHanzi.putAll(mapCopy.filterValues { it.contains(i) })
            }
            keyList = filteredHanzi.keys.toMutableList()
            var randomHanzi = keyList.shuffled().take(1)[0]//select one random Hanzi
            var values = mapCopy[randomHanzi] as List<String>
            var miniRadicalList = values.intersect(totalRadicals)
            if (miniRadicalList.size >= 2) {
                includedRadicalList.clear()
                includedRadicalList.addAll(miniRadicalList)
                hanziAssigner.add(randomHanzi)
                mapCopy.keys.remove(randomHanzi)
                includedRadicalList = radicalExpander(includedRadicalList)
            }
        }

        while (hanziAssigner.size < 7){
            var filteredHanzi = mutableMapOf<String, List<String>>()
            for (i in includedRadicalList) { //must have at least one value from previous Hanzi
                filteredHanzi.putAll(mapCopy.filterValues { it.contains(i) })
            }
            keyList = filteredHanzi.keys.toMutableList()
            var randomHanzi = keyList.shuffled().take(1)[0]//select one random Hanzi
            var values = mapCopy[randomHanzi] as List<String>
            var miniRadicalList = values.intersect(totalRadicals)
            if (miniRadicalList.size >= 2) {
                includedRadicalList.clear()
                includedRadicalList.addAll(miniRadicalList)
                hanziAssigner.add(randomHanzi)
                mapCopy.keys.remove(randomHanzi)
                includedRadicalList = radicalExpander(includedRadicalList)
            }
        }

        while (hanziAssigner.size < 8){
            var filteredHanzi = mutableMapOf<String, List<String>>()
            for (i in includedRadicalList) { //must have at least one value from previous Hanzi
                filteredHanzi.putAll(mapCopy.filterValues { it.contains(i) })
            }
            keyList = filteredHanzi.keys.toMutableList()
            var randomHanzi = keyList.shuffled().take(1)[0]//select one random Hanzi
            var values = mapCopy[randomHanzi] as List<String>
            var miniRadicalList = values.intersect(totalRadicals)
            if (miniRadicalList.size >= 2) {
                includedRadicalList.clear()
                includedRadicalList.addAll(miniRadicalList)
                hanziAssigner.add(randomHanzi)
                mapCopy.keys.remove(randomHanzi)
                includedRadicalList = radicalExpander(includedRadicalList)
            }
        }

        return hanziAssigner
    }

    private fun radicalExpander(radicals: List<String>): ArrayList<String> {
        var returnList = arrayListOf<String>()

        for (radical in radicals) {
            returnList.add(radical) //auto add the char
            //if the radical has variations, add the others to check from
            when(radical){
                "人" -> returnList.add("亻")
                "亻" -> returnList.add("人")
                "刀" -> returnList.add("刂")
                "刂" -> returnList.add("刀")
                "言" -> returnList.add("讠")
                "讠" -> returnList.add("言")
                "犬" -> returnList.add("犭")
                "犭" -> returnList.add("犬")
                "糸" -> returnList.add("纟")
                "纟" -> returnList.add("糸")
                "食" -> returnList.add("饣")
                "饣" -> returnList.add("食")
                "攵" -> returnList.add("攴")
                "攴" -> returnList.add("攵")
                "心" -> returnList.add("忄")
                "忄" -> returnList.add("心")
                "衣" -> returnList.add("衤")
                "衤" -> returnList.add("衣")
                "示" -> returnList.add("礻")
                "礻" -> returnList.add("示")
                "金" -> returnList.add("钅")
                "钅" -> returnList.add("金")
                "足" -> returnList.add("⻊")
                "⻊" -> returnList.add("足")
                "火" -> returnList.add("灬")
                "灬" -> returnList.add("火")
                "水" -> {returnList.add("氵")
                        returnList.add("氺")}
                "氵" -> {returnList.add("水")
                        returnList.add("氺")}
                "氺" -> {returnList.add("水")
                        returnList.add("氵")}
                "小" -> {returnList.add("⺌")
                        returnList.add("⺍")}
                "⺌" -> {returnList.add("小")
                        returnList.add("⺍")}
                "⺍" -> {returnList.add("小")
                        returnList.add("⺌")}
                "手" -> {returnList.add("扌")
                        returnList.add("龵")}
                "扌" -> {returnList.add("手")
                        returnList.add("龵")}
                "龵" -> {returnList.add("手")
                        returnList.add("扌")}
            }
        }
        return returnList
    }
}