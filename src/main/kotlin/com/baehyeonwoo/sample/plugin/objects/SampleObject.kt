/*
 * Copyright (c) 2022 BaeHyeonWoo
 *
 *  Licensed under the General Public License, Version 3.0. (https://opensource.org/licenses/gpl-3.0/)
 */

package com.baehyeonwoo.sample.plugin.objects

import com.baehyeonwoo.sample.plugin.SamplePluginMain
import com.baehyeonwoo.sample.plugin.events.SampleEvent
import org.bukkit.event.Listener

/**
 * @author BaeHyeonWoo w/ xnoeyhx
 */

@Suppress("UNUSED", "MemberVisibilityCanBePrivate") // REMOVE SUPPRESS WHEN USING!
object SampleObject {
    val plugin = SamplePluginMain.instance
    val server = plugin.server
    val event: Listener by lazy { SampleEvent() }
}