package me.dolphin2410.sample

import me.dolphin2410.gamelib.abiity.ItemAbility
import io.github.monun.invfx.InvFX.frame
import io.github.monun.invfx.openFrame
import net.kyori.adventure.text.Component.text
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class XrayAbility: ItemAbility(Items.xray.clone()) {
    override fun onInit() {
        isGlobalAbility = true
    }

    override fun onClick(e: PlayerInteractEvent) {
        val frame = frame(1, text("Choose Who to Xray")) {
            list(1, 0, 7, 1, true, { Bukkit.getOnlinePlayers().toList() }) {
                transform { player ->
                    val head = ItemStack(Material.PLAYER_HEAD)
                    head.editMeta { meta ->
                        meta.displayName(player.displayName())
                        (meta as SkullMeta).owningPlayer = player
                    }
                    onClickItem { _, _, (player, _), _ ->
                        player.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 600, 1))
                        e.item!!.amount--
                    }
                    head
                }
            }.also { list ->
                slot(0, 0) {
                    item = me.dolphin2410.gamelib.Registry.MENU_PREV
                    onClick { list.index-- }
                }
                slot(8, 0) {
                    item = me.dolphin2410.gamelib.Registry.MENU_AFTER
                    onClick { list.index++ }
                }
            }
        }
        e.player.openFrame(frame)
    }
}