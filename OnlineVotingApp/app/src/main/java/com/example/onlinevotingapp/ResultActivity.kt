package com.example.onlinevotingapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ResultActivity : AppCompatActivity() {

    private lateinit var resultRecyclerView: RecyclerView
    private lateinit var resultAdapter: ResultAdapter
    private lateinit var databaseRef: DatabaseReference

    private val resultList = mutableListOf<Candidate>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        resultRecyclerView = findViewById(R.id.resultRecyclerView)
        resultRecyclerView.layoutManager = LinearLayoutManager(this)

        resultAdapter = ResultAdapter(resultList)
        resultRecyclerView.adapter = resultAdapter

        databaseRef = FirebaseDatabase.getInstance().getReference("voteCounts")

        fetchResults()


    }

    private fun fetchResults() {
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                resultList.clear()
                for (candidateSnapshot in snapshot.children) {
                    val name = candidateSnapshot.key ?: continue
                    val votes = candidateSnapshot.getValue(Int::class.java) ?: 0
                    resultList.add(Candidate(name, "", votes)) // department is optional
                }
                resultAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
