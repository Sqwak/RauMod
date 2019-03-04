package xyz.perfected.config.gui.element;

import xyz.perfected.config.gui.ConfigGui;
import xyz.perfected.config.gui.selector.TextSelector;
import xyz.perfected.config.value.Value;

import java.util.function.Predicate;

public abstract class TextSelectorElement extends GuiElement
{
    protected final TextSelector textSelector;
    
    public TextSelectorElement(final Value value, final ConfigGui parent) {
        this(value, parent, character -> true);
    }
    
    public TextSelectorElement(final Value value, final ConfigGui parent, final Predicate<Character> charPredicate) {
        super(value, parent, 20, 20);
        textSelector = new TextSelector(String.valueOf(value.get()), charPredicate);
    }
    
    @Override
    public void draw(final int xElement, final int yElement, final int availWidth, final int availHeight, final int xMouse, final int yMouse) {
        textSelector.draw(xElement, yElement, availWidth, availHeight, xMouse, yMouse);
    }
    
    @Override
    public void keyTyped(final char c, final int keyCode) {
        textSelector.textboxKeyTyped(c, keyCode);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        textSelector.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public int getElementX(final int x, final int availWidth) {
        return textSelector.getElementX(x, availWidth);
    }
    
    @Override
    public int getElementY(final int y, final int availHeight) {
        return textSelector.getElementY(y, availHeight);
    }
    
    @Override
    public void onSelect() {
        super.onSelect();
        textSelector.setFocused(true);
    }
    
    @Override
    public void onReset() {
        super.onReset();
        textSelector.setText(value.getDefaultValue().toString());
    }
}
