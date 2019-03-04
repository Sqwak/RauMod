package xyz.perfected.config.gui.element;

import xyz.perfected.config.gui.ConfigGui;
import xyz.perfected.config.util.Polygon;
import xyz.perfected.config.value.Value;

public class BoolElement extends GuiElement
{
    private static final int size = 20;
    private boolean state;
    private int xButton;
    private int yButton;
    
    public BoolElement(final Value value, final ConfigGui parent) {
        super(value, parent, 20, 20);
        state = (boolean) value.get();
    }
    
    @Override
    public void draw(final int xElement, final int yElement, final int availWidth, final int availHeight, final int xMouse, final int yMouse) {
        xButton = getElementX(xElement, availWidth);
        yButton = getElementY(yElement, availHeight);
        drawRect(xButton - 1, yButton - 1, xButton + width + 1, yButton + height + 1, -6250336);
        drawRect(xButton, yButton, xButton + width, yButton + height, -16777216);
        final int padding = 1;
        if (state) {
            drawCheckmark(xButton + padding, yButton + padding, width - 2 * padding);
        }
    }
    
    private void drawCheckmark(final int x, final int y, final int size) {
        final double yOffset = size / 5.0f / 2.0f;
        Polygon.Builder.create().addVertex(x, y + size * 3 / 6 + yOffset).addVertex(x + size * 2 / 6.0f, y + size * 5.0f / 6.0f + yOffset).addVertex(x + size * 3 / 6.0f, y + size * 4.0f / 6.0f + yOffset).addVertex(x + size / 6.0f, y + size * 2 / 6 + yOffset).build().draw(-3092272);
        Polygon.Builder.create().addVertex(x + size / 2.0f, y + size * 4.0f / 6.0f + yOffset).addVertex(x + size, y + size / 6.0f + yOffset).addVertex(x + size * 5.0f / 6.0f, y + yOffset).addVertex(x + size * 2.0f / 6.0f, y + size * 3.0f / 6.0f + yOffset).build().draw(-3092272);
    }
    
    @Override
    public void keyTyped(final char c, final int keyCode) {
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (wasClickOn(xButton, yButton, mouseX, mouseY)) {
            state = !state;
        }
    }
    
    @Override
    public int getElementX(final int xElement, final int availWidth) {
        return getMiddleX(xElement, availWidth) - getWidth() / 2;
    }
    
    @Override
    public int getElementY(final int yElement, final int availHeight) {
        return getMiddleY(yElement, availHeight) - getHeight() / 2;
    }
    
    @Override
    public Boolean updateValue() {
        final boolean prev = (boolean) value.get();
        if (prev == state) {
            return false;
        }
        value.setValue(state);
        return true;
    }
    
    @Override
    public void onReset() {
        state = (boolean) value.getDefaultValue();
    }
}
