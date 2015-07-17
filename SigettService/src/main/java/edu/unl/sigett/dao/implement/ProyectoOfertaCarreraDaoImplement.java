/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.ProyectoOfertaCarreraDao;
import edu.unl.sigett.entity.ProyectoCarreraOferta;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class ProyectoOfertaCarreraDaoImplement extends AbstractDao<ProyectoCarreraOferta> implements ProyectoOfertaCarreraDao {

    public ProyectoOfertaCarreraDaoImplement() {
        super(ProyectoCarreraOferta.class);
    }

   

    @Override
    public List<ProyectoCarreraOferta> buscar(ProyectoCarreraOferta proyectoCarreraOferta) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("SELECT pco from ProyectoCarreraOferta pco WHERE 1=1");
        if (proyectoCarreraOferta.getCarreraId() != null) {
            sql.append(" and pco.carreraId=:carreraId ");
            parametros.put("carreraId", proyectoCarreraOferta.getCarreraId());
            existeFiltro = Boolean.TRUE;
        }
        if (proyectoCarreraOferta.getEsActivo() != null) {
            sql.append(" and pco.esActivo=:activo ");
            parametros.put("activo", proyectoCarreraOferta.getEsActivo());
        }
        if (proyectoCarreraOferta.getProyectoId() != null) {
            sql.append(" and pco.proyectoId=:proyecto ");
            parametros.put("proyecto", proyectoCarreraOferta.getProyectoId());
            existeFiltro = Boolean.TRUE;
        }
        if (proyectoCarreraOferta.getOfertaAcademicaId() != null) {
            sql.append(" and pco.ofertaAcademicaId=:ofertaId ");
            parametros.put("ofertaId", proyectoCarreraOferta.getOfertaAcademicaId());
            existeFiltro = Boolean.TRUE;
        }
        if (!existeFiltro) {
            return new ArrayList<>();
        }
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        return q.getResultList();
    }

}
