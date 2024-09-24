package com.unsoed.elvora.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unsoed.elvora.data.Activity
import com.unsoed.elvora.databinding.ItemActivityRentalBinding

class ActivityAdapter(private val listActivity: List<Activity>): RecyclerView.Adapter<ActivityAdapter.ViewHolder>() {

    private lateinit var onItemClicked: OnItemClick

    fun onItemClickCallback(onSelectedItem: OnItemClick) {
        onItemClicked = onSelectedItem
    }

    class ViewHolder(val binding: ItemActivityRentalBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Activity) {
            binding.tvActStatus.text = data.status
            binding.tvActBattery.text = data.battery
            binding.tvActDate.text = data.date
            binding.tvActMotor.text = data.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemActivityRentalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listActivity.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listActivity[position]
        holder.bind(data)
    }

    interface OnItemClick {
        fun onClick(data: Activity)
    }
}