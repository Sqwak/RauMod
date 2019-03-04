package xyz.perfected.config.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import xyz.perfected.config.gui.element.GuiElement;

import java.util.List;

public class ValueScrollingList extends ScrollingList
{
    public static final int entryHeight = 15;
    public static final int paddingTop = 30;
    private final ConfigGui parent;
    private final List<GuiElement> entries;
    
    public ValueScrollingList(final ConfigGui parent, final List<GuiElement> entries) {
        super(parent.height, 30, parent.height - parent.getVerticalPadding(), parent.getHorizontalPadding(), 15, parent.width, parent.height);
        this.parent = parent;
        this.entries = entries;
    }
    
    @Override
    protected int getSize() {
        return entries.size();
    }
    
    @Override
    protected void elementClicked(final int index, final boolean doubleClick) {
        parent.selectModIndex(index);
    }
    
    @Override
    protected boolean isSelected(final int index) {
        return parent.fieldIndexSelected(index);
    }
    
    @Override
    protected void drawBackground() {
        parent.drawDefaultBackground();
    }
    
    @Override
    protected int getContentHeight() {
        return getSize() * 15 + 1;
    }
    
    @Override
    protected void drawSlot(final int idx, final int right, final int top, final int height, final Tessellator tess) {
        final GuiElement element = entries.get(idx);
        final FontRenderer font = parent.getFontRenderer();
        String name = element.getDisplayName();
        name = font.trimStringToWidth(name, getWidth() - 10);
        final boolean trimmed = name.length() != element.getDisplayName().length();
        final boolean center = element instanceof DummyElement && !trimmed;
        final int x = center ? ((getWidth() - 10 - font.getStringWidth(name)) / 2 + 3) : (getLeft() + 3);
        font.drawString(name, x, top + 2, 16777215);
    }
}
