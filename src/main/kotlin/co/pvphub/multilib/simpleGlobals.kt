package co.pvphub.multilib

import org.bukkit.plugin.java.JavaPlugin
import java.io.Serializable

inline fun <reified T: Serializable> T.synced(plugin: JavaPlugin, name: String) = SyncedVar(plugin, name, T::class.java, this)