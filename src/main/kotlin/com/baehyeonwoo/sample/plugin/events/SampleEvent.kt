/*
 * Copyright (c) 2022 BaeHyeonWoo
 *
 *  Licensed under the General Public License, Version 3.0. (https://opensource.org/licenses/gpl-3.0/)
 */

package com.baehyeonwoo.sample.plugin.events

import net.kyori.adventure.text.Component.text
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

/**
 * @author BaeHyeonWoo w/ xnoeyhx
 */

class SampleEvent : Listener {
    @EventHandler
    fun PlayerJoinEvent.onJoin() {
        player.sendMessage(text("Hello World!"))
    }
}