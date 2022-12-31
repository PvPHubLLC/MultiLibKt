package co.pvphub.multilib.dsl

import co.pvphub.multilib.util.toByteArray
import com.github.puregero.multilib.MultiLib
import org.bukkit.plugin.java.JavaPlugin
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream
import java.io.Serializable

inline fun <T : Serializable> on(
    plugin: JavaPlugin,
    name: String,
    printErrors: Boolean = false,
    crossinline callback: T.() -> Unit
) {
    MultiLib.on(plugin, name) { it ->
        val stream = ByteArrayInputStream(it)
        val out = ObjectInputStream(stream)
        try {
            val value = out.readObject() as T
            callback(value)
        } catch (e: ClassCastException) {
            if (printErrors)
                e.printStackTrace()
        }
    }
}

inline fun onString(
    plugin: JavaPlugin,
    name: String,
    crossinline callback: String.() -> Unit
) {
    MultiLib.onString(plugin, name) { it -> callback(it) }
}

inline fun <T : Serializable> replyOn(
    plugin: JavaPlugin,
    name: String,
    printErrors: Boolean = false,
    crossinline callback: T.() -> Reply<T>
) {
    MultiLib.on(plugin, name) { it, reply ->
        val stream = ByteArrayInputStream(it)
        val out = ObjectInputStream(stream)
        try {
            val value = out.readObject() as T
            val cb = callback(value)
            reply.accept(cb.name, cb.value.toByteArray())
        } catch (e: ClassCastException) {
            if (printErrors)
                e.printStackTrace()
        }
    }
}

inline fun replyOnString(
    plugin: JavaPlugin,
    name: String,
    crossinline callback: String.() -> Reply<String>
) {
    MultiLib.onString(plugin, name) { it, reply ->
        val cb = callback(it)
        reply.accept(cb.name, cb.value)
    }
}

class Reply<T : Serializable>(val name: String, val value: T)

@JvmName("on1")
inline fun <T : Serializable> JavaPlugin.on(
    name: String,
    printErrors: Boolean = false,
    crossinline callback: T.() -> Unit
) = on(this, name, printErrors, callback)

@JvmName("onString1")
inline fun JavaPlugin.onString(
    name: String,
    crossinline callback: String.() -> Unit
) = onString(this, name, callback)

@JvmName("replyOn1")
inline fun <T : Serializable> JavaPlugin.replyOn(
    name: String,
    printErrors: Boolean = false,
    crossinline callback: T.() -> Reply<T>
) = replyOn(this, name, printErrors, callback)

@JvmName("replyOnString1")
inline fun JavaPlugin.replyOnString(
    name: String,
    crossinline callback: String.() -> Reply<String>
) = replyOnString(this, name, callback)