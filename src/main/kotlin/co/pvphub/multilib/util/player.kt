package co.pvphub.multilib.util

import co.pvphub.multilib.SyncedVar
import com.github.puregero.multilib.MultiLib
import org.bukkit.Bukkit
import org.bukkit.entity.Player

fun Player.setData(key: String, value: String) =
    MultiLib.setData(this, key, value)

fun <T> Player.setData(key: String, value: T) =
    setData(key, SyncedVar.gson.toJson(value))

fun Player.getData(key: String) =
    MultiLib.getData(this, key)

inline fun <reified T> Player.getData(key: String): T =
    SyncedVar.gson.fromJson(MultiLib.getData(this, key), T::class.java)

fun Player.setPersistent(key: String, value: String) =
    MultiLib.setPersistentData(this, key, value)

fun <T> Player.setPersistent(key: String, value: T) =
    setPersistent(key, SyncedVar.gson.toJson(value))

fun Player.getPersistent(key: String): String =
    MultiLib.getPersistentData(this, key)

inline fun <reified T> Player.getPersistent(key: String) =
    SyncedVar.gson.fromJson(getPersistent(key), T::class.java)

operator fun <T> Player.set(key: String, value: T) = set(key to false, value)

operator fun <T> Player.set(keyAndPersistent: Pair<String, Boolean>, value: T) =
    if (keyAndPersistent.second)
        setPersistent(keyAndPersistent.first, value)
    else setData(keyAndPersistent.first, value)

inline operator fun <reified T> Player.get(keyAndPersistent: Pair<String, Boolean>): T =
    if (keyAndPersistent.second)
        getPersistent<T>(keyAndPersistent.first)
    else getData<T>(keyAndPersistent.first)

fun Player.isExternalServer() = MultiLib.isExternalPlayer(this)
fun Player.getExternalServer() = MultiLib.getExternalServerName(this)
fun Player.chatOnOtherServers(message: String) = MultiLib.chatOnOtherServers(this, message)
fun Bukkit.localPlayers() = MultiLib.getLocalOnlinePlayers()
fun Bukkit.allPlayers() = MultiLib.getAllOnlinePlayers()