package com.example.visionapi

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class OCRRecyclerViewAdapter(private var data : ArrayList<String>) : RecyclerView.Adapter<OCRRecyclerViewAdapter.Holder>() {

    private lateinit var itemClickListener : OnItemClickListener

    interface OnItemClickListener{
        fun onClick(v: View , position : Int)
    }

    inner class Holder(view : View) : RecyclerView.ViewHolder(view){
        val textView : TextView
        val item : LinearLayout
        init {
            textView = view.findViewById(R.id.item_tv)
            item = view.findViewById(R.id.rvItem)
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.recycler_view_item, p0, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(p0: Holder, position: Int) {
        p0.textView.text = data[position]
        p0.item.setOnClickListener{
            itemClickListener.onClick(p0.itemView, position)
        }
    }

    fun setItemClickListener(onItemClickListener : OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }
}