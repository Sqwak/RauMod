package xyz.perfected.config.gui;

import net.minecraft.client.gui.GuiButton;

public class Buttons
{
    public static class Save extends GuiButton
    {
        public Save(final int x, final int y) {
            this(x, y, 40, 20);
        }
        
        public Save(final int x, final int y, final int buttonWidth, final int buttonHeight) {
            super(0, x, y, buttonWidth, buttonHeight, "Save");
        }
    }
    
    public static class Reset extends GuiButton
    {
        public Reset(final int x, final int y) {
            this(x, y, 40, 20);
        }
        
        public Reset(final int x, final int y, final int buttonWidth, final int buttonHeight) {
            super(0, x, y, buttonWidth, buttonHeight, "Reset");
        }
    }
    
    public static class Cancel extends GuiButton
    {
        public Cancel(final int x, final int y) {
            this(x, y, 40, 20);
        }
        
        public Cancel(final int x, final int y, final int buttonWidth, final int buttonHeight) {
            super(0, x, y, buttonWidth, buttonHeight, "Cancel");
        }
    }
}
