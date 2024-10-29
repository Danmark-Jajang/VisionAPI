package com.example.visionapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class OCRRecyclerViewAdapter(var data : ArrayList<String>) : RecyclerView.Adapter<OCRRecyclerViewAdapter.Holder>() {
    //외부 listener 저장용
    lateinit var itemClickListener : OnItemClickListener

    interface OnItemClickListener{
        fun onClick(h: Holder , position : Int)
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
//        p0.item.setOnClickListener{
//            itemClickListener.onClick(p0, position)
//        }
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }
}