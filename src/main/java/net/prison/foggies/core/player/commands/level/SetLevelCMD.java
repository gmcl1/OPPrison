package net.prison.foggies.core.player.commands.level;

import me.lucko.helper.Commands;
import me.lucko.helper.text3.Text;
import me.lucko.helper.text3.TextComponent;
import me.lucko.helper.text3.event.HoverEvent;
import me.lucko.helper.utils.Players;
import net.prison.foggies.core.utils.Lang;
import net.prison.foggies.core.OPPrison;
import net.prison.foggies.core.utils.Permissions;
import net.prison.foggies.core.player.storage.PlayerStorage;
import net.prison.foggies.core.utils.Number;
import net.prison.foggies.core.utils.StringUtils;
import org.bukkit.entity.Player;

public class SetLevelCMD {

    public SetLevelCMD(OPPrison plugin) {
        final PlayerStorage playerStorage = plugin.getPlayerStorage();

        Commands.create()
                .assertPermission(Permissions.LEVEL_ADMIN.getPermission())
                .assertUsage("<player> <amount>")
                .handler(c -> {
                    final Player target = c.arg(0).parseOrFail(Player.class);
                    long amount = c.arg(1).parseOrFail(Long.class);

                    if (amount < 0) {
                        Players.msg(c.sender(), Lang.GREATER_THAN_OR_EQUAL_TO_0.getMessage());
                        return;
                    }

                    playerStorage.get(target.getUniqueId())
                            .whenComplete(((prisonPlayer, throwable) -> {

                                if (throwable != null) {
                                    throwable.printStackTrace();
                                    return;
                                }

                                prisonPlayer.ifPresent(pp -> pp.setLevel(amount));
                                Text.sendMessage(target,
                                        TextComponent.of(StringUtils.colorPrefix("&7Your &cLevel Data &7has been updated by an &cAdmin&7, hover for details."))
                                                .hoverEvent(HoverEvent.showText(
                                                        TextComponent.of(
                                                                StringUtils.color("&7Below is information on how your &cLevel Data" + "\n" +
                                                                        "&7has been altered: " + "\n" +
                                                                        "" + "\n" +
                                                                        "&c&l" + Lang.BLOCK_SYMBOL.getMessage() + "&fLevel Set To: " + Number.pretty(amount)
                                                                ))
                                                ))
                                );

                            }));

                })
                .register("levelset", "lset");


    }
}
