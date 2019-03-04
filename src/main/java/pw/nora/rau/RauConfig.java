package pw.nora.rau;

import xyz.perfected.config.ConfigHolder;
import xyz.perfected.config.value.Value;

public class RauConfig extends ConfigHolder {
    public static final RauConfig getConfig = new RauConfig();

    private RauConfig(){
        if(getConfig != null){
            throw new IllegalStateException("Error");
        }
    }

    public Value<Boolean> prefixEnabled = Value.Builder.create(true)
            .setCategory("Toggles")
            .setDescription("Enables/disables the RAU Chat Prefix.")
            .setDisplayName("Prefix Enabled")
            .setShowInHud(true)
            .build();

    public Value<Boolean> msgPlayersEnabled = Value.Builder.create(true)
            .setCategory("Toggles")
            .setDescription("Allows you to click join messages to send welcome messages.")
            .setDisplayName("Clickable Join Messages")
            .setShowInHud(true)
            .build();

    public Value<String> chatPrefix = Value.Builder.create("&3[RAU]")
            .setCategory("Strings")
            .setDescription("RAU Chat Prefix.  Colour codes are supported through '&'\nWhen the prefix is clicked it will warn the player to be more respectful.")
            .setDisplayName("RAU Prefix")
            .setShowInHud(true)
            .build();

    public Value<String> warnMsg = Value.Builder.create("%s Please respect all users on the Hypixel Network, thank you.")
            .setCategory("Strings")
            .setDescription("Message sent when you click the RAU Prefix.\n%s = player")
            .setDisplayName("Warn Message")
            .setShowInHud(true)
            .build();

    public Value<String> welcomeMsg = Value.Builder.create("Welcome back %s! Friendly reminder to Respect All Users :)")
            .setCategory("Strings")
            .setDescription("Message sent to friends who join the server.\n%s = player")
            .setDisplayName("Welcome Message")
            .setShowInHud(true)
            .build();


}
