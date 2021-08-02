package com.mockproject.service;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mockproject.model.Subject;
import com.mockproject.repository.AnswerRepository;
import com.mockproject.repository.ClassRepository;
import com.mockproject.repository.SubjectRepository;
import com.mockproject.model.Answer;
import com.mockproject.model.Class;


@Service
public class AnswerService {
	@Autowired
	private AnswerRepository answerRepository;
	
	public List<Answer> findAnswerByQuestion_idQuestion(int id){
		return answerRepository.findAnswerByQuestion_idQuestion(id);
	}
	
	public List<Answer> findAllAnswer(){
		return answerRepository.findAll();
	}
}
