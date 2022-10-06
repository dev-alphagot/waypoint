package io.github.devalphagot.waypoint.events

import io.github.devalphagot.waypoint.Main
import io.github.monun.tap.fake.FakeEntityServer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class JoinQuit: Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent){
        Main.fakeEntityServer[e.player.uniqueId] = FakeEntityServer.create(Main.instance)
        Main.fakeEntityServer[e.player.uniqueId]!!.let {
            it.addPlayer(e.player)
            Main.instance.server.scheduler.runTaskTimer(Main.instance, it::update, 0L, 1L)
        }
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent){
        if(!Main.fakeEntityServer.containsKey(e.player.uniqueId)) return

        Main.fakeEntityServer[e.player.uniqueId]!!.shutdown()
        Main.fakeEntityServer.remove(e.player.uniqueId)
    }
}