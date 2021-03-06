/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service;

import edu.jlmallas.academico.entity.Nivel;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface NivelService {

    void create(Nivel nivel);

    void edit(Nivel nivel);

    void remove(Nivel nivel);

    Nivel find(Object id);

    List<Nivel> findAll();

    List<Nivel> findRange(int[] range);

    Nivel buscarPorNombre(Nivel nivel);

    int count();

}
