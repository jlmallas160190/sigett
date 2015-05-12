/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.DocenteUsuarioDao;
import edu.unl.sigett.entity.DocenteUsuario;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class DocenteUsuarioDaoImplement extends AbstractDao<DocenteUsuario> implements DocenteUsuarioDao {

    public DocenteUsuarioDaoImplement() {
        super(DocenteUsuario.class);
    }

    @Override
    public DocenteUsuario buscarPorDocente(Long id) {
        List<DocenteUsuario> docenteUsuarios = new ArrayList<>();
        try {
            Query query = em.createQuery("Select d from DocenteUsuario d where " + "d.docenteId.id=:id");
            query.setParameter("id", id);
            docenteUsuarios = query.getResultList();
            return !docenteUsuarios.isEmpty() ? docenteUsuarios.get(0) : null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
