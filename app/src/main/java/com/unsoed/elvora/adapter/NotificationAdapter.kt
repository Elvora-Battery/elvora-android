package com.unsoed.elvora.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.unsoed.elvora.data.response.notification.DataItem
import com.unsoed.elvora.databinding.CardItemNotiificationBinding
import com.unsoed.elvora.helper.formatDate

class NotificationAdapter(private val listNotification: List<DataItem>): RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    class ViewHolder(val binding: CardItemNotiificationBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataItem) {
            binding.tvNotifDate.text = "Created Notif: ${formatDate(data.createdAt.toString())}"
            binding.tvNotifDesc.text = "${data.content}"
            binding.tvNotifStatus.text = "${data.label}"
            binding.tvNotifTitle.text = data.title
            binding.tvNotifIdBattery.text = "EV${data.id}"

            itemView.setOnClickListener {
                Toast.makeText(itemView.context, "Test Click Notifikasi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardItemNotiificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listNotification.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listNotification[position]
        holder.bind(data)
    }
}