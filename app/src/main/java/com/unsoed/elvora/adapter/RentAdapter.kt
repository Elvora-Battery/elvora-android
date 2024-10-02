package com.unsoed.elvora.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unsoed.elvora.data.Rental
import com.unsoed.elvora.databinding.ItemBatteryRentalBinding

class RentAdapter(private val listRent: List<Rental>): RecyclerView.Adapter<RentAdapter.ViewHolder>() {
    private lateinit var onItemClicked: OnItemClick
    private lateinit var onDetailItemClicked: OnItemClick

    fun onItemClickCallback(onSelectedItem: OnItemClick) {
        onItemClicked = onSelectedItem
    }

    fun onDetailItemClickCallback(onSelectedItem: OnItemClick) {
        onDetailItemClicked = onSelectedItem
    }

    class ViewHolder(val binding: ItemBatteryRentalBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Rental) {
            binding.tvBatteryPrice.text = data.price
            binding.tvBatteryType.text = data.type
            binding.tvBatteryDesc.text = data.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBatteryRentalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listRent.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listRent[position]
        holder.bind(data)
        holder.binding.btnRent.setOnClickListener {
            onItemClicked.onClick(data)
        }

        holder.binding.btnDesc.setOnClickListener {
            onDetailItemClicked.onClick(data)
        }
    }
    interface OnItemClick {
        fun onClick(data: Rental)
    }
}