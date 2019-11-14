package com.kingbart.radicallydifferent

import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import com.kingbart.radicallydifferent.animationactivities.TryAgain
import com.kingbart.radicallydifferent.animationactivities.YouWin
import com.kingbart.radicallydifferent.jsonadapters.CharacterEntry
import com.kingbart.radicallydifferent.jsonadapters.Chinese

class RoadTripActivity : AppCompatActivity() {
    lateinit var hanzi1TextView: TextView
    lateinit var hanzi2TextView: TextView
    lateinit var hanzi3TextView: TextView
    lateinit var hanzi4TextView: TextView
    lateinit var hanzi5TextView: TextView
    lateinit var hanzi6TextView: TextView
    lateinit var startTextView: TextView
    lateinit var endTextView: TextView
    lateinit var startButton: Button
    lateinit var endButton: Button
    
    lateinit var pitStop1 : ImageView
    lateinit var pitStop2 : ImageView
    lateinit var pitStop3 : ImageView
    lateinit var pitStop4 : ImageView
    lateinit var pitStop5 : ImageView
    lateinit var pitStop6 : ImageView

    /*lateinit var pRect1 : Rect //used to detect collision
    lateinit var pRect2 : Rect
    lateinit var pRect3 : Rect
    lateinit var pRect4 : Rect
    lateinit var pRect5 : Rect
    lateinit var pRect6 : Rect*/

    /*lateinit var hRect1 : Rect //used to detect collision
    lateinit var hRect2 : Rect
    lateinit var hRect3 : Rect
    lateinit var hRect4 : Rect
    lateinit var hRect5 : Rect
    lateinit var hRect6 : Rect*/

    /*var selectedHanzi : Rect? = null
    var selectedPitstop : Rect? = null*/

    //var collision = Rect.intersects(selectedHanzi,selectedPitstop)

    lateinit var timeLeftTextView: TextView
    lateinit var countDownTimer: CountDownTimer

    val initialCountDown: Long = 120000 //make level vars this is 2 mins
    val countDownInterval: Long = 1000
    var timeRemaining: Long = 120000

    var gameStarted = false
    var gameOverResponse = false //set to lose

    var jsonList: List<CharacterEntry> = ArrayList() //is entire JSON file
    lateinit var chineseMap: MutableMap<String, List<String>> //map of JSON file
    lateinit var miniMap: MutableMap<String, List<String>> //so it's only 8 long
    lateinit var solutionKey: MutableList<String>

