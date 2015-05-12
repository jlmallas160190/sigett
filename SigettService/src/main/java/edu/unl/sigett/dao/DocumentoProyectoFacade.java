/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.DocumentoProyecto;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class DocumentoProyectoFacade extends AbstractDao<DocumentoProyecto> implements DocumentoProyectoFacadeLocal {

    public DocumentoProyectoFacade() {
        super(DocumentoProyecto.class);
    }

    @Override
    public List<DocumentoProyecto> buscarPorProyecto(Long proyectoId) {
        try {
            Query query = em.createQuery("Select doc from DocumentoProyecto doc WHERE " + "(doc.proyectoId.id=:id)");
            query.setParameter("id", proyectoId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
