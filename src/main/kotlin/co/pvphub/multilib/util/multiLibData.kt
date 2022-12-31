package co.pvphub.multilib.util

import co.pvphub.multilib.SyncedVar
import com.github.puregero.multilib.DataStorageImpl
import com.github.puregero.multilib.MultiLib
import java.util.concurrent.CompletableFuture

val multiLibData = MultiLib.getDataStorage()

inline operator fun <reified T> DataStorageImpl.invoke(key: String, crossinline then: T?.() -> Unit) {
    multiLibData.get(key)
        .thenAccept {
            val something = SyncedVar.gson.fromJson(it, T::class.java)
            then(something)
        }
}

inline operator fun <reified T> DataStorageImpl.invoke(key: String, value: T): CompletableFuture<String> =
    multiLibData.set(key, SyncedVar.gson.toJson(value))

operator fun DataStorageImpl.plusAssign(pair: Pair<String, String>) {
    multiLibData.add(pair.first, pair.second)
}

@JvmName("plusAssignI")
operator fun DataStorageImpl.plusAssign(pair: Pair<String, Int>) {
    multiLibData.add(pair.first, pair.second)
}

@JvmName("plusAssignL")
operator fun DataStorageImpl.plusAssign(pair: Pair<String, Long>) {
    multiLibData.add(pair.first, pair.second)
}

@JvmName("plusAssignD")
operator fun DataStorageImpl.plusAssign(pair: Pair<String, Double>) {
    multiLibData.add(pair.first, pair.second)
}