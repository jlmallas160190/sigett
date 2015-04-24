/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service;

import edu.jlmallas.academico.entity.Area;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface AreaService {

    void guardar(Area area);

    void actualizar(Area area);

    void eliminar(Area area);

    Area buscarPorId(Integer id);

    List<Area> findAll();

    List<Area> buscarPorCriterio(Area area);

}
