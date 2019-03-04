package xyz.perfected.config.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ScaledResolution
{
    private final double scaledWidthD;
    private final double scaledHeightD;
    private int scaledWidth;
    private int scaledHeight;
    private int scaleFactor;
    
    public ScaledResolution() {
        final Minecraft mc = Minecraft.getMinecraft();
        scaledWidth = mc.displayWidth;
        scaledHeight = mc.displayHeight;
        scaleFactor = 1;
        final boolean flag = mc.isUnicode();
        int i = mc.gameSettings.guiScale;
        if (i == 0) {
            i = 1000;
        }
        while (scaleFactor < i && scaledWidth / (scaleFactor + 1) >= 320 && scaledHeight / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }
        if (flag && scaleFactor % 2 != 0 && scaleFactor != 1) {
            --scaleFactor;
        }
        scaledWidthD = scaledWidth / scaleFactor;
        scaledHeightD = scaledHeight / scaleFactor;
        scaledWidth = MathHelper.ceiling_double_int(scaledWidthD);
        scaledHeight = MathHelper.ceiling_double_int(scaledHeightD);
    }
    
    public int getScaledWidth() {
        return scaledWidth;
    }
    
    public int getScaledHeight() {
        return scaledHeight;
    }
    
    public double getScaledWidth_double() {
        return scaledWidthD;
    }
    
    public double getScaledHeight_double() {
        return scaledHeightD;
    }
    
    public int getScaleFactor() {
        return scaleFactor;
    }
}
