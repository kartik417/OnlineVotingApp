package com.example.onlinevotingapp

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var tvWelcome: TextView
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        tvWelcome = findViewById(R.id.tvWelcome)
        val cardViewElections = findViewById<CardView>(R.id.cardViewElections)
        val cardCastVote = findViewById<CardView>(R.id.cardCastVote)
        val cardProfile = findViewById<CardView>(R.id.cardProfile)
        val cardLogout = findViewById<CardView>(R.id.cardLogout)
        val cardResults = findViewById<CardView>(R.id.cardResults)


        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        // Show logged-in user email
        user?.let {
            tvWelcome.text = "Welcome, ${user.email}"
        }
        // Navigation
        cardViewElections.setOnClickListener {
            animateCard(cardViewElections)
            startActivity(Intent(this, ViewElectionActivity::class.java))
        }

        cardCastVote.setOnClickListener {
            // You can create CastVoteActivity and navigate here
            animateCard(cardCastVote)
            startActivity(Intent(this, ViewElectionActivity::class.java))
            finish()

        }

        cardProfile.setOnClickListener {
            // Create ProfileActivity if needed
            animateCard(cardProfile)
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }

        cardLogout.setOnClickListener {
            auth.signOut()
            animateCard(cardLogout)
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        cardResults.setOnClickListener {
            animateCard(cardResults)
            val intent = Intent(this, ResultActivity::class.java)
            startActivity(intent)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun animateCard(card: CardView) {
        val anim = ScaleAnimation(
            1f, 0.95f, // X scale from 100% to 95%
            1f, 0.95f, // Y scale from 100% to 95%
            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot X at center
            Animation.RELATIVE_TO_SELF, 0.5f  // Pivot Y at center
        )
        anim.duration = 100
        anim.fillAfter = true

        val reverseAnim = ScaleAnimation(
            0.95f, 1f,
            0.95f, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        reverseAnim.startOffset = 100
        reverseAnim.duration = 100
        reverseAnim.fillAfter = true

        val animationSet = AnimationSet(true)
        animationSet.addAnimation(anim)
        animationSet.addAnimation(reverseAnim)

        card.startAnimation(animationSet)
    }

}