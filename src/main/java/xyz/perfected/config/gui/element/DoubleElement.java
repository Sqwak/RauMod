package xyz.perfected.config.gui.element;

import xyz.perfected.config.gui.ConfigGui;
import xyz.perfected.config.value.Value;

import java.util.function.Predicate;

public class DoubleElement extends TextSelectorElement
{
    private static final Predicate<Character> predicate;
    
    public DoubleElement(final Value value, final ConfigGui parent) {
        super(value, parent, DoubleElement.predicate);
    }
    
    @Override
    public Boolean updateValue() {
        final double prev = (double) value.get();
        try {
            final double current = Double.parseDouble(textSelector.getText());
            value.setValue(current);
            return current != prev;
        }
        catch (NumberFormatException ex) {
            return false;
        }
    }
    
    static {
        predicate = (character -> Character.isDigit(character) || character == '-' || character == '.');
    }
}
