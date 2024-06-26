package net.prison.foggies.core.pickaxe.enchants;

import me.lucko.helper.utils.Players;
import net.prison.foggies.core.OPPrison;
import net.prison.foggies.core.mines.obj.PersonalMine;
import net.prison.foggies.core.pickaxe.model.EnchantBase;
import net.prison.foggies.core.pickaxe.obj.PlayerPickaxe;
import net.prison.foggies.core.player.constant.SettingType;
import net.prison.foggies.core.player.obj.PrisonPlayer;
import net.prison.foggies.core.player.obj.Setting;
import net.prison.foggies.core.utils.Lang;
import net.prison.foggies.core.utils.Number;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class Producer extends EnchantBase {

    @Override
    public String getColor() {
        return "&e";
    }

    @Override
    public String getSymbol() {
        return Lang.BLOCK_SYMBOL.getMessage();
    }

    @Override
    public String getDisplayName() {
        return getColor() + "&l" + getSymbol() + getColor() + "Producer";
    }

    @Override
    public String getMenuDisplayName() {
        return getColor() + "&lProducer";
    }

    @Override
    public String getIdentifier() {
        return "PRODUCER";
    }

    @Override
    public long getStartLevel() {
        return 100;
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>(
                Arrays.asList(
                        "&7Chance to find huge bursts of tokens",
                        "&7while mining."
                )
        );
    }

    @Override
    public long getMaxLevel() {
        return 10000;
    }

    @Override
    public long getAdminMaxLevel() {
        return Long.MAX_VALUE;
    }

    @Override
    public float getChance() {
        return 0.5F;
    }

    @Override
    public double getBasePrice() {
        return 25000000;
    }

    @Override
    public double getCost(long amount) {
        return getBasePrice() * amount;
    }

    @Override
    public void handle(OPPrison plugin, PrisonPlayer prisonPlayer, PlayerPickaxe playerPickaxe, PersonalMine personalMine, BlockBreakEvent e) {
        Player player = e.getPlayer();
        final Optional<Setting> tokenMinerSetting = prisonPlayer.getSetting(SettingType.TOKEN_MINER);

        long level = playerPickaxe.getLevel(getIdentifier());
        long tokens = ThreadLocalRandom.current().nextLong(level * 25);

        prisonPlayer.addTokens(tokens, false);

        if (tokenMinerSetting.isPresent() && tokenMinerSetting.get().isToggled())
            Players.msg(player, "&b&lPRODUCER &7Found &b" + Number.formatted(tokens) + "&7 Tokens.");
    }
}