    private val totalRadicals = listOf("人","亻","儿","力","刂","刀","亠","冖","厂","阝","讠","言","又",
        "十","子","几","厶","勹","彳","宀","氵","水","氺","冫","广","口","囗","士","土","犭","犬","艹",
        "纟","糸","辶","门","饣","食","马","女","大","小","⺌","⺍","寸","山","工","巾","干","日","月",
        "夕","弓","攵","攴","户","戶","木","贝","见","王","车","心","忄","手","扌","龵","火","灬","欠",
        "斤","止","牛","白","禾","衤","衣","礻","示","目","罒","钅","金","鸟","田","石","疒","立","皿",
        "虫","竹","米","羊","舌","耳","羽","舟","足","⻊","酉","鱼","隹","雨","青")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_road_trip)
        timeLeftTextView = findViewById(R.id.timer)

        hanzi1TextView = findViewById(R.id.hanzi1)
        hanzi2TextView = findViewById(R.id.hanzi2)
        hanzi3TextView = findViewById(R.id.hanzi3)
        hanzi4TextView = findViewById(R.id.hanzi4)
        hanzi5TextView = findViewById(R.id.hanzi5)
        hanzi6TextView = findViewById(R.id.hanzi6)

        startTextView = findViewById(R.id.start)//do not enable drag
        endTextView = findViewById(R.id.end)

        pitStop1 = findViewById(R.id.pitstop1)//these are slots for the hanzi to drop into
        pitStop2 = findViewById(R.id.pitstop2)
        pitStop3 = findViewById(R.id.pitstop3)
        pitStop4 = findViewById(R.id.pitstop4)
        pitStop5 = findViewById(R.id.pitstop5)
        pitStop6 = findViewById(R.id.pitstop6)

        /*pRect1 = pitStop1.drawable.bounds
        pRect2 = pitStop2.drawable.bounds
        pRect3 = pitStop3.drawable.bounds
        pRect4 = pitStop4.drawable.bounds
        pRect5 = pitStop5.drawable.bounds
        pRect6 = pitStop6.drawable.bounds*/

        //init a rect by creating the 4 parameters
        /*hRect1 = Rect(hanzi1TextView.left, hanzi1TextView.top, hanzi1TextView.bottom, hanzi1TextView.right)
        hRect2 = Rect(hanzi2TextView.left, hanzi2TextView.top, hanzi2TextView.bottom, hanzi2TextView.right)
        hRect3 = Rect(hanzi3TextView.left, hanzi3TextView.top, hanzi3TextView.bottom, hanzi3TextView.right)
        hRect4 = Rect(hanzi4TextView.left, hanzi4TextView.top, hanzi4TextView.bottom, hanzi4TextView.right)
        hRect5 = Rect(hanzi5TextView.left, hanzi5TextView.top, hanzi5TextView.bottom, hanzi5TextView.right)
        hRect6 = Rect(hanzi6TextView.left, hanzi6TextView.top, hanzi6TextView.bottom, hanzi6TextView.right)*/

        startButton = findViewById(R.id.startButton)
        endButton = findViewById(R.id.endGame)

        var jsonfile = jsonConverter()
        val gsonFile = Gson().fromJson(jsonfile, Chinese::class.java)
        jsonList = gsonFile.character_entry
        chineseMap = mapMaker(jsonList)

        //startButton.setOnClickListener{ if (!gameStarted) { startGame()} }

        resetGame()
    }

    private fun resetGame() {
        gameOverResponse = false
        val initialTimeLeft = initialCountDown / 1000
        timeLeftTextView.text = getString(R.string.timer, initialTimeLeft.toString())

        var hanziAssigner = randomizer(chineseMap)

        //buttonVisibility(gameStarted) //hides the Hanzi while the timer is off
        hanziController(hanziAssigner)

        //allows for a Hanzi to be moved
        val dragListener = View.OnTouchListener(function = { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_MOVE) {
                view.y = motionEvent.rawY - view.height / 2
                view.x = motionEvent.rawX - view.width / 2
            }
            true
        })

        hanzi1TextView.setOnTouchListener(dragListener)
        hanzi2TextView.setOnTouchListener(dragListener)
        hanzi3TextView.setOnTouchListener(dragListener)
        hanzi4TextView.setOnTouchListener(dragListener)
        hanzi5TextView.setOnTouchListener(dragListener)
        hanzi6TextView.setOnTouchListener(dragListener)
        

        /*createSolutionKey(hanzi1TextView)
        createSolutionKey(hanzi2TextView)
        createSolutionKey(hanzi3TextView)
        createSolutionKey(hanzi4TextView)
        createSolutionKey(hanzi5TextView)
        createSolutionKey(hanzi6TextView)*/

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

    private fun checkScore(): Boolean {
        //these all indicate some overlap between the 2 Hanzi
        //if this allows just random answers, restrict to only overlapping with totalRadicals
        /*var size0 = miniMap[solutionKey[0]].intersect(miniMap[solutionKey[1]]).size
        var size1 = miniMap[solutionKey[1]].intersect(miniMap[solutionKey[2]]).size
        var size2 = miniMap[solutionKey[2]].intersect(miniMap[solutionKey[3]]).size
        var size3 = miniMap[solutionKey[3]].intersect(miniMap[solutionKey[4]]).size
        var size4 = miniMap[solutionKey[4]].intersect(miniMap[solutionKey[5]]).size
        var size5 = miniMap[solutionKey[5]].intersect(miniMap[solutionKey[6]]).size
        var size6 = miniMap[solutionKey[6]].intersect(miniMap[solutionKey[7]]).size

        //var sizeTest = miniMap[solutionKey[0]].intersect(miniMap[solutionKey[1]]).intersect(totalRadicals).size


        if (size0 > 0 && size1 > 0 && size2 > 0 && size3 > 0 && size4 > 0 && size5 > 0 && size6 > 0)
        { gameOverResponse = true } //overlap exists between all Hanzi*/

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

    private fun jsonConverter(): String {
        val jsonfile = applicationContext.assets.open("chinese_breakdown.json").bufferedReader()
            .use { it.readText() }
        Log.e("response", jsonfile)

        return jsonfile
    }

    private fun hanziController(list: MutableList<String>) {
        //randomly assigns buttons values so each game is different
        startTextView.text = list[0]
        list.removeAt(0) // so it's not selected 2x
        endTextView.text = list[6]
        list.removeAt(6)

        list.shuffle()
        hanzi1TextView.text = list[0]
        hanzi2TextView.text = list[1]
        hanzi3TextView.text = list[2]
        hanzi4TextView.text = list[3]
        hanzi5TextView.text = list[4]
        hanzi6TextView.text = list[5]
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

    private fun buttonVisibility(gameStarted: Boolean) {
        if (gameStarted) {
            showHide(hanzi1TextView)
            showHide(hanzi2TextView)
            showHide(hanzi3TextView)
            showHide(hanzi4TextView)
            showHide(hanzi5TextView)
            showHide(hanzi6TextView)
            hideButton(startButton)

        } else {
            showHide(hanzi1TextView) //hides hanzi if game not started
            showHide(hanzi2TextView)
            showHide(hanzi3TextView)
            showHide(hanzi4TextView)
            showHide(hanzi5TextView)
            showHide(hanzi6TextView)
        }
    }

    private fun showHide(view: View) {
        view.visibility = if (view.visibility == View.VISIBLE) {
            View.INVISIBLE
        } else {
            View.VISIBLE
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
        miniMap = mutableMapOf()
        var hanziAssigner = mutableListOf<String>()
        var includedRadicalList = mutableListOf<String>()

        //create a list of keys to select randomly from
        var keyList = chineseMap.keys.toMutableList()

        while (hanziAssigner.size < 1){
            var randomHanzi = keyList.shuffled().take(1)[0]//select one random Hanzi
            var values = chineseMap[randomHanzi] as List<String>

            var miniRadicalList = values.intersect(totalRadicals)
            if (miniRadicalList.size >= 2) {
                includedRadicalList.addAll(miniRadicalList) // be able to use all of these radicals
                miniMap[randomHanzi] = values //makes a smaller map to solution check
                hanziAssigner.add(randomHanzi) //add it to the answers
                keyList.remove(randomHanzi) //don't risk picking the same hanzi
                includedRadicalList = radicalExpander(includedRadicalList)
            }

        }

        while (hanziAssigner.size < 2){
            var filteredHanzi = mutableMapOf<String, List<String>>()
            for (i in includedRadicalList) { //must have at least one value from previous Hanzi
                filteredHanzi.putAll(chineseMap.filterValues { it.contains(i) })
            }
            keyList = filteredHanzi.keys.toMutableList()
            var randomHanzi = keyList.shuffled().take(1)[0]//select one random Hanzi
            var values = chineseMap[randomHanzi] as List<String>
            var miniRadicalList = values.intersect(totalRadicals)
            if (miniRadicalList.size >= 2) {
                includedRadicalList.clear()
                includedRadicalList.addAll(miniRadicalList)
                miniMap[randomHanzi] = values
                hanziAssigner.add(randomHanzi)
                includedRadicalList = radicalExpander(includedRadicalList)
            }
        }

        while (hanziAssigner.size < 3){
            var filteredHanzi = mutableMapOf<String, List<String>>()
            for (i in includedRadicalList) { //must have at least one value from previous Hanzi
                filteredHanzi.putAll(chineseMap.filterValues { it.contains(i) })
            }
            keyList = filteredHanzi.keys.toMutableList()
            var randomHanzi = keyList.shuffled().take(1)[0]//select one random Hanzi
            var values = chineseMap[randomHanzi] as List<String>
            var miniRadicalList = values.intersect(totalRadicals)
            if (miniRadicalList.size >= 2) {
                includedRadicalList.clear()
                includedRadicalList.addAll(miniRadicalList)
                miniMap[randomHanzi] = values
                hanziAssigner.add(randomHanzi)
                includedRadicalList = radicalExpander(includedRadicalList)
            }
        }

        while (hanziAssigner.size < 4){
            var filteredHanzi = mutableMapOf<String, List<String>>()
            for (i in includedRadicalList) { //must have at least one value from previous Hanzi
                filteredHanzi.putAll(chineseMap.filterValues { it.contains(i) })
            }
            keyList = filteredHanzi.keys.toMutableList()
            var randomHanzi = keyList.shuffled().take(1)[0]//select one random Hanzi
            var values = chineseMap[randomHanzi] as List<String>
            var miniRadicalList = values.intersect(totalRadicals)
            if (miniRadicalList.size >= 2) {
                includedRadicalList.clear()
                includedRadicalList.addAll(miniRadicalList)
                miniMap[randomHanzi] = values
                hanziAssigner.add(randomHanzi)
                includedRadicalList = radicalExpander(includedRadicalList)
            }
        }

        while (hanziAssigner.size < 5){
            var filteredHanzi = mutableMapOf<String, List<String>>()
            for (i in includedRadicalList) { //must have at least one value from previous Hanzi
                filteredHanzi.putAll(chineseMap.filterValues { it.contains(i) })
            }
            keyList = filteredHanzi.keys.toMutableList()
            var randomHanzi = keyList.shuffled().take(1)[0]//select one random Hanzi
            var values = chineseMap[randomHanzi] as List<String>
            var miniRadicalList = values.intersect(totalRadicals)
            if (miniRadicalList.size >= 2) {
                includedRadicalList.clear()
                includedRadicalList.addAll(miniRadicalList)
                miniMap[randomHanzi] = values
                hanziAssigner.add(randomHanzi)
                includedRadicalList = radicalExpander(includedRadicalList)
            }
        }

        while (hanziAssigner.size < 6){
            var filteredHanzi = mutableMapOf<String, List<String>>()
            for (i in includedRadicalList) { //must have at least one value from previous Hanzi
                filteredHanzi.putAll(chineseMap.filterValues { it.contains(i) })
            }
            keyList = filteredHanzi.keys.toMutableList()
            var randomHanzi = keyList.shuffled().take(1)[0]//select one random Hanzi
            var values = chineseMap[randomHanzi] as List<String>
            var miniRadicalList = values.intersect(totalRadicals)
            if (miniRadicalList.size >= 2) {
                includedRadicalList.clear()
                includedRadicalList.addAll(miniRadicalList)
                miniMap[randomHanzi] = values
                hanziAssigner.add(randomHanzi)
                includedRadicalList = radicalExpander(includedRadicalList)
            }
        }

        while (hanziAssigner.size < 7){
            var filteredHanzi = mutableMapOf<String, List<String>>()
            for (i in includedRadicalList) { //must have at least one value from previous Hanzi
                filteredHanzi.putAll(chineseMap.filterValues { it.contains(i) })
            }
            keyList = filteredHanzi.keys.toMutableList()
            var randomHanzi = keyList.shuffled().take(1)[0]//select one random Hanzi
            var values = chineseMap[randomHanzi] as List<String>
            var miniRadicalList = values.intersect(totalRadicals)
            if (miniRadicalList.size >= 2) {
                includedRadicalList.clear()
                includedRadicalList.addAll(miniRadicalList)
                miniMap[randomHanzi] = values
                hanziAssigner.add(randomHanzi)
                includedRadicalList = radicalExpander(includedRadicalList)
            }
        }

        while (hanziAssigner.size < 8){
            var filteredHanzi = mutableMapOf<String, List<String>>()
            for (i in includedRadicalList) { //must have at least one value from previous Hanzi
                filteredHanzi.putAll(chineseMap.filterValues { it.contains(i) })
            }
            keyList = filteredHanzi.keys.toMutableList()
            var randomHanzi = keyList.shuffled().take(1)[0]//select one random Hanzi
            var values = chineseMap[randomHanzi] as List<String>
            var miniRadicalList = values.intersect(totalRadicals)
            if (miniRadicalList.size >= 2) {
                includedRadicalList.clear()
                includedRadicalList.addAll(miniRadicalList)
                miniMap[randomHanzi] = values
                hanziAssigner.add(randomHanzi)
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

    /*override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.apply {
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    selectedHanzi = when {
                        hRect1.contains(x.toInt(),y.toInt()) -> hRect1
                        hRect2.contains(x.toInt(),y.toInt()) -> hRect2
                        hRect3.contains(x.toInt(),y.toInt()) -> hRect3
                        hRect4.contains(x.toInt(),y.toInt()) -> hRect4
                        hRect5.contains(x.toInt(),y.toInt()) -> hRect5
                        hRect6.contains(x.toInt(),y.toInt()) -> hRect6
                        else -> null
                       }
                    selectedPitstop = when{
                        pRect1.contains(x.toInt(),y.toInt()) -> pRect1
                        pRect2.contains(x.toInt(),y.toInt()) -> pRect2
                        pRect3.contains(x.toInt(),y.toInt()) -> pRect3
                        pRect4.contains(x.toInt(),y.toInt()) -> pRect4
                        pRect5.contains(x.toInt(),y.toInt()) -> pRect5
                        pRect6.contains(x.toInt(),y.toInt()) -> pRect6
                        else -> null
                    }
                    return true
                    }
                MotionEvent.ACTION_MOVE -> {
                    when (selectedHanzi){
                        hRect1 -> hRect1.offset(x.toInt(),y.toInt())
                        hRect2 -> hRect2.offset(x.toInt(),y.toInt())
                        hRect3 -> hRect3.offset(x.toInt(),y.toInt())
                        hRect4 -> hRect4.offset(x.toInt(),y.toInt())
                        hRect5 -> hRect5.offset(x.toInt(),y.toInt())
                        hRect6 -> hRect6.offset(x.toInt(),y.toInt())
                        else -> return true
                        }
                    collision = Rect.intersects(selectedHanzi, selectedPitstop)
                    return true
                    }
                }
        }
        return false
    }*/
}