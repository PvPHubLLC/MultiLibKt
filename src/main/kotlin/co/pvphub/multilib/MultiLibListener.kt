package co.pvphub.multilib

import co.pvphub.multilib.util.toByteArray
import co.pvphub.multilib.util.toObject
import com.github.puregero.multilib.MultiLib
import org.bukkit.plugin.java.JavaPlugin
import java.io.Serializable
import kotlin.reflect.KClass

interface MultiLibListener {

    fun register(plugin: JavaPlugin) {
        // Get a list of all listeners in this class
        this::class.java
            .declaredMethods
            .filter { it.annotations.any { a -> a is MultiLibOn<*> } }
            .forEach {
                // Get the annotation and (possible) reply annotation
                val onAnnotation = it.annotations.first { a -> a is MultiLibOn<*> } as MultiLibOn<*>
                var repliesAnnotation = it.annotations.firstOrNull { a -> a is MultiLibReplies<*> }
                // Make sure the method's param is single and the right type we want
                if (it.parameterCount != 0)
                    throw Error("Event handler ${it.name} needs one type to get the data in the event.")
                if (it.parameterTypes[0] != onAnnotation.clazz.java)
                    throw Error(
                        "Event handler ${it.name} specifies it accepts ${onAnnotation.clazz.simpleName}, " +
                                "but accepts ${it.parameterTypes.map { c -> c.simpleName }}"
                    )
                // If we want to reply we must handle this differently
                if (repliesAnnotation != null) {
                    repliesAnnotation = repliesAnnotation as MultiLibReplies<*>
                    // If the return types don't match then throw and error
                    if (it.returnType != repliesAnnotation.clazz.java)
                        throw Error("Event handler ${it.name} specifies it returns ${repliesAnnotation.clazz.simpleName}, but returns ${it.returnType.simpleName}")
                    MultiLib.on(plugin, onAnnotation.channel) { data, reply ->
                        // Translate the data, invoke the function and accept the reply
                        val translated = data.toObject(onAnnotation.clazz.java) ?: return@on
                        val returnValue = it.invoke(null, translated)
                        reply.accept(repliesAnnotation.channel, (returnValue as Serializable).toByteArray())
                    }
                } else {
                    MultiLib.on(plugin, onAnnotation.channel) { data ->
                        val translated = data.toObject(onAnnotation.clazz.java) ?: return@on
                        it.invoke(this, translated)
                    }
                }
            }
    }

}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class MultiLibOn<T : Serializable>(val channel: String, val clazz: KClass<T>)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class MultiLibReplies<T : Serializable>(
    val channel: String,
    val clazz: KClass<T>
)