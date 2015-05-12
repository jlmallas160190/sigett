/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.dao;

import edu.jlmallas.academico.entity.EstadoLaboral;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface EstadoLaboralDao {

    void create(EstadoLaboral estadoLaboral);

    void edit(EstadoLaboral estadoLaboral);

    void remove(EstadoLaboral estadoLaboral);

    EstadoLaboral find(Object id);

    List<EstadoLaboral> findAll();

    List<EstadoLaboral> findRange(int[] range);

    EstadoLaboral buscarPorTipoContratoNombre(String nombre);

    List<EstadoLaboral> buscar(EstadoLaboral estadoLaboral);

    int count();

}
