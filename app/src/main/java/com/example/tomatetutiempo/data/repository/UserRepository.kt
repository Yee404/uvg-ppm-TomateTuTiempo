package com.example.tomatetutiempo.data.repository

import com.example.tomatetutiempo.data.model.User
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class UserRepository {

    // Obtenemos las instancias de Firebase directamente, sin la propiedad KTX.
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    /**
     * Obtiene el perfil del usuario actualmente logueado desde Firestore.
     */
    suspend fun getUserProfile(): User? {
        val currentUser = auth.currentUser ?: return null

        return try {
            // Envolvemos la llamada de Firebase en una corutina suspendible.
            // Esto reemplaza la función .await() de la librería KTX.
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
                User(
                    uid = currentUser.uid,
                    name = document.getString("name") ?: "Sin nombre",
                    email = document.getString("email") ?: "Sin email"
                )
            } else {
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
            // Usamos el mismo patrón para reemplazar .await()
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
                val userDocument = mapOf("name" to name, "email" to email)
                // Hacemos la escritura en Firestore
                suspendCancellableCoroutine<Unit> { continuation ->
                    firestore.collection("users").document(newUser.uid).set(userDocument)
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
}