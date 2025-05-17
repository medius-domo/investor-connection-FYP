package com.invesproject.shared.data.repository

import com.invesproject.shared.domain.model.User
import com.invesproject.shared.domain.model.UserRole
import com.invesproject.shared.domain.repository.AuthRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore

class FirebaseAuthRepository : AuthRepository {
    private val auth = Firebase.auth
    private val firestore = Firebase.firestore
    private val usersCollection = firestore.collection("users")

    override suspend fun signUp(email: String, password: String, name: String, role: UserRole): User {
        val authResult = auth.createUserWithEmailAndPassword(email, password)
        val user = User(
            id = authResult.user?.uid ?: throw IllegalStateException("User ID not found"),
            email = email,
            name = name,
            role = role
        )
        usersCollection.document(user.id).set(user)
        return user
    }

    override suspend fun signIn(email: String, password: String): User {
        val authResult = auth.signInWithEmailAndPassword(email, password)
        val userId = authResult.user?.uid ?: throw IllegalStateException("User ID not found")
        return usersCollection.document(userId).get().data()
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser ?: return null
        return usersCollection.document(firebaseUser.uid).get().data()
    }

    override suspend fun updateUserProfile(user: User): User {
        usersCollection.document(user.id).set(user)
        return user
    }
} 