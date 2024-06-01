package deti.tqs.cinemax.dataInit;

import java.nio.charset.StandardCharsets;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import deti.tqs.cinemax.models.AppUser;
import deti.tqs.cinemax.models.CustomFile;
import deti.tqs.cinemax.models.FilesClass;
import deti.tqs.cinemax.services.FileService;
import deti.tqs.cinemax.services.UserService;

@Component
@Profile("integration-test")
public class FilesDataInit implements CommandLineRunner{
    private FileService fileService;
    private UserService userService;

    @Autowired
    public FilesDataInit(FileService fileService, UserService userService){
        this.fileService = fileService;
        this.userService = userService;
    }


    public void run(String ...args) throws Exception{
        CustomFile testDir = new CustomFile();
        testDir.setName("testDir");
        testDir.setParent(null);
        testDir.setSize(0L);
        testDir.setType("directory");
        fileService.createDirectory(testDir);

        byte[] content = "This is a test file content".getBytes(StandardCharsets.UTF_8);
        MultipartFile file = new MockMultipartFile("file", "test.png", "image/png", content);
        FilesClass ff = new FilesClass();
        ff.setFile(file);
        ff.setParentId(1L);
        fileService.createFile(ff);

        MultipartFile file1 = new MockMultipartFile("file", "test1.png", "image/png", content);
        FilesClass ff1 = new FilesClass();
        ff1.setFile(file1);
        ff1.setParentId(1L);
        fileService.createFile(ff1);

        AppUser admin = new AppUser();
        admin.setUsername("admin");
        admin.setEmail("admin");
        admin.setRole("ADMIN");
        admin.setPassword("admin");
        userService.saveAdminUser(admin);
    }
}