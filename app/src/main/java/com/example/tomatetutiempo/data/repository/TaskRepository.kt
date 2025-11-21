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

/**
 * Repositorio encargado de gestionar las operaciones de datos de las Tareas
 * con Firebase Firestore.
 */
class TaskRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Estado observable para la lista de tareas
    private val _tareas = MutableStateFlow<List<Tarea>>(emptyList())
    val tareas: StateFlow<List<Tarea>> = _tareas.asStateFlow()

    /**
     * Bloque de inicialización que configura el listener en tiempo real
     * para las tareas del usuario actual al instanciar el repositorio.
     */
    init {
        getCurrentUserId()?.let { userId ->
            // Escuchamos la colección "tareas" ordenadas por fecha ascendente
            firestore.collection("users").document(userId).collection("tareas")
                .orderBy("fecha", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        println("Error escuchando tareas: $error")
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        // Mapeamos los documentos a objetos Tarea y asignamos el ID
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

    /**
     * Agrega una nueva tarea a la colección del usuario en Firestore.
     */
    suspend fun agregarTarea(tarea: Tarea) {
        val userId = getCurrentUserId() ?: return
        try {
            // Convertimos el callback de Firebase a Corutina
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

    /**
     * Actualiza los detalles principales (nombre, horas, descripción) de una tarea existente.
     */
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

    /**
     * Actualiza el estado de finalización de una tarea específica.
     */
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

    /**
     * Elimina permanentemente una tarea de Firestore.
     */
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