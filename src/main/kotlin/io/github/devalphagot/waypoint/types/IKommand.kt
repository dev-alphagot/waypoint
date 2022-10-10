package io.github.devalphagot.waypoint.types

import io.github.monun.kommand.node.LiteralNode

interface IKommand {
    fun kommand(r: LiteralNode)
}