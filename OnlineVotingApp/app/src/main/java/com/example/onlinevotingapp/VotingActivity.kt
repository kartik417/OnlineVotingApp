package com.example.onlinevotingapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class VotingActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var candidateAdapter: CandidateAdapter
    private lateinit var databaseRef: DatabaseReference
    private lateinit var voteCountRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private val candidateList = mutableListOf(
        Candidate("John Doe", "Computer Science", 0),
        Candidate("Jane Smith", "Electrical Engineering", 0),
        Candidate("Alice Johnson", "Mechanical Engineering", 0),
        Candidate("Bob Williams", "Civil Engineering", 0)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voting)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().getReference("votes")
        voteCountRef = FirebaseDatabase.getInstance().getReference("voteCounts")

        candidateAdapter = CandidateAdapter(candidateList) { selectedCandidate ->
            showVoteConfirmation(selectedCandidate)
        }

        recyclerView.adapter = candidateAdapter

        fetchVoteCounts()
    }

    private fun fetchVoteCounts() {
        voteCountRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in candidateList.indices) {
                    val candidateName = candidateList[i].name
                    val count = snapshot.child(candidateName).getValue(Int::class.java) ?: 0
                    candidateList[i] = candidateList[i].copy(votes = count)
                }
                candidateAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@VotingActivity, "Failed to load vote counts.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showVoteConfirmation(candidate: Candidate) {
        AlertDialog.Builder(this)
            .setTitle("Confirm Vote")
            .setMessage("Are you sure you want to vote for ${candidate.name}?")
            .setPositiveButton("Yes") { _, _ ->
                saveVoteToFirebase(candidate)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun saveVoteToFirebase(candidate: Candidate) {
        val userId = auth.currentUser?.uid ?: return

        databaseRef.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(this@VotingActivity, "You have already voted!", Toast.LENGTH_SHORT).show()
                } else {
                    // Save user's vote
                    val voteMap = mapOf(
                        "name" to candidate.name,
                        "department" to candidate.department
                    )
                    databaseRef.child(userId).setValue(voteMap)
                        .addOnSuccessListener {

                            incrementVoteCount(candidate.name)
                            Toast.makeText(this@VotingActivity, "Vote cast successfully!", Toast.LENGTH_SHORT).show()
                            goToResultScreen()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this@VotingActivity, "Failed to vote. Try again.", Toast.LENGTH_SHORT).show()
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@VotingActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun incrementVoteCount(candidateName: String) {
        val voteRef = voteCountRef.child(candidateName)
        voteRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                val currentCount = currentData.getValue(Int::class.java) ?: 0
                currentData.value = currentCount + 1
                return Transaction.success(currentData)
            }

            override fun onComplete(error: DatabaseError?, committed: Boolean, snapshot: DataSnapshot?) {
                if (error != null) {
                    Toast.makeText(this@VotingActivity, "Failed to count vote", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun goToResultScreen() {
        val intent = Intent(this, ResultActivity::class.java)
        startActivity(intent)
        finish() // Prevent back navigation to voting
    }

}
