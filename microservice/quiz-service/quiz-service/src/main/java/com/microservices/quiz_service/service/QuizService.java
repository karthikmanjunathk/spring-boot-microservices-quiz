package com.microservices.quiz_service.service;

import com.microservices.quiz_service.feign.QuizInterface;
import com.microservices.quiz_service.model.*;
import com.microservices.quiz_service.repo.QuizDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    private QuizDao quizDao;

    @Autowired
    QuizInterface quizInterface;

    public ResponseEntity<String> createQuiz(String category, Integer numQ, String title){

        List<Integer> questionList = quizInterface.getQuestionsForQuiz(category, numQ).getBody();  // Fetching from question service
        Quiz quiz = new Quiz();
        quiz.setTitle(title);      // Title is for quiz, given by the user
        quiz.setQuestionIds(questionList);
        quizDao.save(quiz);
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        System.out.println("In getQuizQuestions method");
        Quiz quiz = quizDao.findById(id).get();
        List<Integer> questionIds = quiz.getQuestionIds();
        return quizInterface.getQuestionsFromId(questionIds);
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> response) {
        return quizInterface.getScore(response);
    }
}
