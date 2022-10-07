package io.github.devalphagot.waypoint

import io.github.devalphagot.waypoint.data.DataHandler
import io.github.devalphagot.waypoint.types.Settings
import io.github.devalphagot.waypoint.types.Waypoint
import io.github.monun.tap.fake.FakeEntityServer
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections
import java.io.File
import java.util.*

/**
 * @author BaeHyeonWoo w/ xnoeyhx
 *

 */

class Main : JavaPlugin() {

    companion object {
        lateinit var instance: Main
            private set

        lateinit var waypoints: MutableMap<UUID, MutableList<Waypoint>>

        lateinit var settings: MutableMap<UUID, Settings>

        lateinit var waypointVisualizer: WaypointVisualizer

        val fakeEntityServer: MutableMap<UUID, FakeEntityServer> = mutableMapOf()
    }

    override fun onEnable() {
        instance = this
        DataHandler.load()

        val reflections = Reflections("io.github.devalphagot.waypoint.events")

        reflections.getSubTypesOf(
            Listener::class.java
        )?.forEach { clazz ->
            logger.info(clazz.name)

            clazz.getDeclaredConstructor().trySetAccessible()
            server.pluginManager.registerEvents(clazz.getDeclaredConstructor().newInstance(), this)
        }

        waypointVisualizer = WaypointVisualizer()
    }

    override fun onDisable() {
        DataHandler.save()
    }
}