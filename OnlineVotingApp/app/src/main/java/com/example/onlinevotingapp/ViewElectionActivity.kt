package com.example.onlinevotingapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ViewElectionActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ElectionAdapter

    private val electionList = listOf(
        ElectionItem("Student Council Election", "Vote for your department representative", "12th April 2025"),
        ElectionItem("General Secretary Election", "Decide the next college GS", "15th April 2025"),
        ElectionItem("Sports Captain Election", "Select your sports leader", "20th April 2025")
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_election)


        recyclerView = findViewById(R.id.recyclerViewElections)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ElectionAdapter(electionList) { selectedElection ->
            val intent = Intent(this, VotingActivity::class.java)
            intent.putExtra("election_title", selectedElection.title)
            intent.putExtra("election_desc", selectedElection.description)
            intent.putExtra("election_date", selectedElection.date)
            startActivity(intent)
        }


        recyclerView.adapter = adapter

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}