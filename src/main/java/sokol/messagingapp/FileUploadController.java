package sokol.messagingapp;

import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sokol.messagingapp.service.StorageService;

import java.io.IOException;

@Controller
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/files/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("folder") String folder) {
        try {
            this.storageService.store(file, folder);
            return new ResponseEntity<>("File \"" + folder + file.getOriginalFilename() + "\" was successfully uploaded", HttpStatus.OK);
        } catch (IOException ioex) {
            ioex.printStackTrace();
            return new ResponseEntity<>("Unable to upload file \"" + folder + file.getOriginalFilename() + "\"" , HttpStatus.NOT_MODIFIED);
        }
    }

}
