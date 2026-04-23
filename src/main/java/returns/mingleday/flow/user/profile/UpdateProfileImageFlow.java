package returns.mingleday.flow.user.profile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import returns.mingleday.domain.image.Image;
import returns.mingleday.domain.image.ImageType;
import returns.mingleday.domain.user.User;
import returns.mingleday.service.image.ImageService;
import returns.mingleday.service.user.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateProfileImageFlow {

    private final UserService userService;
    private final ImageService imageService;

    @Transactional
    public String updateProfileImage(Integer userId, MultipartFile profileImage) {
        User user = userService.findUserByUserId(userId);

        Image image = imageService.uploadImage(profileImage, userId.longValue(), ImageType.USER_PROFILE);

        user.updateProfileUrl(image.getPath());

        log.info("Success to update profile image - userId: {}", userId);
        return image.getPath();
    }
}
