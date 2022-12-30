import co.pvphub.multilib.syncedVar
import java.io.Serializable

class Bruh(name: String, var value: Int) : Serializable

fun main() {
    var testValue = Bruh("mattmx", 19) syncedVar "testValue"
    testValue.modifyVar {
        this?.value = -1
    }
    println(testValue)
}