package com.example.onlinevotingapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class CandidateAdapter(
    private val candidates: List<Candidate>,
    private val onVoteClick: ((Candidate) -> Unit)? = null
) : RecyclerView.Adapter<CandidateAdapter.CandidateViewHolder>() {

    class CandidateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.cardView)
        val nameText: TextView = view.findViewById(R.id.textViewName)
        val departmentText: TextView = view.findViewById(R.id.textViewDepartment)
        val voteCountTextView: TextView = view.findViewById(R.id.textViewVotes) // Make sure this exists in item layout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_candidate, parent, false)
        return CandidateViewHolder(view)
    }

    override fun onBindViewHolder(holder: CandidateViewHolder, position: Int) {
        val candidate = candidates[position]
        holder.nameText.text = candidate.name
        holder.departmentText.text = candidate.department
        holder.voteCountTextView.text = "Votes: ${candidate.votes}"

        if (onVoteClick != null) {
            holder.cardView.setOnClickListener {
                onVoteClick(candidate)
            }
        } else {
            holder.cardView.setOnClickListener(null)
        }
    }

    override fun getItemCount(): Int = candidates.size
}
