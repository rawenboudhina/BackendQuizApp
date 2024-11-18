package com.quizapplication.quizapp.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quizapplication.quizapp.Entity.Category;
import com.quizapplication.quizapp.Repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category createCategory(Category category) {
        Category createCategory = new Category();
        createCategory.setName(category.getName());

        return categoryRepository.save(createCategory);
    }

    @Override
    public Category findCategoryById(Long categoryId) {
        Optional<Category> opt = categoryRepository.findById(categoryId);

        return opt.orElse(null);  // Retourne null si la catégorie n'est pas trouvée
    }
}
