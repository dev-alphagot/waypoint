package io.github.devalphagot.waypoint.command

import io.github.devalphagot.waypoint.Main
import io.github.devalphagot.waypoint.Main.Companion.waypoints
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
                        player.sendMessage("message.no-waypoint".translated.toComponent())
                        return@executes
                    }
                }
            }
        }
    }
}