/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.ConfiguracionCarreraDao;
import edu.unl.sigett.entity.ConfiguracionCarrera;
import edu.unl.sigett.service.ConfiguracionCarreraService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class ConfiguracionCarreraServiceImplement implements ConfiguracionCarreraService {
    
    @EJB
    private ConfiguracionCarreraDao configuracionCarreraDao;
    
    @Override
    public void guardar(final ConfiguracionCarrera configuracionCarrera) {
        configuracionCarreraDao.create(configuracionCarrera);
    }
    
    @Override
    public void actualizar(final ConfiguracionCarrera configuracionCarrera) {
        configuracionCarreraDao.edit(configuracionCarrera);
    }
    
    @Override
    public List<ConfiguracionCarrera> buscar(final ConfiguracionCarrera configuracionCarrera) {
        return configuracionCarreraDao.buscar(configuracionCarrera);
    }
    
    @Override
    public ConfiguracionCarrera buscarPrimero(ConfiguracionCarrera configuracionCarrera) {
        List<ConfiguracionCarrera> configuracionCarreras = configuracionCarreraDao.buscar(configuracionCarrera);
        if (configuracionCarreras == null) {
            return null;
        }
        return !configuracionCarreras.isEmpty() ? configuracionCarreras.get(0) : null;
    }
    
    @Override
    public ConfiguracionCarrera buscarPorId(final ConfiguracionCarrera configuracionCarrera) {
        return configuracionCarreraDao.find(configuracionCarrera.getCarreraId());
    }
    
}
