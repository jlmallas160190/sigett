/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.Actividad;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface ActividadFacadeLocal {

    void create(Actividad actividad);

    void edit(Actividad actividad);

    void remove(Actividad actividad);

    Actividad find(Object id);

    List<Actividad> findAll();

    List<Actividad> findRange(int[] range);

    List<Actividad> buscarPorProyecto(Long cronogramaId);

    List<Actividad> buscarPorRevisar(Long cronogramaId);

    List<Actividad> buscarRevisadas(Long cronogramaId);

    List<Actividad> buscarEnDesarrollo(Long cronogramaId);

    List<Actividad> buscarPorDirectorProyecto(String numeroIdentificacion);

    List<Actividad> buscarPorRevisarDirectorProyecto(String numeroIdentificacion);

    List<Actividad> buscarPorAutorProyecto(String numeroIdentificacion);

    double sumatoriaActividades(Long actividadId, Long cronogramaId);

    double sumatoriaSubActividades(Long actividadId, Long actividadPadreId);

    List<Actividad> buscarSubActividades(Long actividadId);

    int count();

}
