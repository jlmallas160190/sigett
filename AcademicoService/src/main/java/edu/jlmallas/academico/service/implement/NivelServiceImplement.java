/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service.implement;

import edu.jlmallas.academico.dao.AbstractDao;
import edu.jlmallas.academico.dao.NivelDao;
import edu.jlmallas.academico.entity.Nivel;
import edu.jlmallas.academico.service.NivelService;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class NivelServiceImplement extends AbstractDao<Nivel> implements NivelService {

    @EJB
    private NivelDao nivelDao;

    public NivelServiceImplement() {
        super(Nivel.class);
    }

    @Override
    public Nivel buscarPorNombre(final Nivel nivel) {
        @SuppressWarnings("UnusedAssignment")
        List<Nivel> niveles = new ArrayList<>();
        niveles = nivelDao.buscar(nivel);
        if (niveles == null) {
            return null;
        }
        return !niveles.isEmpty() ? niveles.get(0) : null;
    }
}
