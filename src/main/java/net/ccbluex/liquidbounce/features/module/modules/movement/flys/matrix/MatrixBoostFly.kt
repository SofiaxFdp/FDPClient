package net.ccbluex.liquidbounce.features.module.modules.movement.flys.matrix

import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.flys.FlyMode
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.server.S08PacketPlayerPosLook
import kotlin.math.cos
import kotlin.math.sin


class MatrixBoostFly : FlyMode("MatrixBoost") {
    private val bypassMode = ListValue("${valuePrefix}BypassMode", arrayOf("New", "Stable", "Test", "Custom"), "New")
    private val speed = FloatValue("${valuePrefix}Speed", 2.0f, 1.0f, 3.0f)
    private val customYMotion = FloatValue("${valuePrefix}CustomJumpMotion", 0.6f, 0.2f, 5f)
    private val jumpTimer = FloatValue("${valuePrefix}JumpTimer", 0.1f, 0.1f, 2f)
    private val boostTimer = FloatValue("${valuePrefix}BoostTimer", 1f, 0.5f, 3f)
    private var boostMotion = 0

    override fun onEnable() {
        boostMotion = 0
    }
    


    override fun onUpdate(event: UpdateEvent) {
        if (boostMotion == 0) {
            val yaw = Math.toRadians(mc.thePlayer.rotationYaw.toDouble())
            mc.netHandler.addToSendQueue(
                C03PacketPlayer.C04PacketPlayerPosition(
                    mc.thePlayer.posX,
                    mc.thePlayer.posY,
                    mc.thePlayer.posZ,
                    true
                )
            )
            if (bypassMode.equals("Test")) {
                mc.netHandler.addToSendQueue(
                    C03PacketPlayer.C04PacketPlayerPosition(
                        mc.thePlayer.posX + -sin(yaw) * 1.44,
                        mc.thePlayer.posY + 1.91,
                        mc.thePlayer.posZ + cos(yaw) * 1.44,
                        false
                    )
                )
            } else {
                mc.netHandler.addToSendQueue(
                    C03PacketPlayer.C04PacketPlayerPosition(
                        mc.thePlayer.posX + -sin(yaw) * 1.5,
                        mc.thePlayer.posY + 1,
                        mc.thePlayer.posZ + cos(yaw) * 1.5,
                        false
                    )
                )
            }
            boostMotion = 1
            mc.timer.timerSpeed = jumpTimer.get()
        } else if (boostMotion == 2) {
            MovementUtils.strafe(speed.get())
            when (bypassMode.get().lowercase()) {
                "stable" -> mc.thePlayer.motionY = 0.8
                "new" -> mc.thePlayer.motionY = 0.48
                "test" -> mc.thePlayer.motionY = 1.91
                "custom" -> mc.thePlayer.motionY = customYMotion.get().toDouble()
            }
            boostMotion = 3
        } else if (boostMotion < 5) {
            boostMotion++
        } else if (boostMotion >= 5) {
            mc.timer.timerSpeed = boostTimer.get()
            if (mc.thePlayer.posY < fly.launchY - 1.0) {
                boostMotion = 0
            }
        }
    }

    override fun onDisable() {
        mc.timer.timerSpeed = 1f
    }

    override fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if(mc.currentScreen == null && packet is S08PacketPlayerPosLook) {
            mc.thePlayer.setPosition(packet.x, packet.y, packet.z)
            mc.netHandler.addToSendQueue(C03PacketPlayer.C06PacketPlayerPosLook(packet.x, packet.y, packet.z, packet.yaw, packet.pitch, false))
            if (boostMotion == 1) {
                boostMotion = 2
            }
            event.cancelEvent()
        }
    }
}
