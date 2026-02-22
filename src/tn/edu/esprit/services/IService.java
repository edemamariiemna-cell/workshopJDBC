/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.esprit.services;

import java.util.List;

/**
 *
 * @author abdelazizmezri
 */
public interface IService<T> {
    public int ajouter(T t);  // retourne l'ID généré
    public void modifier(T t);
    public void supprimer(int id);
    public T getOne(int id);
    public List<T> getAll();
}

