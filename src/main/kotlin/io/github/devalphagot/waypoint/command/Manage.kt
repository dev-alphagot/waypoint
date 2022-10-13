package io.github.devalphagot.waypoint.command

import io.github.devalphagot.waypoint.Main
import io.github.devalphagot.waypoint.Main.Companion.waypoints
import io.github.devalphagot.waypoint.translated
import io.github.devalphagot.waypoint.types.IKommand
import io.github.devalphagot.waypoint.types.Waypoint
import io.github.monun.kommand.StringType
import io.github.monun.kommand.getValue
import io.github.monun.kommand.node.LiteralNode
import io.github.monun.kommand.wrapper.Position3D
import org.bukkit.Location
import java.util.*

class Manage: IKommand {


    override fun kommand(r: LiteralNode) {
        r.then("add"){
            then("name" to string(StringType.QUOTABLE_PHRASE)){
                then("location" to position()){
                    executes {
                        val name: String by it
                        val location: Position3D by it

                        val wp = Waypoint(
                            Location(player.world, location.x, location.y, location.z),
                            Date(),
                            name,
                            Waypoint.Type.NORMAL
                        )

                        if(!waypoints.containsKey(player.uniqueId)) waypoints[player.uniqueId] = mutableListOf()
                        waypoints[player.uniqueId]!!.add(wp)

                        player.sendMessage("message.add.success".translated.format(
                            wp.name
                        ))
                    }
                }
            }
        }
    }
}