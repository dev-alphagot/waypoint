package io.github.devalphagot.waypoint.command

import io.github.devalphagot.waypoint.Main
import io.github.devalphagot.waypoint.Main.Companion.waypoints
import io.github.devalphagot.waypoint.toComponent
import io.github.devalphagot.waypoint.toComponentNoPrefix
import io.github.devalphagot.waypoint.translated
import io.github.devalphagot.waypoint.types.IKommand
import io.github.monun.kommand.Kommand.Companion.register
import io.github.monun.kommand.getValue
import io.github.monun.kommand.node.LiteralNode
import net.kyori.adventure.text.Component
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.util.*

class Trace: IKommand {
    private val traceTasks = mutableMapOf<UUID, BukkitTask>()

    override fun kommand(r: LiteralNode) {
        r.then("trace"){
            then("start"){
                executes {
                    if(!waypoints.containsKey(player.uniqueId)) {
                        player.sendMessage("message.common.no-waypoints".translated.toComponent())
                        return@executes
                    }
                    else {
                        player.sendMessage("message.common.waypoint-index".translated.format(
                            "/waypoint trace 1"
                        ).toComponent())
                        return@executes
                    }
                }
                then("index" to int(1, waypoints.maxOf { it.value.size })){
                    executes {
                        if(!waypoints.containsKey(player.uniqueId)) {
                            player.sendMessage("message.common.no-waypoints".translated.toComponent())
                            return@executes
                        }

                        val index: Int by it

                        if(waypoints[player.uniqueId]!!.size < index){
                            player.sendMessage("message.common.waypoint-over".translated.format(
                                index
                            ).toComponent())
                            return@executes
                        }

                        val wp = waypoints[player.uniqueId]!![index - 1]

                        if(traceTasks.containsKey(player.uniqueId)) traceTasks[player.uniqueId]!!.cancel()
                        traceTasks[player.uniqueId] = object: BukkitRunnable() {
                            override fun run(){
                                player.sendActionBar(
                                    "actionbar.trace.info".translated.format(
                                        wp.name,
                                        index,
                                        wp.location.distance(player.location),
                                        "global.location".translated.format(
                                            wp.location.x,
                                            wp.location.y,
                                            wp.location.z
                                        )
                                    ).toComponentNoPrefix()
                                )
                            }
                        }.runTaskTimer(Main.instance, 0L, 20L)
                        player.sendMessage("message.trace.start".translated.format(
                            wp.name
                        ).toComponent())
                    }
                }
            }
            then("stop"){
                executes {
                    if(!waypoints.containsKey(player.uniqueId)) {
                        player.sendMessage("message.common.no-waypoints".translated.toComponent())
                        return@executes
                    }

                    if(!traceTasks.containsKey(player.uniqueId)) {
                        player.sendMessage("message.trace.stop-fail".translated.toComponent())
                        return@executes
                    }

                    traceTasks[player.uniqueId]!!.cancel()
                    player.sendActionBar(Component.text(""))
                    player.sendMessage("message.trace.stop-success".translated.toComponent())
                }
            }
        }
    }
}