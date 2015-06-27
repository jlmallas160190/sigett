/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.MiembroTribunal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class MiembroFacade extends AbstractDao<MiembroTribunal> implements MiembroFacadeLocal {

    public MiembroFacade() {
        super(MiembroTribunal.class);
    }

    @Override
    public List<MiembroTribunal> buscarPorTribunal(Long tribunalId) {
        try {
            Query query = em.createQuery("SELECT m FROM Miembro m WHERE" + " (m.tribunalId.id=:id and m.esActivo=TRUE)");
            query.setParameter("id", tribunalId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<MiembroTribunal> buscarPorDocente(Long docenteId) {
        try {
            Query query = em.createQuery("SELECT m FROM Miembro m WHERE" + " (m.docenteId.id=:id and m.esActivo=TRUE) order by m.id DESC");
            query.setParameter("id", docenteId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
