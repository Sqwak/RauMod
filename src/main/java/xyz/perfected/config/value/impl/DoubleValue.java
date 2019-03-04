package xyz.perfected.config.value.impl;

import net.minecraftforge.common.config.Configuration;
import xyz.perfected.config.gui.ConfigGui;
import xyz.perfected.config.gui.ValueMeta;
import xyz.perfected.config.gui.element.DoubleElement;
import xyz.perfected.config.gui.element.GuiElement;
import xyz.perfected.config.value.Value;

public class DoubleValue extends Value<Double>
{
    public DoubleValue(final String category, final String displayName, final String description, final Double defaultValue, final ValueMeta meta) {
        super(category, displayName, description, defaultValue, meta);
    }
    
    @Override
    public void saveValueTo(final Configuration configuration) {
        configuration.get(getCategory(), getDisplayName(), (double)getDefaultValue()).set((double)get());
    }
    
    @Override
    public void loadValueFrom(final Configuration configuration) {
        setValue(configuration.get(getCategory(), getDisplayName(), (double)getDefaultValue()).getDouble());
    }
    
    public static ValueMeta getDefaultMeta() {
        return new ValueMeta<Double>() {
            @Override
            public GuiElement createGui(final Value value, final ConfigGui parent) {
                return new DoubleElement(value, parent);
            }
            
            @Override
            public Class<Double> getTClass() {
                return Double.class;
            }
            
            @Override
            public Double parse(final Object object) {
                return (Double)object;
            }
            
            @Override
            public Value<Double> createValue(final String category, final String displayName, final String description, final Double defaultValue) {
                return new DoubleValue(category, displayName, description, defaultValue, this);
            }
        };
    }
}
