/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.ConfiguracionProyecto;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class ConfiguracionProyectoFacade extends AbstractDao<ConfiguracionProyecto> implements ConfiguracionProyectoFacadeLocal {

    public ConfiguracionProyectoFacade() {
        super(ConfiguracionProyecto.class);
    }

    @Override
    public ConfiguracionProyecto buscarConfDiasTrabajoAlMes(Long proyectoId) {
        List<ConfiguracionProyecto> configuracionProyectos = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT c FROM ConfiguracionProyecto c where " + "(c.proyectoId.id=:id AND c.codigo='DM')");
            query.setParameter("id", proyectoId);
            configuracionProyectos = query.getResultList();
            return !configuracionProyectos.isEmpty() ? configuracionProyectos.get(0) : null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public ConfiguracionProyecto buscarConfHorasTrabajoAlDia(Long proyectoId) {
        List<ConfiguracionProyecto> configuracionProyectos = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT c FROM ConfiguracionProyecto c where " + "(c.proyectoId.id=:id AND c.codigo='HD')");
            query.setParameter("id", proyectoId);
            configuracionProyectos = query.getResultList();
            return !configuracionProyectos.isEmpty() ? configuracionProyectos.get(0) : null;
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public ConfiguracionProyecto buscarCatalogoDuracion(Long proyectoId) {
        List<ConfiguracionProyecto> configuracionProyectos = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT c FROM ConfiguracionProyecto c where " + "(c.proyectoId.id=:id AND c.codigo='CD')");
            query.setParameter("id", proyectoId);
            configuracionProyectos = query.getResultList();
            return !configuracionProyectos.isEmpty() ? configuracionProyectos.get(0) : null;
        } catch (Exception e) {
        }
        return null;
    }
}
