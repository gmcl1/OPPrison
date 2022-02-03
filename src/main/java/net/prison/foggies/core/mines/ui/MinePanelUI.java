package net.prison.foggies.core.mines.ui;

import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.scheme.MenuPopulator;
import me.lucko.helper.menu.scheme.MenuScheme;
import me.lucko.helper.utils.Players;
import net.prison.foggies.core.OPPrison;
import net.prison.foggies.core.mines.handler.MineQueueHandler;
import net.prison.foggies.core.mines.obj.PersonalMine;
import net.prison.foggies.core.utils.Lang;
import net.prison.foggies.core.utils.Number;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.Optional;

public class MinePanelUI extends Gui {

    private final OPPrison plugin;
    private final MineQueueHandler queueHandler;
    private String targetName;
    private Optional<PersonalMine> personalMine;

    public MinePanelUI(Player player, OfflinePlayer target, OPPrison plugin) {
        super(player, 6, "&bMine Panel");
        this.plugin = plugin;
        this.queueHandler = plugin.getMineQueueHandler();
        this.personalMine = plugin.getMineStorage().get(target.getUniqueId());
        this.targetName = target.getName();
    }

    private static final MenuScheme OUTLINE = new MenuScheme()
            .mask("111111111")
            .mask("100000001")
            .mask("100000001")
            .mask("100000001")
            .mask("100000001")
            .mask("111111111");


    @Override
    public void redraw() {

        if (personalMine.isEmpty()) {
            getPlayer().closeInventory();
            return;
        }

        MenuPopulator outlinePopulator = new MenuPopulator(this, OUTLINE);
        outlinePopulator.getSlots().forEach(slot -> outlinePopulator.accept(ItemStackBuilder.of(Material.CYAN_STAINED_GLASS_PANE).buildItem().build()));

        final String symbol = "&3&l" + Lang.BLOCK_SYMBOL.getMessage();
        boolean hasAccess = personalMine.get().hasAccess(getPlayer().getUniqueId()) || getPlayer().isOp();
        String accessString = hasAccess ? "&8(&a&lACCESSABLE&8)" : "&8(&4&lNO ACCESS&8)";

        setItem(20,
                ItemStackBuilder
                        .of(Material.IRON_DOOR)
                        .name("&bVisit Mine ")
                        .lore(
                                "&7Click here to visit this mine,",
                                "&7it's owned by &b" + targetName + "&7.",
                                "",
                                symbol + "&bStatus: " + accessString
                        )
                        .enchant(Enchantment.DIG_SPEED)
                        .hideAttributes()
                        .build(() -> {
                            if (!hasAccess) {
                                Players.msg(getPlayer(), Lang.NO_MINE_ACCESS.getMessage());
                                return;
                            }
                            getPlayer().teleport(personalMine.get().getMineRegion().getSpawnPoint().toBukkitLocation());
                        })
        );

        setItem(22,
                ItemStackBuilder
                        .of(Material.DIAMOND_PICKAXE)
                        .name("&bView Mine Friends")
                        .lore(
                                "&7Click here to view the list of",
                                "&7people who have access to this mine.",
                                "",
                                symbol + "&bFriend Amount: &f" + Number.pretty(personalMine.get().getFriends().size())
                        )
                        .enchant(Enchantment.ARROW_DAMAGE)
                        .hideAttributes()
                        .build(() -> {
                            if (!hasAccess) {
                                Players.msg(getPlayer(), Lang.NO_MINE_ACCESS.getMessage());
                                return;
                            }

                            // OPEN MENU

                        })
        );

        setItem(24,
                ItemStackBuilder
                        .of(Material.DIAMOND_ORE)
                        .name("&bChange Mine Block")
                        .lore(
                                "&7Click here to change your current",
                                "&7mine block, each mine block has a different",
                                "&7sell price.",
                                "",
                                symbol + "&bCurrent Sell Price: &f$" + Number.pretty(personalMine.get().getMineBlock().getSellPrice())
                        )
                        .enchant(Enchantment.ARROW_DAMAGE)
                        .hideAttributes()
                        .build(() -> {
                            if (!hasAccess) {
                                Players.msg(getPlayer(), Lang.NO_MINE_ACCESS.getMessage());
                                return;
                            }
                            new MineBlockUI(plugin, getPlayer()).open();
                        })
        );

        setItem(30,
                ItemStackBuilder
                        .of(Material.REDSTONE_TORCH)
                        .name("&bReset Mine")
                        .lore(
                                "&7Click here to reset this mine.",
                                "",
                                symbol + "&bBlock Total: &f$" + Number.pretty(personalMine.get().getMineRegion().innerRegion().getTotalBlockSize())
                        )
                        .enchant(Enchantment.ARROW_DAMAGE)
                        .hideAttributes()
                        .build(() -> {
                            if (!hasAccess) {
                                Players.msg(getPlayer(), Lang.NO_MINE_ACCESS.getMessage());
                                return;
                            }

                            if (queueHandler.addToQueue(personalMine.get()))
                                Players.msg(getPlayer(), Lang.MINE_ADDED_TO_QUEUE.getMessage());
                            else
                                Players.msg(getPlayer(), Lang.MINE_ALREADY_IN_QUEUE.getMessage());
                        })
        );

    }
}