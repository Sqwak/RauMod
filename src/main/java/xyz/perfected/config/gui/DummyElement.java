package xyz.perfected.config.gui;

import net.minecraft.util.EnumChatFormatting;
import xyz.perfected.config.gui.element.GuiElement;

public class DummyElement extends GuiElement
{
    private String category;
    
    public DummyElement(final String category, final ConfigGui parent) {
        super(null, parent, 0, 0);
        this.category = EnumChatFormatting.BOLD + category;
    }
    
    @Override
    public void draw(final int xElement, final int yElement, final int availWidth, final int availHeight, final int xMouse, final int yMouse) {
    }
    
    @Override
    public void keyTyped(final char c, final int keyCode) {
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
    }
    
    @Override
    public Boolean updateValue() {
        return false;
    }
    
    @Override
    public String getDisplayName() {
        return category;
    }
    
    @Override
    public String getDescription() {
        return "";
    }
    
    @Override
    public void onSelect() {
        parent.selectModIndex(-1);
    }
    
    @Override
    public int getElementX(final int x, final int availWidth) {
        return 0;
    }
    
    @Override
    public int getElementY(final int y, final int availHeight) {
        return 0;
    }
}
