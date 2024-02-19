package downloadFile.service;

import downloadFile.entity.Category;
import downloadFile.entity.User;
import downloadFile.repository.CategoryRepository;
import downloadFile.repository.UserSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category saveCategory(Category category){
        return categoryRepository.save(category);
    }
}
