package com.invesproject.android

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize

class InvesProjectApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            Log.d("FirebaseInit", "Initializing Firebase...")
            FirebaseApp.initializeApp(this)
            Log.d("FirebaseInit", "Firebase initialized successfully")
            
            // Test Firestore connection
            val db = FirebaseFirestore.getInstance()
            db.collection("test").document("test")
                .set(mapOf("test" to "test"))
                .addOnSuccessListener {
                    Log.d("FirebaseInit", "Successfully wrote to Firestore")
                }
                .addOnFailureListener { e ->
                    Log.e("FirebaseInit", "Error writing to Firestore", e)
                }
        } catch (e: Exception) {
            Log.e("FirebaseInit", "Error initializing Firebase", e)
        }
    }
} 