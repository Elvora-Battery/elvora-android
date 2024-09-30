package com.unsoed.elvora.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unsoed.elvora.databinding.ItemFilterButtonBinding

class ButtonFilterAdapter(private val listFilter: ArrayList<String>): RecyclerView.Adapter<ButtonFilterAdapter.ViewHolder>(){
    class ViewHolder(val binding: ItemFilterButtonBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: String) {
          binding.btnFilterText.text = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFilterButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listFilter.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listFilter[position]
        holder.bind(data)
    }
}