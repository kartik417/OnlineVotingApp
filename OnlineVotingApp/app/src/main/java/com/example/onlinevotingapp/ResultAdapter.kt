package com.example.onlinevotingapp


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ResultAdapter(private val results: List<Candidate>) :
    RecyclerView.Adapter<ResultAdapter.ResultViewHolder>() {

    class ResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.resultCandidateName)
        val voteCount: TextView = view.findViewById(R.id.resultVoteCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_result_candidate, parent, false)
        return ResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val candidate = results[position]
        holder.name.text = candidate.name
        holder.voteCount.text = "Votes: ${candidate.votes}"
    }

    override fun getItemCount(): Int = results.size
}
