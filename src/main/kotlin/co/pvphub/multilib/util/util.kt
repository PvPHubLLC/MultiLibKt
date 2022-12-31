package co.pvphub.multilib.util

import java.io.*
import java.lang.ClassCastException

fun Serializable.toByteArray() : ByteArray {
    val stream = ByteArrayOutputStream()
    val objOut = ObjectOutputStream(stream)
    objOut.writeObject(this)
    return stream.toByteArray()
}

fun <T : Serializable> ByteArray.toObject() : T? {
    val stream = ByteArrayInputStream(this)
    val out = ObjectInputStream(stream)
    return try {
        out.readObject() as T
    } catch (_: ClassCastException) {
        null
    }
}

fun <T : Serializable> ByteArray.toObject(clazz: Class<T>) : T? {
    val stream = ByteArrayInputStream(this)
    val out = ObjectInputStream(stream)
    return try {
        val value = out.readObject()
        clazz.cast(value)
    } catch (_: ClassCastException) {
        null
    }
}