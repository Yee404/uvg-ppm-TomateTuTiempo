package com.example.tomatetutiempo.data.repository

import com.example.tomatetutiempo.data.model.Tarea
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class TaskRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _tareas = MutableStateFlow<List<Tarea>>(emptyList())
    val tareas: StateFlow<List<Tarea>> = _tareas.asStateFlow()

    init {
        getCurrentUserId()?.let { userId ->
            firestore.collection("users").document(userId).collection("tareas")
                .orderBy("fecha", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        println("Error escuchando tareas: $error")
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        val listaTareas = snapshot.documents.mapNotNull { doc ->
                            val tarea = doc.toObject(Tarea::class.java)
                            tarea?.copy(id = doc.id)
                        }
                        _tareas.value = listaTareas
                    }
                }
        }
    }

    private fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    suspend fun agregarTarea(tarea: Tarea) {
        val userId = getCurrentUserId() ?: return
        try {
            suspendCancellableCoroutine<Unit> { continuation ->
                firestore.collection("users").document(userId)
                    .collection("tareas").add(tarea)
                    .addOnSuccessListener { continuation.resume(Unit) }
                    .addOnFailureListener { continuation.resumeWithException(it) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun actualizarTarea(tareaId: String, nombre: String, horasNecesarias: Int, descripcion: String) {
        val userId = getCurrentUserId() ?: return
        try {
            val updates = mapOf(
                "nombre" to nombre,
                "horasNecesarias" to horasNecesarias,
                "descripcion" to descripcion
            )
            suspendCancellableCoroutine<Unit> { continuation ->
                firestore.collection("users").document(userId).collection("tareas").document(tareaId)
                    .update(updates)
                    .addOnSuccessListener { continuation.resume(Unit) }
                    .addOnFailureListener { continuation.resumeWithException(it) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun marcarTareaComoCompletada(tareaId: String, completada: Boolean) {
        val userId = getCurrentUserId() ?: return
        try {
            suspendCancellableCoroutine<Unit> { continuation ->
                firestore.collection("users").document(userId).collection("tareas").document(tareaId)
                    .update("completada", completada)
                    .addOnSuccessListener { continuation.resume(Unit) }
                    .addOnFailureListener { continuation.resumeWithException(it) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun eliminarTarea(tareaId: String) {
        val userId = getCurrentUserId() ?: return
        try {
            suspendCancellableCoroutine<Unit> { continuation ->
                firestore.collection("users").document(userId).collection("tareas").document(tareaId)
                    .delete()
                    .addOnSuccessListener { continuation.resume(Unit) }
                    .addOnFailureListener { continuation.resumeWithException(it) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}