package com.example.onlinevotingapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ElectionAdapter(private val items: List<ElectionItem>,
                      private val onItemClick: (ElectionItem) -> Unit
):RecyclerView.Adapter<ElectionAdapter.ElectionViewHolder>() {
    inner class ElectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardTitle: TextView = itemView.findViewById(R.id.titleText)
        val cardDesc: TextView = itemView.findViewById(R.id.descText)
        val cardDate: TextView = itemView.findViewById(R.id.dateText)
        init {
            itemView.setOnClickListener {
                onItemClick(items[adapterPosition])
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_election_card, parent, false)
        return ElectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val item = items[position]
        holder.cardTitle.text = item.title
        holder.cardDesc.text = item.description
        holder.cardDate.text = "🗓️ ${item.date}"
    }

    override fun getItemCount(): Int = items.size
}