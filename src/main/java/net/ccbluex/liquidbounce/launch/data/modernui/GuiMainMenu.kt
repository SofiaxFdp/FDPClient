/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/SkidderMC/FDPClient/
 */
package net.ccbluex.liquidbounce.launch.data.modernui

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.font.FontLoaders
import net.ccbluex.liquidbounce.ui.btn.TestBtn
import net.ccbluex.liquidbounce.ui.client.GuiBackground
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager
import net.ccbluex.liquidbounce.ui.i18n.LanguageManager
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.misc.HttpUtils
import net.ccbluex.liquidbounce.utils.misc.MiscUtils
import net.minecraft.client.gui.*
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.client.GuiModList
import java.awt.Color

class GuiMainMenu : GuiScreen(), GuiYesNoCallback {
    var drawed = false
    var clicked = false
    var displayed = false
    
    fun drawBtns() {
        this.buttonList.add(
            TestBtn(
                100,
                (this.width / 2) - (130 / 2),
                this.height / 2 - 20,
                130,
                23,
                I18n.format("menu.singleplayer"),
                null,
                2,
                if (LiquidBounce.Darkmode.equals(true)) { Color(20, 20, 20, 170) } else { Color(255, 255, 255, 170) }
            )
        )
        this.buttonList.add(
            TestBtn(
                101,
                (this.width / 2) - (130 / 2),
                this.height / 2 + 10,
                130,
                23,
                I18n.format("menu.multiplayer"),
                null,
                2,
                if (LiquidBounce.Darkmode.equals(true)) { Color(20, 20, 20, 180) } else { Color(255, 255, 255, 170) }
            )
        )

        this.buttonList.add(
            TestBtn(
                200,
                (this.width / 2) - (130 / 2),
                this.height / 2 + 40,
                130,
                23,
                LanguageManager.get("ui.altmanager"),
                null,
                2,
                if (LiquidBounce.Darkmode.equals(true)) { Color(20, 20, 20, 180) } else { Color(255, 255, 255, 170) }
            )
        )

        this.buttonList.add(
            TestBtn(
                102,
                (this.width / 2) - (130 / 2),
                this.height / 2 + 70,
                130,
                23,
                LanguageManager.get("ui.mods"),
                null,
                2,
                if (LiquidBounce.Darkmode.equals(true)) { Color(20, 20, 20, 180) } else { Color(255, 255, 255, 170) }
            )
        )


        this.buttonList.add(
            TestBtn(
                104,
                this.width - 35,
                10,
                25,
                25,
                I18n.format("menu.quit"),
                if (LiquidBounce.Darkmode.equals(true)) { ResourceLocation("fdpclient/imgs/icon/quitDark.png") } else { ResourceLocation("fdpclient/imgs/icon/quit.png") },
                2,
                if (LiquidBounce.Darkmode.equals(true)) { Color(20, 20, 20, 180) } else { Color(255, 255, 255, 170) }
            )
        )

        this.buttonList.add(
            TestBtn(
                103,
                this.width - 65,
                10,
                25,
                25,
                I18n.format("menu.options").replace(".", ""),
                if (LiquidBounce.Darkmode.equals(true)) { ResourceLocation("fdpclient/imgs/icon/settingDark.png") } else { ResourceLocation("fdpclient/imgs/icon/setting.png") },
                2,
                if (LiquidBounce.Darkmode.equals(true)) { Color(20, 20, 20, 180) } else { Color(255, 255, 255, 170) }
                )
        )

        this.buttonList.add(
            TestBtn(
                201,
                this.width - 95,
                10,
                25,
                25,
                I18n.format("ui.background"),
                if (LiquidBounce.Darkmode.equals(true)) { ResourceLocation("fdpclient/imgs/icon/wallpaperDark.png") } else { ResourceLocation("fdpclient/imgs/icon/wallpaper.png") },
                2,
                if (LiquidBounce.Darkmode.equals(true)) { Color(20, 20, 20, 180) } else { Color(255, 255, 255, 170) }
            )
        )

              this.buttonList.add(
            TestBtn(
                204,
                this.width - 125,
                10,
                25,
                25,
                "Website",
                if (LiquidBounce.Darkmode.equals(true)) { ResourceLocation("fdpclient/imgs/icon/websiteDark.png") } else { ResourceLocation("fdpclient/imgs/icon/website.png") },
                2,
                if (LiquidBounce.Darkmode.equals(true)) { Color(20, 20, 20, 180) } else { Color(255, 255, 255, 170) }
            )
        )


        this.buttonList.add(
            TestBtn(
                203, this.width - 155, 10, 25, 25, "Discord", if (LiquidBounce.Darkmode.equals(true)) { ResourceLocation("fdpclient/imgs/icon/discordDark.png") } else { ResourceLocation("fdpclient/imgs/icon/discord.png") }, 2,
                if (LiquidBounce.Darkmode.equals(true)) { Color(20, 20, 20, 180) } else { Color(255, 255, 255, 170) }
            )
        )


        this.buttonList.add(
            TestBtn(
                205, 20, 10, 25, 25, "Toggle theme", if (LiquidBounce.Darkmode.equals(true)) { ResourceLocation("fdpclient/imgs/icon/moon-nightDark.png") } else { ResourceLocation("fdpclient/imgs/icon/moon-night.png") }, 2,
                if (LiquidBounce.Darkmode.equals(true)) { Color(20, 20, 20, 180) } else { Color(255, 255, 255, 170) }
            )
        )

        drawed = true
    }


