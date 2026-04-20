package returns.mingleday.service.image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import returns.mingleday.domain.image.ImageType;
import returns.mingleday.response.code.ImageExceptionCode;
import returns.mingleday.response.exception.BaseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String[] storeFile(MultipartFile file, ImageType imageType) {
        String relativePath = "";
        switch (imageType) {
            case GROUP_PROFILE -> relativePath = "group/profile/";
            case SCHEDULE_BEFORE -> relativePath = "schedule/before/";
            case SCHEDULE_AFTER -> relativePath = "schedule/after/";
            case USER_PROFILE -> relativePath = "user/profile/";
            default -> relativePath = "etc/";
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Path targetLocation = Paths.get(uploadDir).resolve(relativePath).resolve(fileName);

        try {
            Files.createDirectories(targetLocation.getParent());

            file.transferTo(targetLocation.toFile()); // save
            log.info("Success to save image - saveDir: {}", targetLocation);
            return new String[] {relativePath, targetLocation.toString()};
        } catch (IOException e) {
            log.warn("Error saving file");
            throw new BaseException(ImageExceptionCode.IMAGE_UPLOAD_FAILED);
        }
    }
}