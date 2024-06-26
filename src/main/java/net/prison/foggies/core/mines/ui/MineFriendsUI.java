package net.prison.foggies.core.mines.ui;

import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.scheme.MenuPopulator;
import me.lucko.helper.menu.scheme.MenuScheme;
import net.prison.foggies.core.OPPrison;
import net.prison.foggies.core.mines.obj.PersonalMine;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class MineFriendsUI extends Gui {

    private OPPrison plugin;
    private PersonalMine personalMine;

    public MineFriendsUI(Player player, OPPrison plugin, PersonalMine personalMine) {
        super(player, 5, "&bMine Friends");
        this.plugin = plugin;
        this.personalMine = personalMine;
    }

    private static final MenuScheme OUTLINE = new MenuScheme()
            .mask("111111111")
            .mask("000000000")
            .mask("000000000")
            .mask("000000000")
            .mask("111111111");

    private static final MenuScheme FRIENDS = new MenuScheme()
            .mask("000000000")
            .mask("111111111")
            .mask("111111111")
            .mask("111111111")
            .mask("000000000");

    @Override
    public void redraw() {
        MenuPopulator outlinePopulator = new MenuPopulator(this, OUTLINE);
        MenuPopulator friendPopulator = new MenuPopulator(this, FRIENDS);
        outlinePopulator.getSlots().forEach(slot -> outlinePopulator.accept(ItemStackBuilder.of(Material.CYAN_STAINED_GLASS_PANE).buildItem().build()));



        personalMine.getFriends()
                .forEach(friend -> {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(friend);

                    ItemStack head = new ItemStack(Material.PLAYER_HEAD);
                    SkullMeta skull = (SkullMeta) head.getItemMeta();
                    skull.setOwningPlayer(offlinePlayer);
                    head.setItemMeta(skull);

                    friendPopulator.accept(
                            ItemStackBuilder
                                    .of(head)
                                    .name("&b" + offlinePlayer.getName())
                                    .enchant(Enchantment.DIG_SPEED)
                                    .hideAttributes()
                                    .buildItem().build()
                    );
                });

    }


}
