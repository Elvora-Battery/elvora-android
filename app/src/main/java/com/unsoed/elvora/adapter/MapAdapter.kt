package com.unsoed.elvora.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unsoed.elvora.data.response.map.StationsItem
import com.unsoed.elvora.databinding.CardItemMapsBinding

class MapAdapter(private val listStation: List<StationsItem>): RecyclerView.Adapter<MapAdapter.ViewHolder>() {
    private lateinit var onItemClicked: OnItemClick

    fun onItemClickCallback(onSelectedItem: OnItemClick) {
        onItemClicked = onSelectedItem
    }
    class ViewHolder(val binding: CardItemMapsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StationsItem) {
                binding.apply {
                    val formattedDistance = String.format("%.2f", data.distance)
                    tvDistanceStation.text = "$formattedDistance km from your location"
                    tvStationName.text = data.station
                    tvStationStreet.text = data.address
                    tvStationTime.text = data.status
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardItemMapsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listStation.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listStation[position]
        holder.bind(data)

        holder.itemView.setOnClickListener {
            onItemClicked.onClick(data)
        }
    }

    interface OnItemClick {
        fun onClick(data: StationsItem)
    }
}