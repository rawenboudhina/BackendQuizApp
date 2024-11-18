package com.quizapplication.quizapp.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quizapplication.quizapp.Entity.Question;
import com.quizapplication.quizapp.Entity.Quiz;
import com.quizapplication.quizapp.Repository.QuestionRepository;
import com.quizapplication.quizapp.Repository.QuizRepository;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuizRepository quizRepository;

    @Override
    public Question createQuestion(Question question, Long quizId) {
        Quiz quiz = quizService.findQuizById(quizId);
        
        Question createQuestion = new Question();
        createQuestion.setContent(question.getContent());
        createQuestion.setQuiz(quiz);
        createQuestion.setAnswers(question.getAnswers());
        createQuestion.setCorrectAnswer(question.getCorrectAnswer());
        createQuestion.setExplanation(question.getExplanation());

        quiz.getQuestions().add(createQuestion);
        quizRepository.save(quiz);

        return questionRepository.save(createQuestion);
    }

    @Override
    public String deleteQuestion(Long questionId) {
        Question question = findQuestionById(questionId);

        if (question != null) {
            questionRepository.delete(question);
            return "Question deleted successfully";
        }

        return "Question not found"; // Si la question n'est pas trouvée
    }

    @Override
    public Question updateQuestion(Question question, long questionId) {
        Question updateQuestion = findQuestionById(questionId);

        if (updateQuestion != null) {
            updateQuestion.setContent(question.getContent());
            updateQuestion.setAnswers(question.getAnswers());
            updateQuestion.setExplanation(question.getExplanation());
            updateQuestion.setCorrectAnswer(question.getCorrectAnswer());
            return questionRepository.save(updateQuestion);
        }

        return null; // Retourne null si la question n'est pas trouvée
    }

    @Override
    public Question findQuestionById(Long questionId) {
        Optional<Question> opt = questionRepository.findById(questionId);

        return opt.orElse(null); // Retourne null si la question n'est pas trouvée
    }

    @Override
    public boolean isCorrect(Long questionId, Integer choice) {
        Question question = findQuestionById(questionId);

        return question != null && question.getCorrectAnswer().equals(choice);
    }
}
