/*
 * Copyright (c) 2022 BaeHyeonWoo
 *
 *  Licensed under the General Public License, Version 3.0. (https://opensource.org/licenses/gpl-3.0/)
 */

package com.baehyeonwoo.sample.plugin

import com.baehyeonwoo.sample.plugin.commands.SampleKommand.register
import com.baehyeonwoo.sample.plugin.config.SampleConfig.load
import com.baehyeonwoo.sample.plugin.objects.SampleObject.event
import io.github.monun.kommand.kommand
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

/**
 * @author BaeHyeonWoo w/ xnoeyhx
 *

 */

class SamplePluginMain : JavaPlugin() {

    companion object {
        lateinit var instance: SamplePluginMain
            private set
    }

    private val configFile = File(dataFolder, "config.yml")

    override fun onEnable() {
        instance = this
        load(configFile)

        server.pluginManager.registerEvents(event, this)

        kommand {
            register("sample") {
                requires { isOp }
                register(this)
            }
        }
    }
}