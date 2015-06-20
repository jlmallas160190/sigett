/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.directorProyecto;

import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.service.ItemService;
import com.jlmallas.comun.service.PersonaService;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.jlmallas.academico.entity.DocenteCarrera;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import edu.jlmallas.academico.service.DocenteCarreraService;
import edu.jlmallas.academico.service.EstudianteCarreraService;
import edu.unl.sigett.director.DirectorDTO;
import edu.unl.sigett.entity.AutorProyecto;
import edu.unl.sigett.entity.Director;
import edu.unl.sigett.entity.DirectorProyecto;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.enumeration.EstadoAutorEnum;
import edu.unl.sigett.service.AutorProyectoService;
import edu.unl.sigett.service.DirectorProyectoService;
import edu.unl.sigett.service.DirectorService;
import edu.unl.sigett.util.SessionSelectItems;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;

/**
 *
 * @author jorge-luis
 */
@Named(value = "directorProyectoController")
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "directorProyectos",
            pattern = "/directorProyectos",
            viewId = "/faces/pages/sigett/directorProyecto/index.xhtml"
    ),
    @URLMapping(
            id = "editarDirectorProyecto",
            pattern = "/editarDirectorProyecto",
            viewId = "/faces/pages/sigett/directorProyecto/editarDirectorProyecto.xhtml"
    )
})
public class DirectorProyectoController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private SessionDirectorProyecto sessionDirectorProyecto;
    @Inject
    private SessionSelectItems sessionSelectItems;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB
    private DirectorProyectoService directorProyectoService;
    @EJB
    private DocenteCarreraService docenteCarreraService;
    @EJB
    private DirectorService directorService;
    @EJB
    private ItemService itemService;
    @EJB
    private AutorProyectoService autorProyectoService;
    @EJB
    private EstudianteCarreraService estudianteCarreraService;
    @EJB
    private PersonaService personaService;
//</editor-fold>

    public DirectorProyectoController() {
    }

    public void preRenderView() {
        this.listadoProyectos();
    }

    //<editor-fold defaultstate="collapsed" desc="CRUD">
    private void listadoProyectos() {
        this.sessionDirectorProyecto.getDirectoresProyectoDTO().clear();
        this.sessionDirectorProyecto.getFilterDirectoresProyectoDTO().clear();
        List<DirectorProyecto> directorProyectos = new ArrayList<>();
        for (DocenteCarrera docenteCarrera : sessionSelectItems.getDocenteCarreras()) {
            List<DirectorProyecto> lista = directorProyectoService.buscar(new DirectorProyecto(null, null, null, null, null, directorService.buscarPorId(new Director(docenteCarrera.getId()))));
            for (DirectorProyecto dp : lista) {
                if (!directorProyectos.contains(dp)) {
                    directorProyectos.add(dp);
                }
            }
        }
        for (DirectorProyecto directorProyecto : directorProyectos) {
            Item estado = itemService.buscarPorId(directorProyecto.getProyectoId().getEstadoProyectoId());
            Item tipo = itemService.buscarPorId(directorProyecto.getProyectoId().getTipoProyectoId());
            Item categoria = itemService.buscarPorId(directorProyecto.getProyectoId().getCatalogoProyectoId());
            directorProyecto.getProyectoId().setEstado(estado.getNombre());
            directorProyecto.getProyectoId().setTipo(tipo.getNombre());
            directorProyecto.getProyectoId().setCatalogo(categoria.getNombre());
            directorProyecto.getProyectoId().setAutores(autores(directorProyecto.getProyectoId()));
            DirectorProyectoDTO directorProyectoDTO = new DirectorProyectoDTO(directorProyecto, new DirectorDTO(directorProyecto.getDirectorId(),
                    docenteCarreraService.buscarPorId(new DocenteCarrera(directorProyecto.getDirectorId().getId())), null));
            directorProyectoDTO.getDirectorDTO().setPersona(personaService.buscarPorId(
                    new Persona(directorProyectoDTO.getDirectorDTO().getDocenteCarrera().getDocenteId().getId())));
            sessionDirectorProyecto.getDirectoresProyectoDTO().add(directorProyectoDTO);
        }
        sessionDirectorProyecto.setFilterDirectoresProyectoDTO(sessionDirectorProyecto.getDirectoresProyectoDTO());
    }

    private String autores(Proyecto proyecto) {
        String resultado = "";
        Item estadoRenunciado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOAUTOR.getTipo(), EstadoAutorEnum.RENUNCIADO.getTipo());
        List<AutorProyecto> autorProyectos = autorProyectoService.buscar(new AutorProyecto(proyecto, null, null, null, null));
        if (autorProyectos == null) {
            return "";
        }
        int contador = 0;
        for (AutorProyecto autorProyecto : autorProyectos) {
            if (autorProyecto.getEstadoAutorId().equals(estadoRenunciado.getId())) {
                continue;
            }
            EstudianteCarrera estudianteCarrera = estudianteCarreraService.buscarPorId(new EstudianteCarrera(autorProyecto.getAspiranteId().getId()));
            Persona persona = personaService.buscarPorId(new Persona(estudianteCarrera.getEstudianteId().getId()));
            if (contador == 0) {
                if (persona == null) {
                    continue;
                }
                resultado = (persona.getApellidos() + " " + persona.getNombres());
            } else {
                resultado = (resultado + ", " + persona.getApellidos() + " " + persona.getNombres());
            }
            contador++;
        }
        return resultado;
    }

    public String editar(DirectorProyectoDTO directorProyectoDTO) {
        sessionDirectorProyecto.setDirectorProyectoDTO(directorProyectoDTO);
        return "pretty:editarDirectorProyecto";
    }
    //</editor-fold>
}
