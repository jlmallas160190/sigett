/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.EvaluacionTribunalDao;
import edu.unl.sigett.dao.ProyectoOfertaCarreraDao;
import edu.unl.sigett.entity.EvaluacionTribunal;
import edu.unl.sigett.entity.ProyectoCarreraOferta;
import edu.unl.sigett.entity.Tribunal;
import edu.unl.sigett.service.EvaluacionTribunalService;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class EvaluacionTribunalServiceImplement implements EvaluacionTribunalService {

    @EJB
    private EvaluacionTribunalDao evaluacionTribunalDao;
    @EJB
    private ProyectoOfertaCarreraDao proyectoOfertaCarreraDao;

    @Override
    public void guardar(final EvaluacionTribunal evaluacionTribunal) {
        this.evaluacionTribunalDao.create(evaluacionTribunal);
    }

    @Override
    public void actualizar(final EvaluacionTribunal evaluacionTribunal) {
        this.evaluacionTribunalDao.edit(evaluacionTribunal);
    }

    @Override
    public void eliminar(final EvaluacionTribunal evaluacionTribunal) {
        this.evaluacionTribunalDao.remove(evaluacionTribunal);
    }

    @Override
    public EvaluacionTribunal buscarPorId(final EvaluacionTribunal evaluacionTribunal) {
        return this.evaluacionTribunalDao.find(evaluacionTribunal.getId());
    }

    @Override
    public List<EvaluacionTribunal> buscar(final EvaluacionTribunal evaluacionTribunal) {
        List<EvaluacionTribunal> evaluacionTribunals = evaluacionTribunalDao.buscar(evaluacionTribunal);
        if (evaluacionTribunals == null) {
            return new ArrayList<>();
        }
        return evaluacionTribunals;
    }

    @Override
    public List<EvaluacionTribunal> buscarPorCarrera(Integer carreraId) {
        List<EvaluacionTribunal> evaluacionTribunals = new ArrayList<>();
        ProyectoCarreraOferta proyectoCarreraOfertaBuscar = new ProyectoCarreraOferta();
        proyectoCarreraOfertaBuscar.setCarreraId(carreraId);
        List<ProyectoCarreraOferta> pcos = proyectoOfertaCarreraDao.buscar(proyectoCarreraOfertaBuscar);
        for (ProyectoCarreraOferta proyectoCarreraOferta : pcos) {
            for (Tribunal tribunal : proyectoCarreraOferta.getProyectoId().getTribunalList()) {
                EvaluacionTribunal evaluacionTribunalBuscar = new EvaluacionTribunal();
                evaluacionTribunalBuscar.setTribunalId(tribunal);
                List<EvaluacionTribunal> evaluaciones = evaluacionTribunalDao.buscar(evaluacionTribunalBuscar);
                for (EvaluacionTribunal evaluacionTribunal : evaluaciones) {
                    evaluacionTribunals.add(evaluacionTribunal);
                }
            }
        }
        return evaluacionTribunals;
    }

}
