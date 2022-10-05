package io.github.devalphagot.waypoint.types

import io.github.devalphagot.waypoint.data.DataHandler
import org.bukkit.Location
import java.util.*

data class Waypoint(
    val location: Location,
    val time: Date = Date(),
    var name: String = "<unnamed ${time.hashCode()}>",
    var type: Type
) {
    enum class Type {
        NORMAL,
        DEATH
    }

    fun wrap(): DataHandler.WaypointW {
        return DataHandler.WaypointW(
            Pair(location.world.name, Triple(
                location.x,
                location.y,
                location.z
            )),
            name,
            time,
            type.name
        )
    }
}
