import android.content.Context
import android.net.Uri
import com.example.eatsplorer.utilities.AuthManager
import com.example.eatsplorer.utilities.Recetass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.*

class FirestoreManager(context: Context) {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = AuthManager(context)
    private val storage = FirebaseStorage.getInstance()

    suspend fun addReceta(receta: Recetass, imageUri: Uri?) {
        receta.uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        if (imageUri != null) {
            val imageUrl = uploadImage(imageUri)
            receta.imagen = imageUrl
        }

        firestore.collection("recetas").add(receta).await()
    }

    suspend fun updateReceta(receta: Recetass) {
        val noteRef = receta.key?.let { firestore.collection("recetas").document(it) }
        noteRef?.set(receta)?.await()
    }

    suspend fun deleteReceta(recetaId: String) {
        val noteRef = firestore.collection("recetas").document(recetaId)
        noteRef.delete().await()
    }

    fun getNotesFlow(): Flow<List<Recetass>> = callbackFlow {
        val userId = auth.getCurrentUser()?.uid
        val notesRef = firestore.collection("recetas")
            .whereEqualTo("uid", userId)
            .orderBy("name")

        val subscription = notesRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            snapshot?.let { querySnapshot ->
                val recetas = mutableListOf<Recetass>()
                for (document in querySnapshot.documents) {
                    val receta = document.toObject(Recetass::class.java)
                    receta?.key = document.id
                    receta?.let { recetas.add(it) }
                }
                trySend(recetas).isSuccess
            }
        }
        awaitClose { subscription.remove() }
    }

    fun getPublishedRecipesFlow(): Flow<List<Recetass>> = callbackFlow {
        val notesRef = firestore.collection("recetas")
            .whereEqualTo("published", true)
            .orderBy("name")

        val subscription = notesRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            snapshot?.let { querySnapshot ->
                val recetas = mutableListOf<Recetass>()
                for (document in querySnapshot.documents) {
                    val receta = document.toObject(Recetass::class.java)
                    receta?.key = document.id
                    receta?.let { recetas.add(it) }
                }
                trySend(recetas).isSuccess
            }
        }
        awaitClose { subscription.remove() }
    }

    private suspend fun uploadImage(imageUri: Uri): String {
        val ref = storage.reference.child("images/${UUID.randomUUID()}")
        val uploadTask = ref.putFile(imageUri).await()
        return ref.downloadUrl.await().toString()
    }

    suspend fun getRecetaByKey(key: String): Recetass? {
        return try {
            val snapshot = firestore.collection("recetas").document(key).get().await()
            snapshot.toObject(Recetass::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
