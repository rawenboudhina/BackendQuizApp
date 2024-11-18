package com.quizapplication.quizapp.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quizapplication.quizapp.Entity.Category;
import com.quizapplication.quizapp.Entity.Question;
import com.quizapplication.quizapp.Entity.Quiz;
import com.quizapplication.quizapp.Repository.QuizRepository;

@Service
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private CategoryService categoryService;

    @Override
    public Quiz createQuiz(Quiz quiz, Long categoryId) {
        Category category = categoryService.findCategoryById(categoryId);

        Quiz createQuiz = new Quiz();
        createQuiz.setCategory(category);
        createQuiz.setTitle(quiz.getTitle());

        return quizRepository.save(createQuiz);
    }

    @Override
    public List<Quiz> findQuizByCategory(Long categoryId) {
        return quizRepository.findByCategory(categoryId);
    }

    @Override
    public Quiz findQuizById(Long quizId) {
        // Return the quiz if found, otherwise return null
        Optional<Quiz> opt = quizRepository.findById(quizId);
        return opt.orElse(null); // Return null if quiz is not found
    }

    @Override
    public Quiz updateQuiz(Quiz quiz, Long quizId) {
        Quiz existingQuiz = findQuizById(quizId);
        if (existingQuiz == null) {
            return null; // If quiz is not found, return null
        }

        if (quiz.getCategory() != null) {
            existingQuiz.setCategory(quiz.getCategory());
        }

        if (quiz.getTitle() != null) {
            existingQuiz.setTitle(quiz.getTitle());
        }

        if (quiz.getCatImage() != null) {
            existingQuiz.setCatImage(quiz.getCatImage());
        }

        return quizRepository.save(existingQuiz);
    }

    @Override
    public List<Question> allQuestionsOfQuiz(Long quizId) {
        Quiz quiz = findQuizById(quizId);
        if (quiz == null) {
            return null; // Return null if quiz is not found
        }
        return quiz.getQuestions();
    }
}
