/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mockproject.controller;

import com.mockproject.model.Question;
import com.mockproject.model.Quiz;
import com.mockproject.model.QuizCart;
import com.mockproject.model.QuizDetail;
import com.mockproject.model.QuizOfStudent;
import com.mockproject.security.CustomUserDetail;
import com.mockproject.service.QuestionService;
import com.mockproject.service.QuizDetailService;
import com.mockproject.service.QuizOfStudentService;
import com.mockproject.service.QuizService;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author Asus
 */
@Controller
public class QuizController {

    public final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Autowired
    QuestionService questionService;

//    @Autowired
//    AnswerService answerService;
    @Autowired
    QuizOfStudentService quizOfStudentService;

    @Autowired
    QuizDetailService quizDetailService;

    @Autowired
    QuizService quizService;
    

    @GetMapping("/student/quiz/showQuiz/{idQuiz}")
    public String showQuiz(HttpSession session,
            Model model, @PathVariable("idQuiz") int idQuiz) {
        Quiz quiz = quizService.getQuizByIdQuiz(idQuiz);
        List<Question> questions = questionService.findListQuestionByIdQuiz(idQuiz);
        session.setAttribute("questions", questions);
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QuizCart cart = new QuizCart(userDetail.getUser().getIdUser(), idQuiz);
        session.setAttribute("quizCart", cart);
        model.addAttribute("questionIndex", 0);
        Timestamp startQuiz = new Timestamp(System.currentTimeMillis());
        session.setAttribute("TimeStartQuiz", startQuiz);
        session.setAttribute("timeLimit", quiz.getTimeLimit());
        return "quiz";
    }

    @GetMapping("/student/quiz/chooseQuestion")
    public String add(HttpSession session, HttpServletRequest request, Model model) throws Exception {
        String page = "quiz";
        int questionIndex = 0;
        int idQuestion = Integer.parseInt(request.getParameter("questionId"));
        int userAnswer = -1;
        try {
            questionIndex = Integer.parseInt(request.getParameter("questionIndex"));
        } catch (Exception e) {
            //Ghi loi ra file log
        }
        try {
            userAnswer = Integer.parseInt(request.getParameter("answer"));
        } catch (Exception e) {
            //Ghi loi ra file log
        }
        QuizCart cart = (QuizCart) session.getAttribute("quizCart");
        cart.getQuizCart().put(idQuestion, userAnswer);
        session.setAttribute("quizCart", cart);
        model.addAttribute("questionIndex", questionIndex);

        if (request.getParameter("action") != null && request.getParameter("action").equals("Finish")) {
            page = submitQuiz(session, model, request);
        }
        return page;
    }

    public String submitQuiz(HttpSession session, Model model, HttpServletRequest request) {
        QuizCart cart = (QuizCart) session.getAttribute("quizCart");
        int totalCorrect = 0;
        List<Question> questions = (List<Question>) session.getAttribute("questions");
        for (Integer questionId : cart.getQuizCart().keySet()) {
            System.out.println(cart.getQuizCart().get(questionId));
            if (questionService.findAnswerByIdCorrect(questionId) == cart.getQuizCart().get(questionId)) {
                totalCorrect++;
            }
        }

        QuizOfStudent quizOfStudent = new QuizOfStudent();
        quizOfStudent.setIdQuiz(cart.getIdQuiz());
        float score = (totalCorrect * 10 / questions.size());
        if (score >= 5.0) {
            quizOfStudent.setPass(true);
        } else {
            quizOfStudent.setPass(false);
        }
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        quizOfStudent.setIdUser(userDetail.getUser().getIdUser());
        quizOfStudent.setTotalCorrect(totalCorrect);
        quizOfStudent.setSubmitDate(df.format(new Date()));
        int idQuizOfStudent = quizOfStudentService.getIdOfQuizOfUser();
        quizOfStudent.setIdQuizOfUser(idQuizOfStudent + 1);
        quizOfStudentService.save(quizOfStudent);
        for (Integer questionId : cart.getQuizCart().keySet()) {
            QuizDetail quizDetail = new QuizDetail();
            quizDetail.setIdQuestion(questionId);
            quizDetail.setQuizOfStudent(quizOfStudent);
            quizDetail.setUserAnswer(cart.getQuizCart().get(questionId));
            quizDetailService.insertQuizDetail(quizDetail);
        }

        //send notification
        quizOfStudentService.sendEmailToNotifyQuiz(userDetail.getUser(), totalCorrect, questions.size());

        session.removeAttribute("quizCart");
        session.removeAttribute("questionIndex");
        session.removeAttribute("TimeStartQuiz");
        return "studentHome";
    }

//    @GetMapping("/quizReview/{idQuiz}")
//    public String viewQuizReviewPage(Model model,@PathVariable("idQuiz") int idQuiz) {
//        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        QuizOfStudent quizOfStudent=quizOfStudentService.getQuizReviewByIdStudentAndIdQuiz(userDetail.getUser().getIdUser(), idQuiz);
//        System.out.println(quizOfStudent.getQuiz().getNameQuiz());
//        System.out.println(quizOfStudent.getSubmitDate());
//        model.addAttribute("quizReview", quizOfStudent);
//        
//        return "quiz-review";
//    }
    

}
