package com.example.visionapi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class TextAdapter(val itemList : ArrayList<OCRText>) : RecyclerView.Adapter<TextAdapter.Holder>(){

    //커스텀 클릭이벤트리스너
    interface onItemClickListener{
        fun onItemClick(view : View, position: Int)
    }
    private lateinit var itemListener : onItemClickListener
    fun setOnItemClickListener(onItemClickListener : onItemClickListener){
        itemListener = onItemClickListener
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tv = itemView.findViewById<TextView>(R.id.item_tv)
        init {
            itemView.setOnClickListener{
                val position = adapterPosition
                if(position!=RecyclerView.NO_POSITION){
                    itemListener.onItemClick(itemView, position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val viewLayout = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_view_item,
            parent,false)
        return Holder(viewLayout)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val currentText = itemList[position].s
        holder.tv.text = currentText


    }
}

data class OCRText(
    val s : String
)