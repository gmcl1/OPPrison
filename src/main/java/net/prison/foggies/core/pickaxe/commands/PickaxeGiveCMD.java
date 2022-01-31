package net.prison.foggies.core.pickaxe.commands;

import me.lucko.helper.Commands;
import net.prison.foggies.core.OPPrison;
import net.prison.foggies.core.pickaxe.handler.PickaxeHandler;
import net.prison.foggies.core.utils.Permissions;

public class PickaxeGiveCMD {

    public PickaxeGiveCMD(OPPrison plugin) {
        final PickaxeHandler pickaxeHandler = plugin.getPickaxeHandler();

        Commands.create()
                .assertPermission(Permissions.PICKAXE_GIVE.getPermission())
                .assertUsage("<player>")
                .handler(c -> {

                })
                .register("pickaxe", "pick");

    }
}
