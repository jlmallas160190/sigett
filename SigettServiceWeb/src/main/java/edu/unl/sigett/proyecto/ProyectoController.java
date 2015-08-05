/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.proyecto;

import com.jlmallas.comun.entity.Documento;
import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.service.DocumentoService;
import com.jlmallas.comun.service.ItemService;
import com.jlmallas.comun.service.PersonaService;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.jlmallas.academico.entity.Area;
import edu.jlmallas.academico.entity.Carrera;
import edu.jlmallas.academico.entity.DocenteCarrera;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import edu.jlmallas.academico.entity.OfertaAcademica;
import edu.jlmallas.academico.service.AreaService;
import edu.jlmallas.academico.service.CarreraService;
import edu.jlmallas.academico.service.DocenteCarreraService;
import edu.jlmallas.academico.service.EstudianteCarreraService;
import edu.jlmallas.academico.service.OfertaAcademicaService;
import edu.unl.sigett.autorProyecto.AutorProyectoDTO;
import edu.unl.sigett.directorProyecto.DirectorDTO;
import edu.unl.sigett.directorProyecto.DirectorProyectoDTO;
import edu.unl.sigett.documentoProyecto.DocumentoProyectoDTO;
import edu.unl.sigett.entity.AutorProyecto;
import edu.unl.sigett.entity.DirectorProyecto;
import edu.unl.sigett.entity.DocumentoProyecto;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.entity.ProyectoCarreraOferta;
import edu.unl.sigett.enumeration.EstadoAutorEnum;
import edu.unl.sigett.enumeration.EstadoDirectorEnum;
import edu.unl.sigett.lud.dto.ProyectoOntDTO;
import edu.unl.sigett.service.AutorProyectoService;
import edu.unl.sigett.service.DirectorProyectoService;
import edu.unl.sigett.service.DocumentoProyectoService;
import edu.unl.sigett.service.ProyectoCarreraOfertaService;
import edu.unl.sigett.service.ProyectoService;
import edu.unl.sigett.util.CabeceraController;
import java.io.File;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Inject;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author jorge-luis
 */
@Named(value = "proyectoController")
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarProyecto",
            pattern = "/editarProyecto",
            viewId = "/faces/pages/sigett/proyecto/editarProyecto.xhtml"
    )
})
public class ProyectoController implements Serializable {

    @Inject
    private SessionProyecto sessionProyecto;
    @Inject
    private CabeceraController cabeceraController;
    @EJB(lookup = "java:global/SigettService/ProyectoServiceImplement!edu.unl.sigett.service.ProyectoService")
    private ProyectoService proyectoService;
    @EJB(lookup = "java:global/ComunService/ItemServiceImplement!com.jlmallas.comun.service.ItemService")
    private ItemService itemService;
    @EJB(lookup = "java:global/SigettService/ProyectoCarreraOfertaServiceImplement!edu.unl.sigett.service.ProyectoCarreraOfertaService")
    private ProyectoCarreraOfertaService proyectoCarreraOfertaService;
    @EJB(lookup = "java:global/ComunService/DocumentoServiceImplement!com.jlmallas.comun.service.DocumentoService")
    private DocumentoService documentoService;
    @EJB(lookup = "java:global/SigettService/DocumentoProyectoServiceImplement!edu.unl.sigett.service.DocumentoProyectoService")
    private DocumentoProyectoService documentoProyectoService;
    @EJB(lookup = "java:global/AcademicoService/OfertaAcademicaServiceImplement!edu.jlmallas.academico.service.OfertaAcademicaService")
    private OfertaAcademicaService ofertaAcademicaService;
    @EJB(lookup = "java:global/AcademicoService/EstudianteCarreraServiceImplement!edu.jlmallas.academico.service.EstudianteCarreraService")
    private EstudianteCarreraService estudianteCarreraService;
    @EJB(lookup = "java:global/ComunService/PersonaServiceImplement!com.jlmallas.comun.service.PersonaService")
    private PersonaService personaService;
    @EJB(lookup = "java:global/AcademicoService/DocenteCarreraServiceImplement!edu.jlmallas.academico.service.DocenteCarreraService")
    private DocenteCarreraService docenteCarreraService;
    @EJB(lookup = "java:global/AcademicoService/CarreraServiceImplement!edu.jlmallas.academico.service.CarreraService")
    private CarreraService carreraService;
    @EJB(lookup = "java:global/AcademicoService/AreaServiceImplement!edu.jlmallas.academico.service.AreaService")
    private AreaService areaService;
    @EJB(lookup = "java:global/SigettService/AutorProyectoServiceImplement!edu.unl.sigett.service.AutorProyectoService")
    private AutorProyectoService autorProyectoService;
    @EJB(lookup = "java:global/SigettService/DirectorProyectoServiceImplement!edu.unl.sigett.service.DirectorProyectoService")
    private DirectorProyectoService directorProyectoService;

