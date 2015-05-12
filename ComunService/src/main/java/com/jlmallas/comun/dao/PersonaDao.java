/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.dao;

import com.jlmallas.comun.entity.Persona;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface PersonaDao {

    void create(Persona persona);

    void edit(Persona persona);

    void remove(Persona persona);

    Persona find(Object id);

    List<Persona> findAll();

    List<Persona> findRange(int[] range);

    Persona buscarPorNumeroIdentificacion(String numeroIdentificacion);

    boolean esUnico(String numeroIdentificacion, Long id);

    int count();

}
