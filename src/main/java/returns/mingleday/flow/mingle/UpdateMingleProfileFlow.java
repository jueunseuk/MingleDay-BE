package returns.mingleday.flow.mingle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import returns.mingleday.domain.image.Image;
import returns.mingleday.domain.image.ImageType;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.mingle.MingleLogType;
import returns.mingleday.domain.mingle.MingleMember;
import returns.mingleday.domain.user.User;
import returns.mingleday.repository.MingleRepository;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.service.image.ImageService;
import returns.mingleday.service.mingle.MingleMemberService;
import returns.mingleday.service.mingle.MingleService;
import returns.mingleday.service.mingle.log.CreateMingleLogService;
import returns.mingleday.service.user.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateMingleProfileFlow {

    @Value("${app.image-base-url}")
    private String imageBaseUrl;

    private final UserService userService;
    private final ImageService imageService;
    private final MingleService mingleService;
    private final MingleRepository mingleRepository;
    private final MingleMemberService mingleMemberService;
    private final CreateMingleLogService createMingleLogService;

    @Transactional
    public String updateMingleProfile(Integer userId, Integer mingleId, MultipartFile file) {
        User user = userService.findUserByUserId(userId);
        Mingle mingle = mingleService.findMingleById(mingleId);

        if(!mingle.getOwner().equals(user)) {
            throw new BaseException(GlobalExceptionCode.FORBIDDEN);
        }

        MingleMember mingleMember = mingleMemberService.getMingleMember(mingle, user);
        Image image = imageService.uploadImage(file, mingleId.longValue(), ImageType.GROUP_PROFILE);

        String imageUrl = imageBaseUrl + "/images/" + image.getPath() + image.getStoredName();
        mingle.updateProfile(imageUrl);

        createMingleLogService.execute(mingle, mingleMember, mingle, MingleLogType.MODIFY);
        log.info("Update a mingle profile image - userId: {}, mingleId: {}", userId, mingleId);

        mingleRepository.save(mingle);

        return imageUrl;
    }
}
