package xyz.perfected.config.gui.element;

import xyz.perfected.config.gui.ConfigGui;
import xyz.perfected.config.value.Value;

import java.util.function.Predicate;

public class IntElement extends TextSelectorElement
{
    private static final Predicate<Character> predicate;
    
    public IntElement(final Value value, final ConfigGui parent) {
        super(value, parent, IntElement.predicate);
    }
    
    @Override
    public Boolean updateValue() {
        final int prev = (int) value.get();
        try {
            final int current = Integer.parseInt(textSelector.getText());
            value.setValue(current);
            return current != prev;
        }
        catch (NumberFormatException ex) {
            return false;
        }
    }
    
    static {
        predicate = (character -> Character.isDigit(character) || character == '-');
    }
}
