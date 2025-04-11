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

class CastVoteActivity : AppCompatActivity() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ElectionAdapter
    private lateinit var resultBtn: Button
    private val electionList = listOf(
        ElectionItem("Student Council", "Vote for student body president", "2025-04-15"),
        ElectionItem("Class Representative", "Choose your class rep", "2025-04-17"),
        ElectionItem("Cultural Secretary", "Pick who manages cultural events", "2025-04-20")
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cast_vote)

        resultBtn = findViewById<Button>(R.id.showResultBtn)
        recyclerView = findViewById(R.id.recyclerViewCastVote)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ElectionAdapter(electionList) { selectedElection ->
            val intent = Intent(this, VotingActivity::class.java)
            intent.putExtra("election_title", selectedElection.title)
            intent.putExtra("election_desc", selectedElection.description)
            intent.putExtra("election_date", selectedElection.date)
            startActivity(intent)
        }

        recyclerView.adapter = adapter

        resultBtn.setOnClickListener {
            startActivity(Intent(this, ResultActivity::class.java))
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}