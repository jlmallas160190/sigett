/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.RenunciaDirectorDao;
import edu.unl.sigett.entity.RenunciaDirector;
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
public class RenunciaDirectorDaoImplement extends AbstractDao<RenunciaDirector> implements RenunciaDirectorDao {

    public RenunciaDirectorDaoImplement() {
        super(RenunciaDirector.class);
    }

    @Override
    public List<RenunciaDirector> buscar(final RenunciaDirector renunciaDirector) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("SELECT r FROM RenunciaDirector r WHERE 1=1 ");
        if (renunciaDirector.getDirectorProyectoId() != null) {
            sql.append(" and r.directorProyectoId=:directorProyectoId");
            parametros.put("directorProyectoId", renunciaDirector.getDirectorProyectoId());
            existeFiltro = Boolean.TRUE;
        }
        if (renunciaDirector.getRenuncia() != null) {
            sql.append(" and r.renuncia=:renuncia");
            parametros.put("renuncia", renunciaDirector.getRenuncia());
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
