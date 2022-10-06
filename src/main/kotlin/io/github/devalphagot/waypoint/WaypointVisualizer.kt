package io.github.devalphagot.waypoint

import net.kyori.adventure.text.Component
import org.bukkit.entity.MagmaCube
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.metadata.MetadataValue

class WaypointVisualizer {
    private fun visualizeWaypoint(){
        Main.waypoints.keys.filter { Main.fakeEntityServer.containsKey(it) }.map { Main.instance.server.getPlayer(it)!! }.forEach { p ->
            Main.waypoints[p.uniqueId]!!.forEach {
                val marker = Main.fakeEntityServer[p.uniqueId]!!.spawnEntity(
                    it.location,
                    MagmaCube::class.java
                )

                marker.bukkitEntity.isGlowing = true
                marker.bukkitEntity.isCustomNameVisible = true
                marker.bukkitEntity.customName(Component.text(it.name))
                marker.bukkitEntity.isInvulnerable = true
                marker.bukkitEntity.setMetadata("Size", FixedMetadataValue(Main.instance, 1))
            }
        }
    }

    init {
        Main.instance.server.scheduler.runTaskTimer(Main.instance, Runnable { visualizeWaypoint() }, 0L, 2L)
    }
}