package com.quizapplication.quizapp.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.quizapplication.quizapp.Entity.Question;
import com.quizapplication.quizapp.Entity.Quiz;
import com.quizapplication.quizapp.Entity.User;
import com.quizapplication.quizapp.Service.QuizService;
import com.quizapplication.quizapp.Service.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/quiz/")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private UserService userService;

    @PostMapping("create-quiz/{categoryId}")
    public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz quiz, @PathVariable("categoryId") Long categoryId) {
        Quiz createQuiz = quizService.createQuiz(quiz, categoryId);
        return new ResponseEntity<>(createQuiz, HttpStatus.CREATED);
    }

    @GetMapping("category-quiz")
    public ResponseEntity<List<Quiz>> findQuizByCategoryHandler(@RequestParam("categoryId") Long categoryId, @RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByToken(jwt);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<Quiz> quizzes = quizService.findQuizByCategory(categoryId);
        return new ResponseEntity<>(quizzes, HttpStatus.OK);
    }

    @GetMapping("all-quizQue")
    public ResponseEntity<List<Question>> findAllQuestionOFQuiz(@RequestParam("quizId") Long quizId, @RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByToken(jwt);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<Question> questions = quizService.allQuestionsOfQuiz(quizId);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @PutMapping("update-quiz/{quizId}")
    public ResponseEntity<Quiz> updateQuiz(@RequestBody Quiz quiz, @PathVariable("quizId") Long quizId) {
        Quiz updateQuiz = quizService.updateQuiz(quiz, quizId);
        return new ResponseEntity<>(updateQuiz, HttpStatus.OK);
    }
}