    /* For modification, please keep "Designed by SkidderMC" */
    override fun initGui() {
        val defaultHeight = (this.height / 3.5).toInt()
        Thread {
            if (LiquidBounce.CLIENTTEXT.contains("Waiting") || LiquidBounce.CLIENTTEXT.contains("Oops")) {
                try {
                    LiquidBounce.CLIENTTEXT = HttpUtils.get("http://fdpinfo.github.io/changelogs")
                } catch (e: Exception) {
                    try {
                        LiquidBounce.CLIENTTEXT = HttpUtils.get("http://fdpinfo.github.io/changelogs")
                    } catch (e: Exception) {
                        LiquidBounce.CLIENTTEXT = "Oops.. :(\$Can't get information!#Try reopen the main menu\$140\$80"
                    }
                }
            }
        }.start()

        drawBtns()
        super.initGui()
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawBackground(0)
        val defaultHeight = (this.height).toFloat()
        val defaultWidth = (this.width).toFloat()
        if (LiquidBounce.Darkmode.equals(true)) { RenderUtils.drawRect(0F, 0F, defaultWidth, defaultHeight, Color(0, 0, 0, 0)) } else { RenderUtils.drawRect(0F, 0F, defaultWidth, defaultHeight, Color(0, 0, 0, 100)) }
        val i = 0
        val defaultHeight1 = (this.height).toDouble()
        val defaultWidth1 = (this.width).toDouble()
        FontLoaders.F40.drawCenteredString( LiquidBounce.CLIENT_NAME, this.width.toDouble() / 2, this.height.toDouble() / 2 - 60, Color(255, 255, 255, 200).rgb)


        FontLoaders.F16.drawString(
            LiquidBounce.CLIENT_NAME + " by SkidderMC",
            10f,
            this.height - 15f,
            Color(1, 1, 1, 170).rgb
        )
        var versionMsg =
            "Version: " + LiquidBounce.CLIENT_VERSION
        FontLoaders.F16.drawString(
            versionMsg,
            this.width - FontLoaders.F16.getStringWidth(versionMsg) - 10F,
            this.height - 15f,
            Color(1, 1, 1, 170).rgb
        )
        try {
            if (!displayed) {
                var back = Layer.draw(
                    defaultWidth.toInt(),
                    defaultHeight1.toInt(),
                    LiquidBounce.CLIENTTEXT.split("$")[2].toFloat(),
                    LiquidBounce.CLIENTTEXT.split("$")[3].toFloat(),
                    LiquidBounce.CLIENTTEXT.split("$")[0],
                    LiquidBounce.CLIENTTEXT.split("$")[1].replace("%VERSION%", LiquidBounce.CLIENT_VERSION),
                    255,
                    mouseX,
                    mouseY,
                    clicked
                )
                if (back == 1) {
                    drawed = false
                    buttonList.removeAll(buttonList)
                } else if (back == 2) {
                    displayed = true
                    drawBtns()
                }
                if (drawed && back != 1) {
                    //drawBtns()
                }
                clicked = false
            } else {
                if (!drawed) {
                    drawBtns()
                }
            }
        } catch (e: Exception) {
            //My HardDisk Exploded :(
        }
        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    override fun mouseClicked(p_mouseClicked_1_: Int, i2: Int, i3: Int) {
        clicked = true
        super.mouseClicked(p_mouseClicked_1_, i2, i3)
    }

    override fun actionPerformed(button: GuiButton) {
        when (button.id) {
            100 -> mc.displayGuiScreen(GuiSelectWorld(this))
            101 -> mc.displayGuiScreen(GuiMultiplayer(this))
            102 -> mc.displayGuiScreen(GuiModList(this))
            103 -> mc.displayGuiScreen(GuiOptions(this, mc.gameSettings))
            104 -> mc.shutdown()
            200 -> mc.displayGuiScreen(GuiAltManager(this))
            203 -> MiscUtils.showURL("https://${LiquidBounce.CLIENT_WEBSITE}/discord.html")
            204 -> MiscUtils.showURL("https://${LiquidBounce.CLIENT_WEBSITE}")
            205 -> LiquidBounce.Darkmode = !LiquidBounce.Darkmode
        }
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {}
}
