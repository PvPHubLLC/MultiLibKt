package co.pvphub.multilib.util

import com.github.puregero.multilib.MultiLib
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.entity.Entity

fun World.isChunkExternal(cx: Int, cz: Int) = MultiLib.isChunkExternal(this, cx, cz)
fun Location.isChunkExternal() = MultiLib.isChunkExternal(this)
fun Entity.isChunkExternal() = MultiLib.isChunkExternal(this)
fun Block.isChunkExternal() = MultiLib.isChunkExternal(this)
fun Chunk.isExternal() = MultiLib.isChunkExternal(this)

fun World.isChunkLocal(cx: Int, cz: Int) = MultiLib.isChunkLocal(this, cx, cz)
fun Location.isChunkLocal() = MultiLib.isChunkLocal(this)
fun Entity.isChunkLocal() = MultiLib.isChunkLocal(this)
fun Block.isChunkLocal() = MultiLib.isChunkLocal(this)
fun Chunk.isLocal() = MultiLib.isChunkLocal(this)