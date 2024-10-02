package com.unsoed.elvora.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unsoed.elvora.data.response.transaction.DataItem
import com.unsoed.elvora.databinding.ItemActivityRentalBinding
import com.unsoed.elvora.helper.formatDate
import com.unsoed.elvora.ui.detail.DetailActivity

class ActivityAdapter(private val listActivity: List<DataItem>): RecyclerView.Adapter<ActivityAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemActivityRentalBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataItem) {
            binding.tvActStatus.text = data.status
            binding.tvActBattery.text = if(data.rentTypeId == 1) "72V 20Ah Battery" else "72V 40Ah Battery"
            binding.tvActDate.text = "Transaction on ${formatDate(data.createdAt.toString())}"
            binding.tvActMotor.text = "ID Battery:EV${data.rentTypeId}"

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_ID, data.id)
                itemView.context.startActivity(intent)
            }
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

}