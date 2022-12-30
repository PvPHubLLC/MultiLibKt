package co.pvphub.multilib

import java.io.Serializable

inline infix fun <reified T: Serializable> T.syncedVar(name: String) = SyncedVar(DummyPlugin(), this, name, T::class.java)