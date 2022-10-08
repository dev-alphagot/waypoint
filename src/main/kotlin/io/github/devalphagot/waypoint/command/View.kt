package io.github.devalphagot.waypoint.command

import io.github.devalphagot.waypoint.Main
import io.github.devalphagot.waypoint.Main.Companion.waypoints
import io.github.devalphagot.waypoint.formatted
import io.github.devalphagot.waypoint.toComponent
import io.github.devalphagot.waypoint.translated
import io.github.devalphagot.waypoint.types.IKommand
import io.github.monun.kommand.Kommand.Companion.register

class View: IKommand {
    override fun kommand() {
        register(Main.instance, "waypoint"){
            then("view"){
                executes {
                    if(!waypoints.containsKey(player.uniqueId)) {
                        player.sendMessage("message.view.no-waypoints".translated.toComponent())
                        return@executes
                    }

                    player.sendMessage("message.view.waypoint-exists".translated.format(waypoints[player.uniqueId]!!.size).toComponent())
                    waypoints[player.uniqueId]!!.forEachIndexed { id, it ->
                        player.sendMessage("message.view.waypoint-info".translated.format(
                            id,
                            it.name,
                            it.time.formatted,
                            it.location.x,
                            it.location.y,
                            it.location.z
                        ))
                    }
                }
            }
        }
    }
}