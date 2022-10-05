package io.github.devalphagot.waypoint.events

import io.github.devalphagot.waypoint.Main.Companion.waypoints
import io.github.devalphagot.waypoint.settings
import io.github.devalphagot.waypoint.types.Waypoint
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import java.text.SimpleDateFormat
import java.util.*

class Death: Listener {
    init {}

    @EventHandler
    fun onDeath(e: PlayerDeathEvent){
        if(!e.player.settings.useDeathWaypoints) return

        if(!waypoints.containsKey(e.player.uniqueId)) waypoints[e.player.uniqueId] = mutableListOf()

        Date().let {
            waypoints[e.player.uniqueId]!!.add(
                Waypoint(
                    location = e.player.location,
                    type = Waypoint.Type.DEATH,
                    name = SimpleDateFormat("YYYY년 M월 d일 a h시 m분 s초에 사망한 지점").format(it),
                    time = it
                )
            )

            e.player.sendMessage("사망 시각, ${SimpleDateFormat("YYYY년 M월 d일 a h시 m분 s초").format(it)}입니다.")
        }
    }
}