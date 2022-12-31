package co.pvphub.multilib

import com.github.puregero.multilib.MultiLib
import com.google.gson.GsonBuilder
import org.bukkit.plugin.java.JavaPlugin
import java.io.*

/**
 * Synchronized class for MultiLib. Use this if you have variables which will change
 * and need to stay the same on every instance.
 *
 * @param plugin instance of the plugin you're making
 * @param initial initial value of the value
 * @param name of the variable
 * @param clazz instance of the variable
 */
open class SyncedVar<T : Serializable>(
    val plugin: JavaPlugin,
    val name: String,
    val clazz: Class<T>,
    initial: T?
) {
    // Gross variable to stop feedback loops of constantly trying to sync
    private var shouldSync = true
    var value = initial
        set(value) {
            field = value
            if (shouldSync)
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
                shouldSync = false
                value = global
                shouldSync = true
            }
        /**
         * We'll call this event whenever we modify the variable. This will be when we sync the variable.
         */
        MultiLib.on(plugin, "${plugin.description.name}-var-${name}") { it ->
            // Merge data
            val stream = ByteArrayInputStream(it)
            val out = ObjectInputStream(stream)
            try {
                shouldSync = false
                value = out.readObject() as T
                shouldSync = true
            } catch (_: ClassCastException) {
            }
        }
    }

    operator fun invoke() = value
    operator fun invoke(newValue: T) {
        value = newValue
    }

    /**
     * Call this manually after you change an internal variable.
     */
    fun sync() : SyncedVar<T> {
        MultiLib.getDataStorage().set("${plugin.description.name}-$name", gson.toJson(value))
        val stream = ByteArrayOutputStream()
        val objOut = ObjectOutputStream(stream)
        shouldSync = false
        val value = this.value
        objOut.writeObject(value)
        this.value = value
        shouldSync = true
        MultiLib.notify("${plugin.description.name}-var-${name}", stream.toByteArray())
        return this
    }

    /**
     * If you need the variable to be removed on a restart for example.
     */
    fun delete() {
        value = null
    }

    /**
     * DSL to modify a variable's internals and automatically send the changes to MultiLib
     *
     * @param access consumer for the changes you make
     */
    fun modifyVar(access: T?.() -> Unit) {
        shouldSync = false
        access.invoke(value)
        shouldSync = true
        sync()
    }

    override fun toString() = value.toString()

    companion object {
        val gson = GsonBuilder().create()
    }

}