    private static final Logger LOG = Logger.getLogger(ProyectoController.class.getName());

    public ProyectoController() {
    }

    public void preRenderView() {
        this.listadoTipos();
        this.listadoCategorias();
        this.listadoEstados();
        this.listadoCarreras();
        this.listadoOfertasAcademicas();
        listadoAreas();
    }

    public void buscar() {
        sessionProyecto.getProyectos().clear();
        sessionProyecto.getFilterProyectos().clear();
        try {
            List<ProyectoCarreraOferta> proyectoCarreraOfertas = proyectoCarreraOfertaService.buscar(
                    new ProyectoCarreraOferta(null, sessionProyecto.getCarreraSeleccionada().getId() != null
                            ? sessionProyecto.getCarreraSeleccionada().getId() : null, null, Boolean.TRUE));

            if (proyectoCarreraOfertas == null) {
                return;
            }
            for (ProyectoCarreraOferta proyectoCarreraOferta : proyectoCarreraOfertas) {
                proyectoCarreraOferta.getProyectoId().setEstado(itemService.buscarPorId(proyectoCarreraOferta.getProyectoId().
                        getEstadoProyectoId()).getNombre());
                proyectoCarreraOferta.getProyectoId().setCatalogo(itemService.buscarPorId(proyectoCarreraOferta.getProyectoId().
                        getCatalogoProyectoId()).getNombre());
                proyectoCarreraOferta.getProyectoId().setTipo(itemService.buscarPorId(proyectoCarreraOferta.getProyectoId().
                        getTipoProyectoId()).getNombre());
                proyectoCarreraOferta.getProyectoId().setAutores(autores(proyectoCarreraOferta.getProyectoId()));
                proyectoCarreraOferta.getProyectoId().setDirectores(directores(proyectoCarreraOferta.getProyectoId()));
                proyectoCarreraOferta.getProyectoId().setNombreOferta(ofertaAcademicaService.find(proyectoCarreraOferta.getOfertaAcademicaId()).getNombre());
                if (!this.sessionProyecto.getProyectos().contains(proyectoCarreraOferta.getProyectoId())) {
                    this.sessionProyecto.getProyectos().add(proyectoCarreraOferta.getProyectoId());
                }
            }
            sessionProyecto.setFilterProyectos(sessionProyecto.getProyectos());
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    private String autores(Proyecto proyecto) {
        String resultado = "";
        Item estadoRenunciado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOAUTOR.getTipo(), EstadoAutorEnum.RENUNCIADO.getTipo());
        if (proyecto.getAutorProyectoList() == null) {
            return "";
        }
        int contador = 0;
        for (AutorProyecto autorProyecto : proyecto.getAutorProyectoList()) {
            if (estadoRenunciado.getId().equals(autorProyecto.getEstadoAutorId())) {
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

    private String directores(Proyecto proyecto) {
        String resultado = "";
        Item estadoRenunciado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADODIRECTOR.getTipo(), EstadoDirectorEnum.RENUNCIADO.getTipo());
        if (proyecto.getDirectorProyectoList() == null) {
            return "";
        }
        int contador = 0;
        for (DirectorProyecto directorProyecto : proyecto.getDirectorProyectoList()) {
            if (estadoRenunciado.getId().equals(directorProyecto.getEstadoDirectorId())) {
                continue;
            }
            DocenteCarrera docenteCarrera = docenteCarreraService.buscarPorId(new DocenteCarrera(directorProyecto.getDirectorId().getId()));
            Persona persona = personaService.buscarPorId(new Persona(docenteCarrera.getDocenteId().getId()));
            if (contador == 0) {
                resultado = (persona.getApellidos() + " " + persona.getNombres());
            } else {
                resultado = (resultado + ", " + persona.getApellidos() + " " + persona.getNombres());
            }
            contador++;
        }
        return resultado;
    }

    public void listadoCarreras() {
        try {
            this.sessionProyecto.getCarreras().clear();
            this.sessionProyecto.getFilterCarreras().clear();
            this.sessionProyecto.getCarreras().addAll(carreraService.buscarPorCriterio(new Carrera()));
            this.sessionProyecto.setFilterCarreras(this.sessionProyecto.getCarreras());
        } catch (Exception e) {
        }
    }

    public void listadoAreas() {
        try {
            this.sessionProyecto.getAreas().clear();
            this.sessionProyecto.getAreas().addAll(areaService.buscarPorCriterio(new Area()));
        } catch (Exception e) {
            LOG.warning(e.getLocalizedMessage());
        }
    }

    /**
     * PERMITE BUSCAR PROYECTOS UTILIZANDO UN COINCIDENCIAS EN LOS GRAFOS
     */
    public void buscadorSemantico() {
        sessionProyecto.getProyectos().clear();
        sessionProyecto.getFilterProyectos().clear();
        cabeceraController.getOntologyService().getProyectoOntService().read(cabeceraController.getCabeceraWebSemantica());
        ProyectoOntDTO proyectoOntDTOBuscar = new ProyectoOntDTO();
        proyectoOntDTOBuscar.setAutor(sessionProyecto.getFiltroBuscadorSemantico());
        proyectoOntDTOBuscar.setCarrera(sessionProyecto.getFiltroBuscadorSemantico());
        proyectoOntDTOBuscar.setDocente(sessionProyecto.getFiltroBuscadorSemantico());
        proyectoOntDTOBuscar.setEstado(sessionProyecto.getFiltroBuscadorSemantico());
        proyectoOntDTOBuscar.setLineaInvestigacion(sessionProyecto.getFiltroBuscadorSemantico());
        proyectoOntDTOBuscar.setNivelAcademico(sessionProyecto.getFiltroBuscadorSemantico());
        proyectoOntDTOBuscar.setTema(sessionProyecto.getFiltroBuscadorSemantico());
        proyectoOntDTOBuscar.setTipo(sessionProyecto.getFiltroBuscadorSemantico());
        List<ProyectoOntDTO> proyectoOntDTOs = cabeceraController.getOntologyService().getProyectoOntService().buscar(proyectoOntDTOBuscar);
        for (ProyectoOntDTO proyectoOntDTO : proyectoOntDTOs) {
            Proyecto proyecto = proyectoService.buscarPorId(new Proyecto(proyectoOntDTO.getId()));
            proyecto.setAutores(autores(proyecto));
            proyecto.setDirectores(directores(proyecto));
            proyecto.setEstado(itemService.buscarPorId(proyecto.getEstadoProyectoId()).getNombre());
            proyecto.setCatalogo(itemService.buscarPorId(proyecto.getCatalogoProyectoId()).getNombre());
            proyecto.setTipo(itemService.buscarPorId(proyecto.getTipoProyectoId()).getNombre());
            proyecto.setNombreOferta(ofertaAcademicaService.find(proyectoOntDTO.getOfertaAcademicaId()).getNombre());
            if (!sessionProyecto.getProyectos().contains(proyecto)) {
                sessionProyecto.getProyectos().add(proyecto);
            }
        }
    }

    @SuppressWarnings("CallToThreadDumpStack")
    public void listadoOfertasAcademicas() {
        this.sessionProyecto.getOfertaAcademicas().clear();
        try {
            for (Carrera carrera : sessionProyecto.getCarreras()) {
                for (ProyectoCarreraOferta pco : proyectoCarreraOfertaService.buscar(
                        new ProyectoCarreraOferta(null, carrera.getId(), null, Boolean.TRUE))) {
                    OfertaAcademica ofertaAcademica = ofertaAcademicaService.find(pco.getOfertaAcademicaId());
                    if (!this.sessionProyecto.getOfertaAcademicas().contains(ofertaAcademica)) {
                        this.sessionProyecto.getOfertaAcademicas().add(ofertaAcademica);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void seleccionarCarrera(SelectEvent event) {
        try {
            sessionProyecto.setCarreraSeleccionada((Carrera) event.getObject());
            buscar();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void listadoCategorias() {
        sessionProyecto.getCategorias().clear();
        sessionProyecto.setCategorias(itemService.buscarPorCatalogo(CatalogoEnum.CATALOGOPROYECTO.getTipo()));
    }

    /**
     * LISTAR ESTADO DE PROYECTO
     */
    private void listadoEstados() {
        sessionProyecto.getEstados().clear();
        sessionProyecto.setEstados(itemService.buscarPorCatalogo(CatalogoEnum.ESTADOPROYECTO.getTipo()));
    }

    private void listadoTipos() {
        sessionProyecto.getTipos().clear();
        sessionProyecto.setTipos(itemService.buscarPorCatalogo(CatalogoEnum.TIPOPROYECTO.getTipo()));
    }

    public String editarProyecto(Proyecto proyecto) {
        iniciar();
        sessionProyecto.setProyectoSeleccionado(proyecto);
        listadoDocumentos();
        buscarAutores();
        buscarDirectores();
        return "pretty:editarProyecto";
    }

    private void listadoDocumentos() {
        try {
            List<DocumentoProyecto> documentoProyectos = this.documentoProyectoService.buscar(
                    new DocumentoProyecto(Boolean.TRUE, null, sessionProyecto.getProyectoSeleccionado()));
            for (DocumentoProyecto documentoProyecto : documentoProyectos) {
                DocumentoProyectoDTO documentoProyectoDTO = new DocumentoProyectoDTO(
                        documentoProyecto, documentoService.buscarPorId(new Documento(
                                        documentoProyecto.getDocumentoId(), null, null, null, null, null, null, null,documentoProyecto.getDocumentoId())));
                documentoProyectoDTO.getDocumento().setCatalogo(itemService.buscarPorId(documentoProyectoDTO.getDocumento().getCatalogoId()).getNombre());
                documentoProyectoDTO.getDocumento().setContents(cabeceraController.getUtilService().obtenerBytes(new File(documentoProyectoDTO.getDocumento().getRuta())));
                sessionProyecto.getDocumentosProyectoDTO().add(documentoProyectoDTO);
            }
            sessionProyecto.setFilterDocumentosProyectoDTO(sessionProyecto.getDocumentosProyectoDTO());
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    private void iniciar() {
        sessionProyecto.getDocumentosProyectoDTO().clear();
        sessionProyecto.getFilterDocumentosProyectoDTO().clear();
        sessionProyecto.getAutoresProyectoDTO().clear();
        sessionProyecto.getFilterAutoresProyectoDTO().clear();
        sessionProyecto.getDirectoresProyectoDTO().clear();
        sessionProyecto.getFilterAutoresProyectoDTO().clear();
    }

    private void buscarAutores() {

        Item estadoRenunciado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOAUTOR.getTipo(), EstadoAutorEnum.RENUNCIADO.getTipo());
        List<AutorProyecto> autorProyectos = autorProyectoService.buscar(new AutorProyecto(sessionProyecto.getProyectoSeleccionado(), null, null, null, null));
        if (autorProyectos == null) {
            return;
        }
        for (AutorProyecto autorProyecto : autorProyectos) {
            if (estadoRenunciado.getId().equals(autorProyecto.getEstadoAutorId())) {
                continue;
            }
            EstudianteCarrera estudianteCarrera = estudianteCarreraService.buscarPorId(new EstudianteCarrera(autorProyecto.getAspiranteId().getId()));
            estudianteCarrera.setEstado(itemService.buscarPorId(estudianteCarrera.getEstadoId()).getNombre());
            Persona persona = personaService.buscarPorId(new Persona(estudianteCarrera.getEstudianteId().getId()));
            AutorProyectoDTO autorProyectoDTO = new AutorProyectoDTO(autorProyecto, autorProyecto.getAspiranteId(), estudianteCarrera, persona);
            if (!sessionProyecto.getAutoresProyectoDTO().contains(autorProyectoDTO)) {
                sessionProyecto.getAutoresProyectoDTO().add(autorProyectoDTO);
            }
        }
        sessionProyecto.setFilterAutoresProyectoDTO(sessionProyecto.getAutoresProyectoDTO());
    }

    private void buscarDirectores() {

        Item estadoRenunciado = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADODIRECTOR.getTipo(), EstadoDirectorEnum.RENUNCIADO.getTipo());
        List<DirectorProyecto> directorProyectos = directorProyectoService.buscar(new DirectorProyecto(null, null, null, sessionProyecto.getProyectoSeleccionado(), null, null));
        if (directorProyectos == null) {
            return;
        }
        for (DirectorProyecto directorProyecto : directorProyectos) {
            if (estadoRenunciado.getId().equals(directorProyecto.getEstadoDirectorId())) {
                continue;
            }
            DocenteCarrera docenteCarrera = docenteCarreraService.buscarPorId(new DocenteCarrera(directorProyecto.getDirectorId().getId()));
            Persona persona = personaService.buscarPorId(new Persona(docenteCarrera.getDocenteId().getId()));
            DirectorProyectoDTO directorProyectoDTO = new DirectorProyectoDTO(directorProyecto,
                    new DirectorDTO(directorProyecto.getDirectorId(), docenteCarrera, persona));
            if (!sessionProyecto.getDirectoresProyectoDTO().contains(directorProyectoDTO)) {
                sessionProyecto.getDirectoresProyectoDTO().add(directorProyectoDTO);
            }
        }
        sessionProyecto.setFilterDirectoresProyectoDTO(sessionProyecto.getDirectoresProyectoDTO());
    }

    public List<Proyecto> completarProyectos(final String query) {
        List<Proyecto> results = new ArrayList<>();
        if (!"".equals(query.trim())) {
            sessionProyecto.setFiltroBuscadorSemantico(query);
            buscadorSemantico();
            results.addAll(sessionProyecto.getProyectos());
            ProyectoConverter.setProyectos(results);
        }
        return results;
    }
}
