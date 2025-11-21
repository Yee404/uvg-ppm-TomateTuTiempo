package com.example.tomatetutiempo.data.repository

import android.util.Log
import com.example.tomatetutiempo.data.model.Curso
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CourseRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Estado observable que contiene la lista de cursos actual
    private val _cursos = MutableStateFlow<List<Curso>>(emptyList())
    val cursos: StateFlow<List<Curso>> = _cursos.asStateFlow()

    // Referencia al listener de Firestore para poder detenerlo después
    private var snapshotListener: ListenerRegistration? = null

    private var currentUserId: String? = null

    /**
     * Detiene la escucha de cambios en Firestore y limpia los datos locales.
     * Se usa al cerrar sesión o cambiar de usuario para evitar fugas de memoria.
     */
    fun detenerListener() {
        snapshotListener?.remove()
        snapshotListener = null
        currentUserId = null
        _cursos.value = emptyList()
        Log.d("CourseRepository", "Listener detenido y datos limpiados")
    }

    /**
     * Agrega un nuevo curso a la colección del usuario actual en Firestore.
     */
    suspend fun agregarCurso(curso: Curso) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        try {
            Log.d("CourseRepository", "Intentando agregar curso: ${curso.nombre}")
            // Usamos .add() para que Firestore genere un ID único automáticamente
            firestore.collection("users").document(userId)
                .collection("cursos").add(curso).await()
            Log.d("CourseRepository", "Curso agregado con éxito")
        } catch (e: Exception) {
            Log.e("CourseRepository", "Error al agregar curso", e)
        }
    }

    /**
     * Elimina un curso específico por su ID de la colección del usuario.
     */
    suspend fun eliminarCurso(cursoId: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        try {
            // Convertimos la API de callbacks de Firebase a Corutinas
            suspendCancellableCoroutine<Unit> { continuation ->
                firestore.collection("users").document(userId)
                    .collection("cursos").document(cursoId)
                    .delete()
                    .addOnSuccessListener { continuation.resume(Unit) }
                    .addOnFailureListener { continuation.resumeWithException(it) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Activa la escucha en tiempo real para el usuario dado.
     * Gestiona la conexión y actualiza el Flow de cursos automáticamente.
     */
    fun activarListener(userId: String) {
        // Si ya estamos escuchando al mismo usuario, no hacemos nada para evitar duplicados
        if (snapshotListener != null && currentUserId == userId) {
            return
        }

        // Limpiamos cualquier listener anterior antes de crear uno nuevo
        detenerListener()

        Log.d("CourseRepository", "Iniciando listener para usuario: $userId")
        currentUserId = userId

        snapshotListener = firestore.collection("users").document(userId).collection("cursos")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("CourseRepository", "Error escuchando cursos", error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    // Mapeamos los documentos de Firestore a objetos Curso
                    val listaCursos = snapshot.documents.mapNotNull { doc ->
                        val curso = doc.toObject(Curso::class.java)
                        // Asignamos el ID del documento al objeto localmente
                        curso?.copy(id = doc.id)
                    }
                    _cursos.value = listaCursos
                }
            }
    }
}