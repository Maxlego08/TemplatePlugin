package fr.maxlego08.hopper.convert;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.maxlego08.hopper.HopperBoard;
import fr.maxlego08.hopper.HopperPlugin;
import fr.maxlego08.hopper.SHopper;
import fr.maxlego08.hopper.zcore.enums.Message;
import fr.maxlego08.hopper.zcore.utils.ZUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ConvertManager extends ZUtils {

	private final HopperPlugin plugin;

	private boolean isConvert = false;

	/**
	 * @param plugin
	 */
	public ConvertManager(HopperPlugin plugin) {
		super();
		this.plugin = plugin;
	}

	public void convert(CommandSender sender) {

		if (this.isConvert) {
			message(sender, Message.CONVERT_ALREADY);
			return;
		}

		File file = new File(this.plugin.getDataFolder(), "data.db");
		if (!file.exists()) {
			message(sender, Message.CONVERT_FILE);
			return;
		}

		this.isConvert = true;
		message(sender, Message.CONVERT_START);

		Thread thread = new Thread(() -> {
			try {

				HopperBoard board = this.plugin.getHopperBoard();
				String url = "jdbc:sqlite:" + this.plugin.getDataFolder() + File.separator + "data.db";

				Connection connection = DriverManager.getConnection(url);

				PreparedStatement statement = connection.prepareStatement("select loc from Mob", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = statement.executeQuery();

				int count = 0, amount = 0;
				while (rs.next()) {
					amount++;
					String locationAsString = rs.getString("loc").replace(";", ",");

					Location location = this.changeStringLocationToLocation(locationAsString);
					SHopper hopper = new SHopper(location);
					boolean isValid;
					if (isValid = hopper.isValid()) {
						board.createHopper(hopper);
						count++;
					}

					for (Player e : Bukkit.getOnlinePlayers()) {
						e.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
								"§a" + amount + " §8/ §b" + count + " - §8(" + (isValid ? "§aOUI" : "§cNON") + "§8)"));
					}

				}

				rs.close();
				statement.close();
				connection.close();

				message(sender, Message.CONVERT_END, "%count%", count);
				
				board.save(this.plugin.getPersist());

			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		thread.start();
	}

}
