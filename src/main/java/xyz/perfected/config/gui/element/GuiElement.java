package xyz.perfected.config.gui.element;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import xyz.perfected.config.gui.ConfigGui;
import xyz.perfected.config.value.Value;

public abstract class GuiElement extends Gui
{
    protected static final FontRenderer renderer;
    protected final Value value;
    protected final ConfigGui parent;
    protected final int width;
    protected final int height;
    
    public GuiElement(final Value value, final ConfigGui parent, final int width, final int height) {
        this.value = value;
        this.parent = parent;
        this.width = width;
        this.height = height;
    }

    public GuiScreen getParent() {
        return parent;
    }

    public Value getValue() {
        return value;
    }

    public String getDescription() {
        return value.getDescription();
    }

    public abstract void draw(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5);

    protected int getMiddleX(final int xElement, final int availWidth) {
        return (int)(xElement + availWidth * 0.5);
    }

    protected int getMiddleY(final int yElement, final int availHeight) {
        return (int)(yElement + availHeight * 0.5);
    }

    public abstract void keyTyped(final char p0, final int p1);

    public abstract void mouseClicked(final int p0, final int p1, final int p2);

    public abstract int getElementX(final int p0, final int p1);

    public abstract int getElementY(final int p0, final int p1);

    public boolean wasClickOn(final int xElement, final int yElement, final int xMouse, final int yMouse) {
        return xMouse >= xElement && xMouse <= xElement + getWidth() && yMouse >= yElement && yMouse <= yElement + getHeight();
    }

    public String getDisplayName() {
        return value.getDisplayName();
    }

    public void onSelect() {
    }

    public void onReset() {
    }

    public abstract Boolean updateValue();

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    static {
        renderer = Minecraft.getMinecraft().fontRendererObj;
    }
}
