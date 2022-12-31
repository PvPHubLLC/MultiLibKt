package co.pvphub.multilib

import co.pvphub.multilib.util.toByteArray
import com.github.puregero.multilib.MultiLib

/**
 * Example listener class apart from the MultiLibKt library.
 * We can register this in the main class, [ExamplePlugin]
 */
class ExampleListener : MultiLibListener {

    /**
     * This is basically a listener that calls another straight away, useless.
     * But you can see it will activate the function [handleEvent], which replies
     * with a String value
     *
     * @param value some value to do stuff with
     */
    @MultiLibOn<Int>("hello_world::execute", Int::class)
    fun helloWorldExec(value: Int) {
        MultiLib.notify("hello_world", "i have travelled long and far, $value".toByteArray())
    }

    /**
     * This function will basically return a value for another event handler to handle.
     *
     * @param value as a string
     */
    @MultiLibOn<Int>("hello_world", Int::class)
    @MultiLibReplies<String>("hello_world::reply", String::class)
    fun handleEvent(value: String) : String {
        println("Currently at function 'handleEvent'")
        return "Alright I get it, the value is $value!"
    }

    /**
     * The last stop of this little journey, will simply print the contents of [value]
     *
     * @param value some string that means something
     */
    @MultiLibOn<String>("hello_world::reply", String::class)
    fun handleReply(value: String) {
        println("Thanks for the reply: '$value'")
    }

}