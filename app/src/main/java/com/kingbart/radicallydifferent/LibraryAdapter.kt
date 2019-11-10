package com.kingbart.radicallydifferent

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kingbart.radicallydifferent.EntryActivity
import com.kingbart.radicallydifferent.R
import kotlinx.android.synthetic.main.item_book.view.*

class LibraryAdapter(context: Context, radicalList : MutableList<ArrayList<String>>) : RecyclerView.Adapter <LibraryAdapter.ViewHolder>()   {
    var context: Context = context
    var radicalList : MutableList<ArrayList<String>> = radicalList

    override fun getItemCount(): Int { return radicalList.size }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        var view : View = view

        fun bindData (position : Int){
            view.page_item.text = radicalList[position][0]
            view.second_item.text = radicalList[position][4]
            view.setOnClickListener{
                val intent = Intent(context, EntryActivity::class.java)
                intent.putExtra("index", position)
                context.startActivity(intent)
            }
        }
    }
}