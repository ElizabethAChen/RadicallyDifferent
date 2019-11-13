package com.kingbart.radicallydifferent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.kingbart.radicallydifferent.jsonadapters.Library
import com.kingbart.radicallydifferent.jsonadapters.RadicalEntry
import kotlinx.android.synthetic.main.activity_recycler_view.*

class RecyclerViewActivity : AppCompatActivity() {

    var libraryList:List<RadicalEntry> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        //file reading and data creation
        var jsonFile = jsonConverter()
        var gsonFile = Gson().fromJson(jsonFile, Library::class.java)
        libraryList = gsonFile.radicalEntry
        var radicalList= listMaker(libraryList)
        recyclePager.adapter = LibraryAdapter(this, radicalList)
        recyclePager.layoutManager = LinearLayoutManager(this)
    }

    private fun jsonConverter():String{
        val jsonfile: String = applicationContext.assets.open("radical_library.json").bufferedReader().use{it.readText()}
        Log.e("response", jsonfile)

        return jsonfile
    }

    private fun listMaker(list:List<RadicalEntry>):MutableList<ArrayList<String>>{
        var radicalEntryList= mutableListOf<ArrayList<String>>()

        for (i in list.indices) {
            //var order = list[i].order not really needed at this stage
            var simplified = list[i].simplified
            var traditional = list[i].traditional
            var variant1 = list[i].variant1
            var variant2 = list[i].variant2
            var meaning = list[i].meaning
            var strokes = list[i].strokes.toString()
            var example1 = list[i].examples1
            var example2 = list[i].examples2
            var example3 = list[i].examples3
            var mnemonic = list[i].mnemonic
            var comments = list[i].comments

            var value = arrayListOf(simplified, traditional, variant1, variant2, meaning,strokes, example1, example2, example3, mnemonic, comments)
            radicalEntryList.add(value)
        }
        return radicalEntryList
    }
}