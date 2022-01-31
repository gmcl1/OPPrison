package net.prison.foggies.core.player;

import net.prison.foggies.core.utils.Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class PlayerDatabase extends Database {

    public PlayerDatabase(String host, String port, String database, String userName, String password) {
        super(host, port, database, userName, password);
        this.createTable();
    }

    @Override
    public void createTable() {
        executeQuery("CREATE TABLE IF NOT EXISTS PlayerData" +
                "(" +
                "UUID VARCHAR(37), " +
                "LEVEL BIGINT, " +
                "PRESTIGE BIGINT, " +
                "LEVEL_EXPERIENCE DOUBLE, " +
                "AUTO_PRESTIGE BOOLEAN, " +
                "PRIMARY KEY (UUID)" +
                ")");
    }

    public Optional<PrisonPlayer> get(UUID uuid) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM PlayerData WHERE UUID=?")) {

            ps.setString(1, uuid.toString());
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next())
                return Optional.of(
                        new PrisonPlayer(uuid, resultSet)
                );

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public void insert(PrisonPlayer prisonPlayer) throws IOException {
        executeQuery("INSERT IGNORE INTO PlayerData VALUES(?,?,?,?,?)",
                prisonPlayer.getUUID().toString(), prisonPlayer.getLevel(), prisonPlayer.getPrestige(),
                prisonPlayer.getLevelExperience(), prisonPlayer.isAutoPrestige());
    }

    public void save(PrisonPlayer prisonPlayer) {
        executeQuery("UPDATE PlayerData SET LEVEL=?,PRESTIGE=?,LEVEL_EXPERIENCE=?,AUTO_PRESTIGE=? WHERE UUID=?",
                prisonPlayer.getLevel(), prisonPlayer.getPrestige(), prisonPlayer.getLevelExperience(),
                prisonPlayer.isAutoPrestige(), prisonPlayer.getUUID().toString());
    }

    public boolean contains(UUID uuid) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM PlayerData WHERE UUID=?")) {
            ps.setString(1, uuid.toString());

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next())
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
