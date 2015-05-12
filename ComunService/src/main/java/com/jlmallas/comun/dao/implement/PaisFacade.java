/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.dao.implement;

import com.jlmallas.comun.entity.Pais;
import com.jlmallas.comun.dao.AbstractDao;
import com.jlmallas.comun.dao.PaisFacadeLocal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class PaisFacade extends AbstractDao<Pais> implements PaisFacadeLocal {

    public PaisFacade() {
        super(Pais.class);
    }

    @Override
    public Pais buscarPorNombre(String nombre) {
        try {
            List<Pais> paises = new ArrayList<>();
            Query query = em.createQuery("SELECT p FROM Pais p WHERE" + " (LOWER(p.nombre)=LOWER(:nombre))");
            query.setParameter("nombre", nombre);
            paises = query.getResultList();
            return !paises.isEmpty() ? paises.get(0) : null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public Pais buscarPorCodigo(String codigo) {
        try {
            List<Pais> paises = new ArrayList<>();
            Query query = em.createQuery("SELECT p FROM Pais p WHERE" + " (LOWER(p.codigo)=LOWER(:codigo))");
            query.setParameter("codigo", codigo);
            paises = query.getResultList();
            return !paises.isEmpty() ? paises.get(0) : null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
