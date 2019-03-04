package pw.nora.rau.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

public class m {
    public static final Minecraft c = Minecraft.getMinecraft();

    public static void error() {
        c.thePlayer.addChatMessage(CC.of("Error.")
                .setColor(EnumChatFormatting.RED)
                .build());
    }
}
