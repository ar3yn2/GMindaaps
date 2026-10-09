package app.gmaaps.core.interfaces.source

interface DexcomBoyda {

    fun isEnabled(): Boolean
    fun requestPermissionIfNeeded()
    fun dexcomPackages(): List<String>
}
