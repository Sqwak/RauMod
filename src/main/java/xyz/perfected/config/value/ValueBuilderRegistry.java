package xyz.perfected.config.value;

import com.google.common.collect.Lists;
import xyz.perfected.config.gui.ValueMeta;
import xyz.perfected.config.value.impl.BoolValue;
import xyz.perfected.config.value.impl.DoubleValue;
import xyz.perfected.config.value.impl.IntValue;
import xyz.perfected.config.value.impl.StringValue;

import java.util.List;

public class ValueBuilderRegistry
{
    private static List<ValueMeta<?>> metaCollection;
    
    public List<ValueMeta<?>> getRegisteredMeta() {
        return ValueBuilderRegistry.metaCollection;
    }
    
    public static <T> Value<T> getInstanceByValue(final String category, final String displayName, final String description, final T defaultValue) {
        for (final ValueMeta<?> meta : ValueBuilderRegistry.metaCollection) {
            if (meta.canParse(defaultValue)) {
                return (Value<T>)meta.create(category, displayName, description, defaultValue);
            }
        }
        throw new IllegalArgumentException("Could not find value for " + defaultValue + " of class " + defaultValue.getClass());
    }
    
    static {
        (ValueBuilderRegistry.metaCollection = Lists.newArrayList()).add(IntValue.getDefaultMeta());
        ValueBuilderRegistry.metaCollection.add(DoubleValue.getDefaultMeta());
        ValueBuilderRegistry.metaCollection.add(StringValue.getDefaultMeta());
        ValueBuilderRegistry.metaCollection.add(BoolValue.getDefaultMeta());
    }
}
