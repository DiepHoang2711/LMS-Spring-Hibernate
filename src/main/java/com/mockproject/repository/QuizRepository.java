/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mockproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mockproject.model.Quiz;
import com.mockproject.model.Subject;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Asus
 */
@Repository
public interface QuizRepository extends JpaRepository<Quiz, Integer> {
       
    List<Quiz> findByIdUser(int idUser);
    
    @Query("SELECT q FROM Quiz q JOIN q.classes c JOIN q.subject s WHERE c.idClass=?1 AND s.idSubject =?2")
    List<Quiz> getAllQuizByIdClassAndIdSubject(int idClass,String idSubject);
}