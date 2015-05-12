/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.Proyecto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface ProyectoFacadeLocal {

    void create(Proyecto proyecto);

    void edit(Proyecto proyecto);

    void remove(Proyecto proyecto);

    Proyecto find(Object id);

    List<Proyecto> findAll();

    List<Proyecto> findRange(int[] range);

    List<Proyecto> buscarPorEstado();

    List<Proyecto> buscarPorTema(String criterio);

    List<Proyecto> buscarByEstado(Integer estadoId);

    List<Proyecto> buscarByCarrera(Integer carreraId);

    List<Proyecto> buscarByLi(Long liId);

    List<Proyecto> buscarByPeriodo(Integer periodoId);

    List<Proyecto> buscarPorCarreraEstado(Integer carreraId, String codigoEstado);

    List<Proyecto> buscarPorCulminar(Integer carreraId);

    List<Proyecto> buscarCaducados(Integer carreraId);

    int count();

}
