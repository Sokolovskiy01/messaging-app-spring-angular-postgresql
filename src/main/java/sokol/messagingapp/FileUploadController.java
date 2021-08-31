package sokol.messagingapp;

import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sokol.messagingapp.model.AppUser;
import sokol.messagingapp.service.AppUserService;
import sokol.messagingapp.service.StorageService;

import java.io.IOException;
import java.util.Date;

@Controller
public class FileUploadController {

    private final StorageService storageService;
    private final AppUserService appUserService;

    @Autowired
    public FileUploadController(StorageService storageService, AppUserService appUserService) {
        this.storageService = storageService;
        this.appUserService = appUserService;
    }


    @GetMapping({ "/uploads/{filename:.+}", "/uploads/{folder}/{filename:.+}" })
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename, @PathVariable(required = false) String folder) {
        String filepath = (folder == null) ? filename : folder + "/" + filename;
        Resource file = storageService.loadAsResource(filepath);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/files/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("folder") String folder) {
        try {
            this.storageService.store(file, folder, null);
            return new ResponseEntity<>("File \"" + folder + file.getOriginalFilename() + "\" was successfully uploaded", HttpStatus.OK);
        } catch (IOException ioex) {
            ioex.printStackTrace();
            return new ResponseEntity<>("Unable to upload file \"" + folder + file.getOriginalFilename() + "\"" , HttpStatus.NOT_MODIFIED);
        }
    }

    @PostMapping("/files/avatarUpload")
    public ResponseEntity<AppUser> handleAvatarUpload(@RequestParam("file") MultipartFile file, @RequestParam("folder") String folder, @RequestParam("userid") String userid ) {
        try {
            String userAvatarFileName = userid + "_" + new Date().getTime() + "_" +file.getOriginalFilename();
            this.storageService.store(file, folder, userAvatarFileName);
            String userFilePath = "/" + this.storageService.getRootLocation() + "/" + folder + userAvatarFileName;
            Long userId = Long.parseLong(userid);
            AppUser existingUser = this.appUserService.getAppUserById(userId);
            existingUser.setImageUrl(userFilePath);
            AppUser returnUser = this.appUserService.updateUser(existingUser);
            return new ResponseEntity<>(returnUser, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null , HttpStatus.NOT_MODIFIED);
        }
    }

}
