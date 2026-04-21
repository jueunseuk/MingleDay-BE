package returns.mingleday.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import returns.mingleday.domain.category.CategoryTemplate;
import returns.mingleday.domain.mingle.MingleType;
import returns.mingleday.repository.CategoryTemplateRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryTemplateService {

    private final CategoryTemplateRepository categoryTemplateRepository;

    public List<CategoryTemplate> findAllTemplateByMingleType(MingleType mingleType) {
        return categoryTemplateRepository.findAllByMingleType(mingleType);
    }
}
