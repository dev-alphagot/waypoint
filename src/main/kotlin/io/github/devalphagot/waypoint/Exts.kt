package io.github.devalphagot.waypoint

import io.github.devalphagot.waypoint.types.Settings
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

var Player.settings: Settings
    get() {
        if(!Main.settings.containsKey(uniqueId)) Main.settings[uniqueId] = Settings()

        return Main.settings[uniqueId]!!
    }
    set(value) {
        Main.settings[uniqueId] = value
    }

val String.translated: String
    get() {
        return Main.langBundle.getString(this).replace("&", "§")
    }

fun String.toComponent(vararg extra: Component): Component {
    var comp = Component.text("${"global.prefix".translated} $this")

    extra.forEach {
        comp = comp.append(it)
    }

    return comp
}