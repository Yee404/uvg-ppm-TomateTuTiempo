package com.example.tomatetutiempo.data.repository

import com.example.tomatetutiempo.data.model.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Calendar
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    /**
     * Obtiene usuario actual desde Firestore.
     */
    suspend fun getUserProfile(): User? {
        val currentUser = auth.currentUser ?: return null

        return try {
            val document = suspendCancellableCoroutine { continuation ->
                firestore.collection("users").document(currentUser.uid).get()
                    .addOnSuccessListener { documentSnapshot ->
                        continuation.resume(documentSnapshot)
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
            }

            if (document.exists()) {
                // toObject para convertirtodo el documento al modelo User
                document.toObject(User::class.java)
            } else {
                // Por si acaso existiera en Auth pero no en firebase
                User(uid = currentUser.uid, name = "Usuario Nuevo", email = currentUser.email ?: "")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Inicia sesión con email y contraseña.
     */
    suspend fun loginUser(email: String, password: String): AuthResult? {
        return try {
            suspendCancellableCoroutine { continuation ->
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener { authResult ->
                        continuation.resume(authResult)
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Registra un nuevo usuario y crea su documento en Firestore.
     */
    suspend fun registerUser(name: String, email: String, password: String): AuthResult? {
        return try {
            val authResult = suspendCancellableCoroutine<AuthResult> { continuation ->
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { result ->
                        continuation.resume(result)
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
            }

            authResult.user?.let { newUser ->
                val userForFirestore = User(
                    uid = newUser.uid,
                    name = name,
                    email = email,
                    gems = 0,
                    completedTasks = 0,
                    streak = 0,
                    lastCompletionDate = null
                )
                suspendCancellableCoroutine<Unit> { continuation ->
                    firestore.collection("users").document(newUser.uid).set(userForFirestore)
                        .addOnSuccessListener { continuation.resume(Unit) }
                        .addOnFailureListener { continuation.resumeWithException(it) }
                }
            }
            authResult
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    suspend fun onTaskCompleted() {
        val currentUser = auth.currentUser ?: return
        val userProfile = getUserProfile() ?: return

        val todayTimestamp = getTodayTimestamp()
        val newStreak = calculateNewStreak(userProfile.lastCompletionDate, todayTimestamp, userProfile.streak)

        val updatedStats = mapOf(
            "gems" to userProfile.gems + 300, // Añade 300 gemas
            "completedTasks" to userProfile.completedTasks + 1,
            "streak" to newStreak,
            "lastCompletionDate" to todayTimestamp
        )

        try {
            suspendCancellableCoroutine<Unit> { continuation ->
                firestore.collection("users").document(currentUser.uid)
                    .update(updatedStats)
                    .addOnSuccessListener { continuation.resume(Unit) }
                    .addOnFailureListener { continuation.resumeWithException(it) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun calculateNewStreak(lastDate: Timestamp?, todayDate: Timestamp, currentStreak: Int): Int {
        if (lastDate == null) return 1 // Primera tarea completada

        val daysDifference = getDaysDifference(lastDate.toDate().time, todayDate.toDate().time)

        return when {
            daysDifference == 0L -> currentStreak // Misma fecha, mantener racha
            daysDifference == 1L -> currentStreak + 1 // Día consecutivo, incrementar
            else -> 1 // Se rompió la racha, reiniciar a 1
        }
    }

    private fun getTodayTimestamp(): Timestamp {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }
        return Timestamp(calendar.time)
    }

    private fun getDaysDifference(date1: Long, date2: Long): Long {
        val millisecondsPerDay = 24 * 60 * 60 * 1000L
        return (date2 - date1) / millisecondsPerDay
    }

    suspend fun purchaseItem(itemId: String, cost: Int) {
        val currentUser = auth.currentUser ?: return
        val userDocRef = firestore.collection("users").document(currentUser.uid)

        try {
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(userDocRef)
                val currentGems = snapshot.getLong("gems")?.toInt() ?: 0

                if (currentGems >= cost) {
                    transaction.update(userDocRef, "gems", currentGems - cost)
                    transaction.update(userDocRef, "purchasedItems", FieldValue.arrayUnion(itemId))
                } else {
                }
            }.await() // !FUNCIÓN KTX <- revisar de primero si algo aquí no funciona :^
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}