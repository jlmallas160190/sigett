/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service;

import edu.jlmallas.academico.entity.CoordinadorPeriodo;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface CoordinadorPeriodoFacadeLocal {

    void create(CoordinadorPeriodo coordinadorPeriodo);

    void edit(CoordinadorPeriodo coordinadorPeriodo);

    void remove(CoordinadorPeriodo coordinadorPeriodo);

    CoordinadorPeriodo find(Object id);

    List<CoordinadorPeriodo> findAll();

    List<CoordinadorPeriodo> findRange(int[] range);

    List<CoordinadorPeriodo> buscarPorCarrera(Integer carreraId);

    CoordinadorPeriodo buscarVigente(Integer carreraId);

    int count();

}
