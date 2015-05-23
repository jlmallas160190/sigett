/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.ProyectoCarreraOferta;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface ProyectoOfertaCarreraDao {

    void create(ProyectoCarreraOferta proyectoCarreraOferta);

    void edit(ProyectoCarreraOferta proyectoCarreraOferta);

    void remove(ProyectoCarreraOferta proyectoCarreraOferta);

    ProyectoCarreraOferta find(Object id);

    List<ProyectoCarreraOferta> findAll();

    List<ProyectoCarreraOferta> findRange(int[] range);

    List<ProyectoCarreraOferta> buscarPorCarreraOferta(Integer carreraId, Integer periodoId);

    List<ProyectoCarreraOferta> buscarPorCarrera(Integer carreraId);

    List<ProyectoCarreraOferta> buscarPorProyecto(Long proyectoId);

    List<ProyectoCarreraOferta> buscar(ProyectoCarreraOferta proyectoCarreraOferta);

    int count();

}
