package xyz.perfected.config;

import com.google.common.collect.Lists;
import xyz.perfected.config.value.Value;

import java.lang.reflect.Field;
import java.util.List;

public abstract class ConfigHolder
{
    private List<Value> values;
    
    protected ConfigHolder() {
        values = Lists.newArrayList();
    }
    
    protected void loadFields() {
        for (final Field field : getClass().getFields()) {
            Object val = null;
            try {
                field.setAccessible(true);
                val = field.get(this);
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (val instanceof Value) {
                values.add((Value)val);
            }
        }
    }
    
    public List<Value> getConfigEntries() {
        if (values.isEmpty()) {
            loadFields();
        }
        return values;
    }
    
    public int getConfigEntryCount() {
        return values.size();
    }
}
