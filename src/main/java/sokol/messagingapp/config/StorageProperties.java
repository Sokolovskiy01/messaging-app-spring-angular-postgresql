package sokol.messagingapp.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageProperties {

    // Folder location for storing files
    private String location = "uploads";

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}