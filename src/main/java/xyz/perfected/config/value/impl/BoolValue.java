package xyz.perfected.config.value.impl;

import net.minecraftforge.common.config.Configuration;
import xyz.perfected.config.gui.ConfigGui;
import xyz.perfected.config.gui.ValueMeta;
import xyz.perfected.config.gui.element.BoolElement;
import xyz.perfected.config.gui.element.GuiElement;
import xyz.perfected.config.value.Value;

public class BoolValue extends Value<Boolean>
{
    public BoolValue(final String category, final String displayName, final String description, final boolean defaultValue, final ValueMeta meta) {
        super(category, displayName, description, defaultValue, meta);
    }
    
    @Override
    public void saveValueTo(final Configuration configuration) {
        configuration.get(getCategory(), getDisplayName(), (boolean)getDefaultValue()).set((boolean)get());
    }
    
    @Override
    public void loadValueFrom(final Configuration configuration) {
        setValue(configuration.get(getCategory(), getDisplayName(), (boolean)getDefaultValue()).getBoolean());
    }
    
    public static ValueMeta getDefaultMeta() {
        return new ValueMeta<Boolean>() {
            @Override
            public GuiElement createGui(final Value value, final ConfigGui parent) {
                return new BoolElement(value, parent);
            }
            
            @Override
            public Value<Boolean> createValue(final String category, final String displayName, final String description, final Boolean defaultValue) {
                return new BoolValue(category, displayName, description, (boolean)defaultValue, this);
            }
            
            @Override
            public Class<Boolean> getTClass() {
                return Boolean.class;
            }
        };
    }
}
