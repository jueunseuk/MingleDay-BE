package returns.mingleday.service.image;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import returns.mingleday.domain.image.Image;
import returns.mingleday.domain.image.ImageType;
import returns.mingleday.repository.ImageRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private final FileService fileService;
    private final ImageRepository imageRepository;

    public Image uploadImage(MultipartFile file, Long targetId, ImageType imageType) {
        Image.validSize(file.getSize());

        String[] path = fileService.storeFile(file, imageType);

        Image image = Image.of(file.getOriginalFilename(), path[1], path[0], file.getSize(), targetId, imageType);

        return imageRepository.save(image);
    }
}