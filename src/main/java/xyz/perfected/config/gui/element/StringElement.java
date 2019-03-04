package xyz.perfected.config.gui.element;

import xyz.perfected.config.gui.ConfigGui;
import xyz.perfected.config.value.Value;

public class StringElement extends TextSelectorElement
{
    public StringElement(final Value value, final ConfigGui parent) {
        super(value, parent);
    }
    
    @Override
    public Boolean updateValue() {
        final String prev = (String) value.get();
        if (prev.equals(textSelector.getText())) {
            return false;
        }
        value.setValue(textSelector.getText());
        return true;
    }
}
