package xyz.perfected.config.value;

import xyz.perfected.config.gui.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.config.Configuration;
import xyz.perfected.config.gui.ValueMeta;

public abstract class Value<T>
{
    private String displayName;
    private String description;
    private String category;
    private ValueMeta meta;
    private boolean saveToFile;
    private boolean showInHud;
    private T value;
    private T defaultValue;
    
    public Value(final String category, final String displayName, final String description, final T defaultValue, final ValueMeta meta) {
        this.saveToFile = true;
        this.showInHud = true;
        this.displayName = displayName;
        this.description = description;
        this.category = category;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.meta = meta;
    }
    
    public ValueMeta getMeta() {
        return meta;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public boolean saveToFile() {
        return saveToFile;
    }
    
    public boolean showInHud() {
        return showInHud;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getCategory() {
        return category;
    }
    
    public T getDefaultValue() {
        return defaultValue;
    }
    
    public T get() {
        return value;
    }
    
    public abstract void saveValueTo(final Configuration p0);
    
    public abstract void loadValueFrom(final Configuration p0);
    
    public void setValue(final T value) {
        try {
            this.value = value;
        }
        catch (ClassCastException e) {
            e.printStackTrace();
            final Minecraft mc = Minecraft.getMinecraft();
            if (mc.thePlayer != null) {
                mc.thePlayer.sendChatMessage(EnumChatFormatting.RED + "Something went wrong setting value for " + getClass().getSimpleName() + " " + getDisplayName() + " value " + value);
            }
        }
    }
    
    public void setValueToDefault() {
        setValue(getDefaultValue());
    }
    
    public static class Builder<T>
    {
        private T defaultValue;
        private String displayName;
        private String description;
        private String category;
        private boolean saveToFile;
        private boolean showInHud;
        
        private Builder() {
            defaultValue = null;
            displayName = "N/A";
            description = "N/A";
            category = "N/A";
            saveToFile = true;
            showInHud = true;
        }
        
        public static <T> Builder<T> create(final T defaultValue) {
            final Builder<T> builder = new Builder<T>();
            builder.defaultValue = defaultValue;
            return builder;
        }
        
        public Builder<T> setDescription(final String description) {
            this.description = description;
            return this;
        }
        
        public Builder<T> setDisplayName(final String displayName) {
            this.displayName = displayName;
            return this;
        }
        
        public Builder<T> setSaveToFile(final boolean saveToFile) {
            this.saveToFile = saveToFile;
            return this;
        }
        
        public Builder<T> setShowInHud(final boolean showInHud) {
            this.showInHud = showInHud;
            return this;
        }
        
        public Builder<T> setCategory(final String category) {
            this.category = category;
            return this;
        }
        
        public Value<T> build() {
            final Value<T> value = ValueBuilderRegistry.getInstanceByValue(category, displayName, description, defaultValue);
            ((Value<Object>)value).showInHud = showInHud;
            ((Value<Object>)value).saveToFile = saveToFile;
            return value;
        }
    }
}
