package com.unsoed.elvora.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unsoed.elvora.data.response.active.DataItem
import com.unsoed.elvora.databinding.CardItemRentalBinding
import com.unsoed.elvora.helper.formatDate
import com.unsoed.elvora.ui.rent.RentalInformationActivity

class ListRentalAdapter(private val listSubs: List<DataItem>): RecyclerView.Adapter<ListRentalAdapter.ViewHolder>() {
    class ViewHolder(val binding: CardItemRentalBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataItem) {
            binding.tvCvDatePurchase.text = "Purchased on ${formatDate(data.createdAt.toString())}"
            binding.tvCvIdBattery.text = "EV${data.id}"
            binding.tvCvEndDate.text = "Ends on ${formatDate(data.expirationDate.toString())}"
            binding.tvCvNameMotor.text = data.batteryName

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, RentalInformationActivity::class.java)
                intent.putExtra(RentalInformationActivity.EXTRA_DATA, data)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardItemRentalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listSubs.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listSubs[position]
        holder.bind(data)
    }
}