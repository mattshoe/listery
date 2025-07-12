package org.mattshoe.shoebox.listery.config

import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import org.mattshoe.shoebox.listery.logging.loge
import javax.inject.Inject

class RemoteConfigRepository @Inject constructor(
    private val frc: FirebaseRemoteConfig
) {

    suspend fun getString(key: String): String {
        return fetch(key) {
            frc.getString(key)
        }
    }

    fun observeString(key: String): Flow<String> {
        return callbackFlow {
            trySend(getString(key))

            val registration = frc.addOnConfigUpdateListener(
                object : ConfigUpdateListener {
                    override fun onUpdate(configUpdate: ConfigUpdate) {
                        if (configUpdate.updatedKeys.contains(key)) {
                            trySend(frc.getString(key))
                        }
                    }

                    override fun onError(error: FirebaseRemoteConfigException) {
                        loge(error)
                    }

                }
            )
            awaitClose {
                registration.remove()
            }
        }
    }

    private suspend fun <T> fetch(key: String, operation: () -> T): T {
        frc.fetchAndActivate().await()
        return operation()
    }
}