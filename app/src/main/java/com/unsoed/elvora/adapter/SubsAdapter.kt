package com.unsoed.elvora.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unsoed.elvora.data.response.getSubs.AllSubsriptionsItem
import com.unsoed.elvora.databinding.CardItemTransactionBinding
import com.unsoed.elvora.helper.formatDate
import com.unsoed.elvora.ui.subs.DetailTransactionActivity

class SubsAdapter(private val listSubs: List<AllSubsriptionsItem>): RecyclerView.Adapter<SubsAdapter.ViewHolder>() {
    class ViewHolder(val binding: CardItemTransactionBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: AllSubsriptionsItem) {
            binding.tvTransactionDate.text = data.createdAt?.let { formatDate(it) }
            binding.tvTransactionMotor.text = "EV${data.id}"
            binding.tvTransactionPrice.text = "Rp${data.rentType?.price}"
            binding.tvTransactionStatus.text = data.status

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailTransactionActivity::class.java)
                intent.putExtra(DetailTransactionActivity.EXTRA_DATA, data)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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