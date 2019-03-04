package xyz.perfected.config.value.impl;

import xyz.perfected.config.gui.*;
import xyz.perfected.config.gui.element.*;
import xyz.perfected.config.value.*;
import net.minecraftforge.common.config.Configuration;
import xyz.perfected.config.gui.ConfigGui;
import xyz.perfected.config.gui.ValueMeta;
import xyz.perfected.config.gui.element.GuiElement;
import xyz.perfected.config.gui.element.StringElement;
import xyz.perfected.config.value.Value;

public class StringValue extends Value<String>
{
    public StringValue(final String category, final String displayName, final String description, final String defaultValue, final ValueMeta meta) {
        super(category, displayName, description, defaultValue, meta);
    }
    
    @Override
    public void saveValueTo(final Configuration configuration) {
        configuration.get(getCategory(), getDisplayName(), (String)getDefaultValue()).set((String)get());
    }
    
    @Override
    public void loadValueFrom(final Configuration configuration) {
        setValue(configuration.get(getCategory(), getDisplayName(), (String)getDefaultValue()).getString());
    }
    
    public static ValueMeta getDefaultMeta() {
        return new ValueMeta<String>() {
            @Override
            public GuiElement createGui(final Value value, final ConfigGui parent) {
                return new StringElement(value, parent);
            }
            
            @Override
            public boolean canParse(final Object object) {
                return object instanceof String;
            }
            
            @Override
            public Class<String> getTClass() {
                return String.class;
            }
            
            @Override
            public String parse(final Object object) {
                return object.toString();
            }
            
            @Override
            public Value<String> createValue(final String category, final String displayName, final String description, final String defaultValue) {
                return new StringValue(category, displayName, description, defaultValue, this);
            }
        };
    }
}
