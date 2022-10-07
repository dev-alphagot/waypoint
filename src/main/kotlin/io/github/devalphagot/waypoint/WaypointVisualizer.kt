package io.github.devalphagot.waypoint

import io.github.monun.tap.fake.FakeEntity
import net.kyori.adventure.text.Component
import org.bukkit.entity.MagmaCube
import org.bukkit.metadata.FixedMetadataValue
import java.util.*

class WaypointVisualizer {
    val markers = mutableMapOf<UUID, MutableList<Pair<Int, FakeEntity<MagmaCube>>>>()

    private fun visualizeWaypoint(){
        Main.waypoints.keys.filter { Main.fakeEntityServer.containsKey(it) }.map { Main.instance.server.getPlayer(it)!! }.forEach { p ->
            Main.waypoints[p.uniqueId]!!.forEach internalLoop@{ w ->
                if(!markers.containsKey(p.uniqueId)){
                    markers[p.uniqueId] = mutableListOf()
                    markers[p.uniqueId]!!.add(
                        Pair(
                            w.time.hashCode(),
                            Main.fakeEntityServer[p.uniqueId]!!.spawnEntity(
                                w.location.add(0.0, 2.0, 0.0),
                                MagmaCube::class.java
                            )
                        )
                    )
                }
                else if(markers[p.uniqueId]!!.none { it.first == w.time.hashCode() }) {
                    markers[p.uniqueId]!!.add(
                        Pair(
                            w.time.hashCode(),
                            Main.fakeEntityServer[p.uniqueId]!!.spawnEntity(
                                w.location.add(0.0, 2.0, 0.0),
                                MagmaCube::class.java
                            )
                        )
                    )
                }

                val marker = markers[p.uniqueId]!!.firstOrNull { it.first == w.time.hashCode() }?.second ?: return@internalLoop

                marker.bukkitEntity.isGlowing = true
                marker.bukkitEntity.isCustomNameVisible = true
                marker.bukkitEntity.customName(Component.text(w.name))
                marker.bukkitEntity.isInvulnerable = true
                marker.bukkitEntity.setMetadata("Size", FixedMetadataValue(Main.instance, 1))
                marker.rotate(0.0f, 2.0f)

                Main.fakeEntityServer[p.uniqueId]!!.update()
            }
        }
    }

    init {
        Main.instance.server.scheduler.runTaskTimer(Main.instance, Runnable { visualizeWaypoint() }, 0L, 2L)
    }
}