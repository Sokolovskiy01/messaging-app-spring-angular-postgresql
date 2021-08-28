package sokol.messagingapp.service;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import sokol.messagingapp.config.StorageProperties;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageService {

    private final Path rootLocation;

    public StorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    /**
     * Upload file to backend directory
     * @param file uploading file
     * @param folderName template : "foldername/". Could be null
     * @param filename custom file name to store in provided folder. Could be null
     * @throws IOException
     */
    public void store(@NotNull MultipartFile file, @Nullable String folderName, @Nullable String filename) throws IOException {
        if (file == null || file.isEmpty()) throw new RuntimeException("Requested file to store is empty");
        else {
            String filepathString = file.getOriginalFilename();
            if (folderName != null) {
                Path newFolder = Path.of(this.rootLocation + folderName);
                if (!Files.exists(newFolder)) Files.createDirectory(newFolder);
                if (filename != null) {
                    filepathString = folderName + filename;
                }
                else {
                    filepathString = folderName + file.getOriginalFilename();
                }
            }
            Files.copy(file.getInputStream(), this.rootLocation.resolve(filepathString));
        }
    }

    public Path load(String filename) {
        return this.rootLocation.resolve(filename);
    }

    /**
     * Function to load stored file as link to backend directory
     */
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new RuntimeException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + filename, e);
        }
    }

    public void deleteAll() throws IOException {
        FileSystemUtils.deleteRecursively(this.rootLocation);
    }

    public void init() {
        try {
            if (!Files.exists(this.rootLocation)) Files.createDirectory(this.rootLocation);
            System.out.println("Storage service successful init");
        }
        catch (IOException ex) {
            throw new RuntimeException("Could not initialize storage service", ex);
        }
    }

    public Path getRootLocation() { return rootLocation; }

}
