package fr.maxlego08.template;

import fr.maxlego08.template.command.commands.CommandTemplate;
import fr.maxlego08.template.placeholder.LocalPlaceholder;
import fr.maxlego08.template.save.Config;
import fr.maxlego08.template.save.MessageLoader;
import fr.maxlego08.template.zcore.ZPlugin;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;

/**
 * System to create your plugins very simply Projet:
 * <a href="https://github.com/Maxlego08/TemplatePlugin">https://github.com/Maxlego08/TemplatePlugin</a>
 *
 * @author Maxlego08
 */
public class Template extends ZPlugin {

    @Override
    public void onEnable() {

        // You can find the plugin id of your plugins on
        // the page https://bstats.org/what-is-my-plugin-id
        int pluginId = 9901 /* INSERT PLUGIN ID HERE */;
        Metrics metrics = new Metrics(this, pluginId);

        LocalPlaceholder placeholder = LocalPlaceholder.getInstance();
        placeholder.setPrefix("template");

        this.preEnable();

        this.registerCommand("template", new CommandTemplate(this));

        this.addSave(Config.getInstance());
        this.addSave(new MessageLoader(this));

        this.loadFiles();

        this.postEnable();
    }

    @Override
    public void onDisable() {

        this.preDisable();

        this.saveFiles();

        this.postDisable();
    }

}
