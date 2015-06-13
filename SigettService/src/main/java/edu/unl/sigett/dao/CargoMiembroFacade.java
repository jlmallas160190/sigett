/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.CargoMiembro;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class CargoMiembroFacade extends AbstractDao<CargoMiembro> implements CargoMiembroFacadeLocal {

    public CargoMiembroFacade() {
        super(CargoMiembro.class);
    }

    @Override
    public List<CargoMiembro> buscarActivos() {
        try {
            Query query = em.createQuery("SELECT c FROM CargoMiembro c WHERE" + " (c.esActivo=TRUE)");
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public CargoMiembro buscarPorCodigo(String codigo) {
        List<CargoMiembro> cargos = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT c FROM CargoMiembro c WHERE" + " (c.codigo=:codigo)");
            query.setParameter("codigo", codigo);
            cargos = query.getResultList();
            return !cargos.isEmpty() ? cargos.get(0) : null;
        } catch (Exception e) {
        }
        return null;
    }
}
