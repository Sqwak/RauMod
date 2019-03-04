package xyz.perfected.config;

import java.io.File;

public interface Config
{
    String getName();
    
    String getVersion();
    
    File getConfigFile();
    
    void loadAllFields();
    
    void saveAllFields();
    
    void openGui();
}
