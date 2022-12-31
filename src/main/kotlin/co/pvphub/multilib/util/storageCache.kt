package co.pvphub.multilib.util

import com.github.puregero.multilib.DataStorageImpl
import com.github.puregero.multilib.util.DataStorageCache
import org.bukkit.plugin.java.JavaPlugin

fun dataStorageCache(plugin: JavaPlugin, prefix: String, dataStorageImpl: DataStorageImpl) =
    DataStorageCache(plugin, prefix, dataStorageImpl)