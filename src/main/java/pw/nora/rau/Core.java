package pw.nora.rau;

import pw.nora.rau.chat.RauChat;
import xyz.perfected.config.Config;
import xyz.perfected.config.ConfigApi;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "Raumod", version = "2.0", name = "RAU Mod")
public class Core {
    public static Config config;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        config = ConfigApi.createAndLoad("\u00a7nRAU Mod by Kbz and Squag","",event.getSuggestedConfigurationFile(), RauConfig.getConfig);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new RauChat());

        ClientCommandHandler.instance.registerCommand(new Command());
    }
}
