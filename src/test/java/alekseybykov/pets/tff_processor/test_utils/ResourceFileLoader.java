package alekseybykov.pets.tff_processor.test_utils;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

public class ResourceFileLoader {

    private static final ClassLoader classLoader = ResourceFileLoader.class.getClassLoader();

    public static MultipartFile loadMultipartFile(
            String rootPath,
            String fileName
    ) throws IOException {
        var regularFile = ResourceFileLoader.loadFile(rootPath, fileName);
        byte[] bytes = Files.readAllBytes(regularFile.toPath());
        return new MockMultipartFile(
                "file",
                regularFile.getName(),
                "text/plain",
                bytes
        );
    }

    public static File loadFile(String root, String fileName) {
        return new File(
                classLoader.getResource(
                        String.format("%s/%s", root, fileName)
                ).getFile()
        );
    }

    public static File[] loadFiles(String root) throws URISyntaxException {
        return new File(classLoader.getResource(root).toURI())
                .listFiles();
    }
}
