package io.github.devalphagot.waypoint.events

import io.github.devalphagot.waypoint.Main
import io.github.monun.tap.fake.FakeEntityServer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitTask
import java.util.UUID

class JoinQuit: Listener {

    @EventHandler
    fun onJoin(e: PlayerJoinEvent){
        Main.fakeEntityServer[e.player.uniqueId] = FakeEntityServer.create(Main.instance)
        Main.fakeEntityServer[e.player.uniqueId]!!.addPlayer(e.player)
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent){
        if(!Main.fakeEntityServer.containsKey(e.player.uniqueId)) return

        Main.fakeEntityServer[e.player.uniqueId]!!.clear()
        Main.fakeEntityServer[e.player.uniqueId]!!.shutdown()
        Main.fakeEntityServer.remove(e.player.uniqueId)

        Main.waypointVisualizer.markers.remove(e.player.uniqueId)
    }
}