package co.pvphub.multilib

import com.github.puregero.multilib.MultiLib
import com.google.gson.GsonBuilder
import org.bukkit.plugin.java.JavaPlugin
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import kotlin.reflect.KProperty

/**
 * Synchronized class for MultiLib. Use this if you have variables which will change
 * and need to stay the same on every instance.
 *
 * @param plugin instance of the plugin you're making
 * @param initial initial value of the value
 * @param name of the variable
 * @param clazz instance of the variable
 */
class SyncedVar<T : Serializable>(
    val plugin: JavaPlugin = DummyPlugin(),
    initial: T?,
    val name: String,
    val clazz: Class<T>
) {
    var value = initial
        set(value) {
            field = value
            sync()
        }

    init {
        /**
         * On initialization of the variable we should see if it is already globally declared.
         * If it is then we should set our instance's value to it, otherwise we can write our value.
         */
        MultiLib.getDataStorage().get("${plugin.description.name}-$name")
            .thenAccept {
                if (it == null) {
                    MultiLib.getDataStorage().get("${plugin.description.name}-$name", gson.toJson(value))
                    return@thenAccept
                }
                val global = gson.fromJson(it, clazz)
                value = global
            }
        /**
         * We'll call this event whenever we modify the variable. This will be when we sync the variable.
         */
        MultiLib.on(plugin, "${plugin.description.name}-var-${name}") { it ->
            // Merge data
            val stream = ByteArrayInputStream(it)
            val out = ObjectInputStream(stream)
            try {
                value = out.readObject() as T
            } catch (_: ClassCastException) {
            }
        }
    }

    /**
     * Call this manually after you change an internal variable.
     */
    fun sync() {
        MultiLib.getDataStorage().get("${plugin.description.name}-$name", gson.toJson(value))
        val stream = ByteArrayOutputStream()
        val objOut = ObjectOutputStream(stream)
        objOut.writeObject(value)
        MultiLib.notify("${plugin.description.name}-var-${name}", stream.toByteArray())
    }

    /**
     * If you need the variable to be removed on a restart for example.
     */
    fun delete() {
        value = null
        sync()
    }

    /**
     * DSL to modify a variable's internals and automatically send the changes to MultiLib
     *
     * @param access consumer for the changes you make
     */
    fun modifyVar(access: T?.() -> Unit) {
        access.invoke(value)
        sync()
    }

    companion object {
        val gson = GsonBuilder().create()
    }

}