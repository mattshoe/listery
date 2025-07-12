package org.mattshoe.shoebox.listery.data.util

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

inline fun <reified T> Query.asFlow(
    crossinline onError:  ProducerScope<List<T>>.(FirebaseFirestoreException) -> Unit = {}
): Flow<List<T>> {
    return callbackFlow {
        addSnapshotListener { snapshot, error ->
            if (error != null) {
                onError(error)
            } else {
                val items = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(T::class.java)
                } ?: emptyList()

                trySend(items)
            }
        }.apply {
            awaitClose {
                remove()
            }
        }
    }
}

inline fun <reified T> DocumentReference.asFlow(
    crossinline onError:  ProducerScope<T>.(FirebaseFirestoreException) -> Unit = {}
): Flow<T?> {
    return callbackFlow {
        addSnapshotListener { snapshot, error ->
            if (error != null) {
                onError(error)
            } else {
                trySend(
                    snapshot?.toObject(T::class.java)
                )
            }
        }.apply {
            awaitClose {
                remove()
            }
        }
    }
}