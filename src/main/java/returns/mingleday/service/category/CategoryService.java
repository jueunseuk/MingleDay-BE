package returns.mingleday.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import returns.mingleday.domain.category.Category;
import returns.mingleday.domain.category.CategoryTemplate;
import returns.mingleday.domain.mingle.Mingle;
import returns.mingleday.domain.mingle.MingleMember;
import returns.mingleday.domain.mingle.MingleType;
import returns.mingleday.domain.mingle.PermissionType;
import returns.mingleday.domain.schedule.Schedule;
import returns.mingleday.domain.user.User;
import returns.mingleday.model.category.CategoryResponse;
import returns.mingleday.model.category.UpsertCategoryRequest;
import returns.mingleday.repository.CategoryRepository;
import returns.mingleday.repository.ScheduleRepository;
import returns.mingleday.response.code.CategoryExceptionCode;
import returns.mingleday.response.code.GlobalExceptionCode;
import returns.mingleday.response.exception.BaseException;
import returns.mingleday.service.mingle.MingleMemberService;
import returns.mingleday.service.mingle.MinglePermissionService;
import returns.mingleday.service.mingle.MingleService;
import returns.mingleday.service.schedule.ScheduleSearchService;
import returns.mingleday.service.user.UserService;
import returns.mingleday.util.ColorUtil;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final UserService userService;
    private final MingleService mingleService;
    private final CategoryRepository categoryRepository;
    private final ScheduleRepository scheduleRepository;
    private final MingleMemberService mingleMemberService;
    private final ScheduleSearchService scheduleSearchService;
    private final MinglePermissionService minglePermissionService;
    private final CategoryTemplateService categoryTemplateService;

    public Category findCategoryByIdAndMingle(Long categoryId, Mingle mingle) {
        return categoryRepository.findCategoryByCategoryIdAndMingle(categoryId, mingle)
                .orElseThrow(() -> new BaseException(GlobalExceptionCode.RESOURCE_NOT_FOUND));
    }

    public List<CategoryResponse> getAllCategoryByMingle(Integer userId, Integer mingleId) {
        User user = userService.findUserByUserId(userId);
        Mingle mingle = mingleService.findMingleById(mingleId);

        if(!mingleMemberService.existsByMingleAndUser(mingle, user)) {
            throw new BaseException(GlobalExceptionCode.FORBIDDEN);
        }

        List<Category> categories = categoryRepository.findAllByMingle(mingle);

        return categories.stream().map(CategoryResponse::new).toList();
    }

    @Transactional
    public void createCategory(Integer userId, Integer mingleId, UpsertCategoryRequest request) {
        User user = userService.findUserByUserId(userId);

        if(!request.getMingleId().equals(mingleId)) {
            throw new BaseException(GlobalExceptionCode.BAD_REQUEST_FOR_MISMATCH);
        }

        ColorUtil.validateHex(request.getTextColor());
        ColorUtil.validateHex(request.getBackgroundColor());

        double contrast = ColorUtil.getContrastRatio(request.getTextColor(), request.getBackgroundColor());
        if (contrast < 4.5) {
            throw new BaseException(CategoryExceptionCode.TOO_SIMILAR_WITH_COLORS);
        }

        Mingle mingle = mingleService.findMingleById(mingleId);
        MingleMember mingleMember = mingleMemberService.getMingleMember(mingle, user);
        if(mingle.getUsePermission() &&
                !mingle.getOwner().equals(user) &&
                !minglePermissionService.doesMemberHavePermission(mingleMember, PermissionType.CREATE)) {
            throw new BaseException(GlobalExceptionCode.FORBIDDEN);
        }

        Category category = Category.of(
                mingle,
                request.getName(),
                request.getDescription(),
                request.getBackgroundColor(),
                request.getTextColor()
        );

        log.info("Create a new category - userId: {}, categoryId: {}", userId, category.getCategoryId());
        categoryRepository.save(category);
    }

    @Transactional
    public void modifyCategory(Integer userId, Integer mingleId, Long categoryId, UpsertCategoryRequest request) {
        User user = userService.findUserByUserId(userId);

        if(!request.getMingleId().equals(mingleId)) {
            throw new BaseException(GlobalExceptionCode.BAD_REQUEST_FOR_MISMATCH);
        }

        ColorUtil.validateHex(request.getTextColor());
        ColorUtil.validateHex(request.getBackgroundColor());

        double contrast = ColorUtil.getContrastRatio(request.getTextColor(), request.getBackgroundColor());
        if (contrast < 4.5) {
            throw new BaseException(CategoryExceptionCode.TOO_SIMILAR_WITH_COLORS);
        }

        Mingle mingle = mingleService.findMingleById(mingleId);
        MingleMember mingleMember = mingleMemberService.getMingleMember(mingle, user);
        if(mingle.getUsePermission() &&
                !mingle.getOwner().equals(user) &&
                !minglePermissionService.doesMemberHavePermission(mingleMember, PermissionType.MODIFY)) {
            throw new BaseException(GlobalExceptionCode.FORBIDDEN);
        }

        if(!request.getCategoryId().equals(categoryId)) {
            throw new BaseException(GlobalExceptionCode.BAD_REQUEST_FOR_MISMATCH);
        }
        Category category = findCategoryByIdAndMingle(categoryId, mingle);

        category.update(
                request.getName(),
                request.getDescription(),
                request.getBackgroundColor(),
                request.getTextColor()
        );

        log.info("Update a category - userId: {}, categoryId: {}", userId, category.getCategoryId());
        categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Integer userId, Integer mingleId, Long categoryId) {
        User user = userService.findUserByUserId(userId);
        Mingle mingle = mingleService.findMingleById(mingleId);

        MingleMember mingleMember = mingleMemberService.getMingleMember(mingle, user);
        if(mingle.getUsePermission() &&
                !mingle.getOwner().equals(user) &&
                !minglePermissionService.doesMemberHavePermission(mingleMember, PermissionType.DELETE)) {
            throw new BaseException(GlobalExceptionCode.FORBIDDEN);
        }

        Category category = findCategoryByIdAndMingle(categoryId, mingle);

        List<Schedule> schedules = scheduleSearchService.findScheduleByCategory(category);
        for(Schedule schedule : schedules) {
            schedule.cleanCategory();
        }
        scheduleRepository.flush();

        log.info("Delete a category - userId: {}, categoryId: {}", userId, category.getCategoryId());
        categoryRepository.delete(category);
    }

    public Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BaseException(GlobalExceptionCode.RESOURCE_NOT_FOUND));
    }

    @Transactional
    public void createTemplate(Mingle mingle, MingleType mingleType) {
        List<CategoryTemplate> categoryTemplates = categoryTemplateService.findAllTemplateByMingleType(mingleType);

        List<Category> categories = new ArrayList<>();
        for(CategoryTemplate categoryTemplate : categoryTemplates) {
            categories.add(Category.of(
                    mingle,
                    categoryTemplate.getName(),
                    categoryTemplate.getDescription(),
                    categoryTemplate.getBackgroundColor(),
                    categoryTemplate.getTextColor())
            );
        }

        categoryRepository.saveAll(categories);
    }
}
