package com.example.tomatetutiempo.data.repository

import com.example.tomatetutiempo.data.model.Curso
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CourseRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    suspend fun obtenerCursos(): List<Curso> {
        val userId = getCurrentUserId() ?: return emptyList()
        return try {
            val snapshot = firestore.collection("users").document(userId)
                .collection("cursos").get().await()
            snapshot.toObjects(Curso::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun agregarCurso(curso: Curso) {
        val userId = getCurrentUserId() ?: return
        try {
            // Firestore asignará un ID si usamos .add()
            firestore.collection("users").document(userId)
                .collection("cursos").add(curso).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}