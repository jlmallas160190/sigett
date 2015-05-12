/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.dao.implement;

import edu.jlmallas.academico.dao.AbstractDao;
import edu.jlmallas.academico.entity.TipoContrato;
import edu.jlmallas.academico.dao.TipoContratoDao;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class TipoContratoDaoImplement extends AbstractDao<TipoContrato> implements TipoContratoDao {

    public TipoContratoDaoImplement() {
        super(TipoContrato.class);
    }

    @Override
    public TipoContrato buscarPorNombre(String nombre) {
        List<TipoContrato> tipoContratos = new ArrayList<>();
        try {
            Query query = em.createNamedQuery("TipoContrato.findByNombre");
            query.setParameter("nombre", nombre);
            tipoContratos = query.getResultList();
            return !tipoContratos.isEmpty() ? tipoContratos.get(0) : null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
