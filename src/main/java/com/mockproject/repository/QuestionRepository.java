/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mockproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mockproject.model.Question;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
/**
 *
 * @author Asus
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer>{
    
    @Query("SELECT q FROM Question q WHERE idSubject=?1")
    public List<Question> findListQuestionBySubject(String idSubject) ;
    
}
