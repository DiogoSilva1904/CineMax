package deti.tqs.cinemax.repository;

import deti.tqs.cinemax.models.CustomFile;
import deti.tqs.cinemax.repositories.FileRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FileRepositoryTest {

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


}
