package io.github.devalphagot.waypoint.data

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.github.devalphagot.waypoint.Main
import io.github.devalphagot.waypoint.Main.Companion.waypoints
import io.github.devalphagot.waypoint.Main.Companion.settings
import io.github.devalphagot.waypoint.types.Settings
import io.github.devalphagot.waypoint.types.Waypoint
import org.bukkit.Location
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*


object DataHandler {
    data class WaypointW(
        val location: Pair<String, Triple<Double, Double, Double>>,
        var name: String,
        val time: Date,
        var type: String
    ) {
        fun unwrap(): Waypoint {
            return Waypoint(
                Location(
                    Main.instance.server.getWorld(location.first),
                    location.second.first,
                    location.second.second,
                    location.second.third
                ),
                time,
                name,
                Waypoint.Type.valueOf(type)
            )
        }
    }

    fun load() {
        val gson = GsonBuilder().setLenient().setPrettyPrinting().create()

        var files = File(Main.instance.dataFolder, "waypoints.json")

        val wwData = try {
            gson.fromJson<MutableMap<String, MutableList<WaypointW>>>(files.readText(), object: TypeToken<MutableMap<String, MutableList<WaypointW>>>(){}.type)
        }
        catch(e: FileNotFoundException){
            mutableMapOf()
        }

        val ret = mutableMapOf<UUID, MutableList<Waypoint>>()

        wwData.forEach {
            ret[UUID.fromString(it.key)] = it.value.map { it.unwrap() }.toMutableList()
        }

        files = File(Main.instance.dataFolder, "user-settings.json")

        waypoints = ret
        settings = try {
            gson.fromJson<MutableMap<String, Settings>>(files.readText(), object: TypeToken<MutableMap<String, Settings>>(){}.type).run {
                val mmap = mutableMapOf<UUID, Settings>()
                forEach {
                    mmap[UUID.fromString(it.key)] = it.value
                }

                mmap
            }
        } catch(e: FileNotFoundException){
            mutableMapOf()
        }
    }

    fun save() {
        val gson = GsonBuilder().setLenient().setPrettyPrinting().create()

        fun saveInternal(s: String) {
            fun saveWaypoint(files: File) {
                val ret = mutableMapOf<String, MutableList<WaypointW>>()

                waypoints.forEach {
                    ret[it.key.toString()] = it.value.map { it.wrap() }.toMutableList()
                }

                files.writeText(gson.toJson(ret, ret::class.java))
            }

            fun saveSettings(files: File) {
                val ret = mutableMapOf<String, Settings>()

                settings.forEach {
                    ret[it.key.toString()] = it.value
                }

                files.writeText(gson.toJson(ret, ret::class.java))
            }
            
            try {
                val files = File(Main.instance.dataFolder, s)

                (
                        when(s.split(".")[0]) {
                            "waypoints" -> ::saveWaypoint
                            "user-settings" -> ::saveSettings
                            else -> return
                        }
                )(files)
            }
            catch(_: FileNotFoundException) {
                try {
                    val files = File(Main.instance.dataFolder, s)
                    files.createNewFile()

                    (
                            when(s.split(".")[0]) {
                                "waypoint" -> ::saveWaypoint
                                "user-settings" -> ::saveSettings
                                else -> return
                            }
                    )(files)
                }
                catch(_: IOException) {
                    try {
                        Main.instance.dataFolder.mkdir()

                        val files = File(Main.instance.dataFolder, s)
                        files.createNewFile()

                        (
                                when(s.split(".")[0]) {
                                    "waypoint" -> ::saveWaypoint
                                    "user-settings" -> ::saveSettings
                                    else -> return
                                }
                        )(files)
                    }
                    catch(e: Exception) {
                        Main.instance.logger.warning("$s 저장에 실패했습니다.")
                        Main.instance.logger.warning(e.stackTraceToString())
                        return
                    }
                }
            }
            catch(e: Exception) {
                Main.instance.logger.warning("$s 저장에 실패했습니다.")
                Main.instance.logger.warning(e.stackTraceToString())
                return
            }
        }

        saveInternal("waypoints.json")
        saveInternal("user-settings.json")
    }
}