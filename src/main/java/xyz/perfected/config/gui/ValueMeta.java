package xyz.perfected.config.gui;

import xyz.perfected.config.gui.element.*;
import xyz.perfected.config.value.*;
import xyz.perfected.config.gui.element.GuiElement;
import xyz.perfected.config.value.Value;

public interface ValueMeta<T>
{
    GuiElement createGui(final Value p0, final ConfigGui p1);
    
    default Value<T> create(final String category, final String displayName, final String description, final Object defaultValue) {
        return createValue(category, displayName, description, parse(defaultValue));
    }
    
    Value<T> createValue(final String p0, final String p1, final String p2, final T p3);
    
    Class<T> getTClass();
    
    default boolean canParse(final Object object) {
        return getTClass().isInstance(object);
    }
    
    default T parse(final Object object) {
        return (T)object;
    }
}
