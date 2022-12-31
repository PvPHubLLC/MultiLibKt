package co.pvphub.multilib

import co.pvphub.multilib.dsl.synced
import co.pvphub.multilib.util.color
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.plugin.java.JavaPlugin

class ExamplePlugin : JavaPlugin() {
    var syncedVariable = "&cThis value is synchronized across the MultiPaper server.".synced(this, "example.syncedVar")

    override fun onEnable() {
        instance = this
        Bukkit.getPluginCommand("multi-lib-example-var")?.setExecutor(ExampleCommand())
        ExampleListener(this).register(this)
    }

    companion object {
        lateinit var instance: ExamplePlugin
    }

}

class ExampleCommand : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isNotEmpty()) {
            when (args[0].lowercase()) {
                "set" -> {
                    if (args.size <= 1) {
                        sender.sendMessage("&cProvide a string to rename the global variable.".color())
                        return false
                    }
                    val compiled = args.toList().subList(1, args.size).joinToString(" ")
                    ExamplePlugin.instance.syncedVariable(compiled)
                    sender.sendMessage("&aChanged global variable's value to '$compiled&a'".color())
                    sender.sendMessage("&aHead to another backend server to check it out.".color())
                }
                "get" -> {
                    sender.sendMessage("&aGlobal variable is currently '${ExamplePlugin.instance.syncedVariable}&a'".color())
                }
            }
        }
        return false
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): MutableList<String>? {
        if (args.size <= 1) {
            return listOf("set", "get")
                .filter { it.startsWith(args[args.size - 1]) }
                .toMutableList()
        }
        return null
    }

}