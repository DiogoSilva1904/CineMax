package deti.tqs.cinemax.repository;

import deti.tqs.cinemax.models.CustomFile;
import deti.tqs.cinemax.repositories.FileRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.properties")
public class FileRepositoryTest {

    @Autowired
    private FileRepository fileRepository;

    @Test
    void testSaveFile() {
        CustomFile file = new CustomFile();
        file.setName("test_file");
        file.setType("txt");
        file.setSize(1024L);
        file.setPath("/path/to/test_file");

        CustomFile savedFile = fileRepository.save(file);

        assertThat(savedFile.getId()).isNotNull();
    }

    @Test
    void testFindByName() {
        CustomFile file = new CustomFile();
        file.setName("test_file");
        file.setType("txt");
        file.setSize(1024L);
        file.setPath("/path/to/test_file");

        fileRepository.save(file);

        Optional<CustomFile> foundFile = fileRepository.findByName("test_file");

        assertThat(foundFile).isPresent();
        assertThat(foundFile.get().getName()).isEqualTo("test_file");
    }

    @Test
    void testFindByPath() {
        CustomFile file = new CustomFile();
        file.setName("test_file");
        file.setType("txt");
        file.setSize(1024L);
        file.setPath("/path/to/test_file");

        fileRepository.save(file);

        Optional<CustomFile> foundFile = fileRepository.findByPath("/path/to/test_file");

        assertThat(foundFile).isPresent();
        assertThat(foundFile.get().getPath()).isEqualTo("/path/to/test_file");
    }

    @Test
    @Disabled
    void testFindAllByParentIsNull() {
        CustomFile file1 = new CustomFile();
        file1.setName("file1");
        file1.setType("txt");
        file1.setSize(1024L);
        file1.setPath("/path/to/file1");

        CustomFile parent = new CustomFile();
        parent.setName("parent");
        parent.setType("directory");
        parent.setSize(0L);
        parent.setPath("/path/to/parent");
        fileRepository.save(parent);

        CustomFile file2 = new CustomFile();
        file2.setName("file2");
        file2.setType("txt");
        file2.setSize(1024L);
        file2.setPath("/path/to/parent/file2");
        file2.setParent(parent);
        fileRepository.save(file2);

        fileRepository.save(file1);
        fileRepository.save(file2);

        List<CustomFile> files = fileRepository.findAllByParentIsNull();

        assertThat(files).containsExactly(file1).doesNotContain(parent);
    }

}
