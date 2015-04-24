/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.ConfiguracionProyecto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface ConfiguracionProyectoFacadeLocal {

    void create(ConfiguracionProyecto configuracionProyecto);

    void edit(ConfiguracionProyecto configuracionProyecto);

    void remove(ConfiguracionProyecto configuracionProyecto);

    ConfiguracionProyecto find(Object id);

    List<ConfiguracionProyecto> findAll();

    List<ConfiguracionProyecto> findRange(int[] range);

    ConfiguracionProyecto buscarConfDiasTrabajoAlMes(Long proyectoId);

    ConfiguracionProyecto buscarConfHorasTrabajoAlDia(Long proyectoId);

    ConfiguracionProyecto buscarCatalogoDuracion(Long proyectoId);

    int count();

}
