package xyz.perfected.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;
import xyz.perfected.config.gui.ConfigGui;
import xyz.perfected.config.value.Value;

import java.io.File;

public class ConfigApi implements Config
{
    private final String name;
    private final String version;
    private final File configFile;
    private Configuration forgeConfig;
    private ConfigHolder configHolder;

    public static Config createAndLoad(final String name, final String version, final File configFile, final ConfigHolder configHolder) {
        final ConfigApi configApi = new ConfigApi(name, version, configFile, configHolder);
        configApi.loadAllFields();
        return configApi;
    }

    private ConfigApi(final String name, final String version, final File configFile, final ConfigHolder configHolder) {
        this.name = name;
        this.version = version;
        this.configFile = configFile;
        this.configHolder = configHolder;
        this.forgeConfig = new Configuration(configFile, version);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void loadAllFields() {
        try {
            configHolder.getConfigEntries().forEach(this::loadField);
            forgeConfig.save();
        }
        catch (Exception e) {
            FMLLog.log(Level.ERROR, (Throwable)e, "Error loading configuration file!", new Object[0]);
        }
    }

    @Override
    public void saveAllFields() {
        try {
            configHolder.getConfigEntries().forEach(this::saveField);
            forgeConfig.save();
        }
        catch (Exception e) {
            FMLLog.log(Level.ERROR, (Throwable)e, "Error saving configuration file!", new Object[0]);
        }
    }

    @Override
    public void openGui() {
        TickDelayedTask.schedule(() -> Minecraft.getMinecraft().displayGuiScreen((GuiScreen)new ConfigGui(this, configHolder)));
    }

    private void loadField(final Value value) {
        if (!value.saveToFile()) {
            return;
        }
        value.loadValueFrom(forgeConfig);
    }

    private void saveField(final Value value) {
        if (!value.saveToFile()) {
            return;
        }
        value.saveValueTo(forgeConfig);
    }

    @Override
    public File getConfigFile() {
        return configFile;
    }

    public ConfigHolder getConfigHolder() {
        return configHolder;
    }
}
