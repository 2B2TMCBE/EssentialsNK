package cn.yescallop.essentialsnk;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockIgniteEvent;
import cn.nukkit.event.block.BlockIgniteEvent.BlockIgniteCause;
import cn.nukkit.event.entity.EntityDeathEvent;
import cn.nukkit.event.entity.EntityRegainHealthEvent;
import cn.nukkit.event.player.*;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.yescallop.essentialsnk.lang.BaseLang;

public class EventListener implements Listener {

    EssentialsAPI api;
    BaseLang lang;

    public EventListener(EssentialsAPI api) {
        this.api = api;
        lang = api.getLanguage();
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        api.setLastLocation(event.getPlayer(), event.getFrom());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        Block bed = event.getBed();
        api.setHome(event.getPlayer(), "bed", Location.fromObject(bed, bed.level, 0, 0));
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (Player p : api.getServer().getOnlinePlayers().values()) {
            if (api.isVanished(p)) {
                player.hidePlayer(p);
            }
            if (api.isVanished(player)) {
                p.hidePlayer(player);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        api.removeTPRequest(event.getPlayer());
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        if (api.isMuted(player)) {
            event.setCancelled();
            player.sendMessage(lang.translateString("commands.generic.muted", api.getUnmuteTimeMessage(player)));
        }
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (api.isMuted(player)) {
            event.setCancelled();
            player.sendMessage(lang.translateString("commands.generic.muted", api.getUnmuteTimeMessage(player)));
        }
    }
}