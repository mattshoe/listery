package org.mattshoe.shoebox.listery.config

import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.Json
import org.mattshoe.shoebox.listery.logging.loge
import javax.inject.Inject

class RemoteConfigRepository @Inject constructor(
    private val frc: FirebaseRemoteConfig
) {

    fun getString(key: String): String {
        return frc.getString(key)
    }

    inline fun <reified T: Any> get(key: String): T? {
        val json = getString(key)
        return try {
            Json.decodeFromString<T>(json)
        } catch (e: Throwable) {
            loge(e)
            null
        }
    }

    inline fun <reified T: Any> observe(key: String): Flow<T?> {
        return _addListener(key) {
            get<T>(key)
        }
    }

    fun observeString(key: String): Flow<String?> {
        return _addListener(key) {
            frc.getString(key)
        }
    }

    fun <T: Any> _addListener(
        key: String,
        fetch: () -> T?
    ): Flow<T?> {
        return callbackFlow {
            trySend(fetch())

            val registration = frc.addOnConfigUpdateListener(
                object : ConfigUpdateListener {
                    override fun onUpdate(configUpdate: ConfigUpdate) {
                        if (configUpdate.updatedKeys.contains(key)) {
                            trySend(fetch())
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
}