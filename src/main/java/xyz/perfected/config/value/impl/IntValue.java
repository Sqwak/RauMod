package xyz.perfected.config.value.impl;

import net.minecraftforge.common.config.Configuration;
import xyz.perfected.config.gui.ConfigGui;
import xyz.perfected.config.gui.ValueMeta;
import xyz.perfected.config.gui.element.GuiElement;
import xyz.perfected.config.gui.element.IntElement;
import xyz.perfected.config.value.Value;

public class IntValue extends Value<Integer>
{
    public IntValue(final String category, final String displayName, final String description, final Integer defaultValue, final ValueMeta meta) {
        super(category, displayName, description, defaultValue, meta);
    }
    
    @Override
    public void saveValueTo(final Configuration configuration) {
        configuration.get(getCategory(), getDisplayName(), (int)getDefaultValue()).set((int)get());
    }
    
    @Override
    public void loadValueFrom(final Configuration configuration) {
        setValue(configuration.get(getCategory(), getDisplayName(), (int)getDefaultValue()).getInt());
    }
    
    public static ValueMeta getDefaultMeta() {
        return new ValueMeta<Integer>() {
            @Override
            public GuiElement createGui(final Value value, final ConfigGui parent) {
                return new IntElement(value, parent);
            }
            
            @Override
            public Class<Integer> getTClass() {
                return Integer.class;
            }
            
            @Override
            public Value<Integer> createValue(final String category, final String displayName, final String description, final Integer defaultValue) {
                return new IntValue(category, displayName, description, defaultValue, this);
            }
        };
    }
}
