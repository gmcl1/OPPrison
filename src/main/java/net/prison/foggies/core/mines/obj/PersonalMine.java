package net.prison.foggies.core.mines.obj;

import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.lucko.helper.Schedulers;
import me.lucko.helper.utils.Players;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class PersonalMine {

    private UUID mineOwner;
    private long blocksMined, mineLevel;
    private double mineExperience;
    private MineBlock mineBlock;
    private MineRegion mineRegion;
    private ArrayList<UUID> friends;
    private boolean isPublic;

    public boolean hasAccess(UUID uuid){
        return friends.contains(uuid) || mineOwner.equals(uuid);
    }

    public void giveAccess(UUID uuid){
        if(friends.contains(uuid)) return;
        friends.add(uuid);
    }

    public void removeAccess(UUID uuid){
        if(!friends.contains(uuid)) return;
        friends.remove(uuid);
    }

    public void addExperience(double amount){
        setMineExperience(getMineExperience() + amount);
    }

    public void addLevel(long amount){
        setMineLevel(getMineLevel() + amount);
    }

    public void addBlocksMined(long amount){
        setBlocksMined(getBlocksMined() + amount);
    }

    public void empty(){
        final World world = FaweAPI.getWorld(mineRegion.getPoint1().getWorld().getName());

        Schedulers.async().run(() -> {
            try(EditSession editSession = WorldEdit.getInstance().newEditSession(world)) {
                editSession.setBlocks((Region) mineRegion.toEntireCuboidRegion(), BlockTypes.AIR);
                editSession.flushQueue();
            }
        });
    }

    public void reset(){
        final World world = FaweAPI.getWorld(mineRegion.getPoint1().getWorld().getName());

        Schedulers.async().run(() -> {
            try(EditSession editSession = WorldEdit.getInstance().newEditSession(world)) {
                editSession.setBlocks((Region) mineRegion.toCuboidRegion(), BlockType.REGISTRY.get(mineBlock.getMaterial().toLowerCase()));
                editSession.flushQueue();
            }
        });

        getPlayersInMine().forEach(player -> player.teleport(mineRegion.getSpawnPoint().toBukkitLocation()));
    }

    public List<Player> getPlayersInMine(){
        return Players.stream()
                .filter(player -> mineRegion.outerRegion().contains(player))
                .collect(Collectors.toList());
    }

}
