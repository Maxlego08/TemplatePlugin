package fr.maxlego08.template.save;

import fr.maxlego08.template.zcore.enums.Message;
import fr.maxlego08.template.zcore.enums.MessageType;
import fr.maxlego08.template.zcore.logger.Logger;
import fr.maxlego08.template.zcore.utils.storage.Persist;
import fr.maxlego08.template.zcore.utils.storage.Savable;
import fr.maxlego08.template.zcore.utils.yaml.YamlUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The MessageLoader class extends YamlUtils and implements Savable to manage message configurations.
 * This class is responsible for loading and saving custom messages to a YAML file for a Bukkit plugin.
 */
public class MessageLoader extends YamlUtils implements Savable {

    private final List<Message> loadedMessages = new ArrayList<>();

    /**
     * Constructor for MessageLoader.
     *
     * @param plugin The JavaPlugin instance associated with this loader.
     */
    public MessageLoader(JavaPlugin plugin) {
        super(plugin);
    }

    /**
     * Saves messages to the configuration file.
     *
     * @param persist The persist instance used for saving the data.
     */
    @Override
    public void save(Persist persist) {

        if (persist != null) return;

        File file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        YamlConfiguration configuration = getConfig(file);
        for (Message message : Message.values()) {

            if (!message.isUse()) continue;

            String path = message.name().toLowerCase().replace("_", "-");

            if (configuration.contains(path)) continue;

            if (message.getType() != MessageType.TCHAT) {
                configuration.set(path + ".type", message.getType().name());
            }
            if (message.getType().equals(MessageType.TCHAT) || message.getType().equals(MessageType.ACTION) || message.getType().equals(MessageType.CENTER)) {

                if (message.isMessage()) {
                    if (message.getType() != MessageType.TCHAT) {
                        configuration.set(path + ".messages", colorReverse(message.getMessages()));
                    } else {
                        configuration.set(path, colorReverse(message.getMessages()));
                    }
                } else {
                    if (message.getType() != MessageType.TCHAT) {
                        configuration.set(path + ".message", colorReverse(message.getMessage()));
                    } else {
                        configuration.set(path, colorReverse(message.getMessage()));
                    }
                }
            } else if (message.getType().equals(MessageType.TITLE)) {

                configuration.set(path + ".title", colorReverse(message.getTitle()));
                configuration.set(path + ".subtitle", colorReverse(message.getSubTitle()));
                configuration.set(path + ".fadeInTime", message.getStart());
                configuration.set(path + ".showTime", message.getTime());
                configuration.set(path + ".fadeOutTime", message.getEnd());
            }
        }

        try {
            configuration.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        loadMessages(configuration);
    }

    /**
     * Loads messages from the configuration file.
     *
     * @param persist The persist instance used for loading the data.
     */
    @Override
    public void load(Persist persist) {

        File file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            this.save(null);
            return;
        }

        YamlConfiguration configuration = getConfig(file);
        this.save(null);

        loadMessages(configuration);
    }

    private void loadMessages(YamlConfiguration configuration) {

        this.loadedMessages.clear();

        for (String key : configuration.getKeys(false)) {
            loadMessage(configuration, key);
        }

        boolean canSave = false;
        for (Message message : Message.values()) {

            if (!this.loadedMessages.contains(message) && message.isUse()) {
                canSave = true;
                break;
            }
        }

        // Allows you to save new parameters
        if (canSave) {
            Logger.info("Save the message file, add new settings");
            this.save(null);
        }
    }

    /**
     * Loads a single message from the given YAML configuration.
     *
     * @param configuration The YAML configuration to load the message from.
     * @param key           The key under which the message is stored.
     */
    private void loadMessage(YamlConfiguration configuration, String key) {
        try {

            Message message = Message.valueOf(key.toUpperCase().replace("-", "_"));

            if (configuration.contains(key + ".type")) {

                MessageType messageType = MessageType.valueOf(configuration.getString(key + ".type", "TCHAT").toUpperCase());
                message.setType(messageType);
                switch (messageType) {
                    case ACTION:
                    case TCHAT_AND_ACTION: {
                        message.setMessage(configuration.getString(key + ".message"));
                        break;
                    }
                    case CENTER:
                    case TCHAT:
                    case WITHOUT_PREFIX: {
                        List<String> messages = configuration.getStringList(key + ".messages");
                        if (messages.isEmpty()) {
                            message.setMessage(configuration.getString(key + ".message"));
                        } else message.setMessages(messages);
                        break;
                    }
                    case TITLE: {
                        String title = configuration.getString(key + ".title");
                        String subtitle = configuration.getString(key + ".subtitle");
                        int fadeInTime = configuration.getInt(key + ".fadeInTime");
                        int showTime = configuration.getInt(key + ".showTime");
                        int fadeOutTime = configuration.getInt(key + ".fadeOutTime");
                        Map<String, Object> titles = new HashMap<>();
                        titles.put("title", color(title));
                        titles.put("subtitle", color(subtitle));
                        titles.put("start", fadeInTime);
                        titles.put("time", showTime);
                        titles.put("end", fadeOutTime);
                        titles.put("isUse", true);
                        message.setTitles(titles);
                        break;
                    }
                }

            } else {
                message.setType(MessageType.TCHAT);
                List<String> messages = configuration.getStringList(key);
                if (messages.isEmpty()) {
                    message.setMessage(configuration.getString(key));
                } else message.setMessages(messages);
            }

            this.loadedMessages.add(message);
        } catch (Exception ignored) {
        }
    }

}
