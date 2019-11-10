package com.kingbart.radicallydifferent

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.kingbart.radicallydifferent.R

class EntryPageAdapter (var context: Context, var radicalList: MutableList<List<String>>) : PagerAdapter() {
    override fun getCount(): Int {
        return radicalList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val itemView = inflater.inflate(R.layout.radical_entry, container, false)

        val radicalView = itemView.findViewById(R.id.radical) as TextView
        val strokeView: TextView = itemView.findViewById(R.id.strokes) as TextView
        val traditionalView: TextView = itemView.findViewById(R.id.traditional) as TextView
        val variantView: TextView = itemView.findViewById(R.id.variant) as TextView
        val variant2View: TextView = itemView.findViewById(R.id.variant2) as TextView
        val meaningView: TextView = itemView.findViewById(R.id.meaning) as TextView
        val mnemonicView: TextView = itemView.findViewById(R.id.mnemonic) as TextView
        val sampleView: TextView = itemView.findViewById(R.id.example1) as TextView
        val sample2View: TextView = itemView.findViewById(R.id.example2) as TextView
        val sample3View: TextView = itemView.findViewById(R.id.example3) as TextView
        val noteView: TextView = itemView.findViewById(R.id.note) as TextView


        //individually sets the TextView setup
        radicalView.text = radicalList[position][0]
        if (radicalList[position][1] != "") { traditionalView.text = radicalList[position][1]
        } else { hide(traditionalView) }
        if (radicalList[position][2] != "") { variantView.text = radicalList[position][2]
        } else { hide(variantView) }
        if (radicalList[position][3] != "") { variant2View.text = radicalList[position][3]
        } else { hide(variant2View) }
        meaningView.text = radicalList[position][4]
        strokeView.text = radicalList[position][5]
        sampleView.text = radicalList[position][6]
        sample2View.text = radicalList[position][7]
        if (radicalList[position][8] != "") { sample3View.text = radicalList[position][8]
        } else { hide(sample3View) }
        mnemonicView.text = radicalList[position][9]
        if (radicalList[position][10] != "") { noteView.text = radicalList[position][10]
        } else { hide(noteView) }

        (container as ViewPager).addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as ScrollView)
    }

    private fun hide(view: TextView) {
        view.visibility = if (view.visibility == View.VISIBLE) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}