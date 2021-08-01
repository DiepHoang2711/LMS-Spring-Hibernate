/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mockproject.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 *
 * @author Asus
 */
@Entity
@Data
@Table(name = "UsersOfClass")
public class UserOfClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUserOfClass;
    
    private int idUser;
    
    private int idClass;
}
