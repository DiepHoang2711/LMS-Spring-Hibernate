/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mockproject.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;

/**
 *
 * @author Asus
 */
@Entity
@Data
@Table(name = "Subjects")
public class Subject {
    
    @Id
    private String idSubject;
    
    private int idUser;
    
    private String nameSubject;
    
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    @Column(name = "createDate")
    private String createDate;
    
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<Quiz> quizes;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subject")
    private Set<Class> classes;
}