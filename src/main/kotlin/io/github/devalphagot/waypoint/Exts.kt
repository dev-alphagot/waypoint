package io.github.devalphagot.waypoint

import io.github.devalphagot.waypoint.types.Settings
import org.bukkit.entity.Player

var Player.settings: Settings
    get() {
        if(!Main.settings.containsKey(uniqueId)) Main.settings[uniqueId] = Settings()

        return Main.settings[uniqueId]!!
    }
    set(value) {
        Main.settings[uniqueId] = value
    }