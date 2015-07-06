/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.directorProyecto;

import com.jlmallas.comun.entity.Configuracion;
import com.jlmallas.comun.entity.Documento;
import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.enumeration.ConfiguracionEnum;
import com.jlmallas.comun.service.ConfiguracionService;
import com.jlmallas.comun.service.DocumentoService;
import com.jlmallas.comun.service.ItemService;
import com.jlmallas.comun.service.PersonaService;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.jlmallas.academico.entity.Carrera;
import edu.jlmallas.academico.entity.DocenteCarrera;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import edu.jlmallas.academico.service.CarreraService;
import edu.jlmallas.academico.service.DocenteCarreraService;
import edu.jlmallas.academico.service.EstudianteCarreraService;
import edu.unl.sigett.director.DirectorDTO;
import edu.unl.sigett.documentoProyecto.DocumentoProyectoDM;
import edu.unl.sigett.documentoProyecto.DocumentoProyectoDTO;
import edu.unl.sigett.entity.AutorProyecto;
import edu.unl.sigett.entity.Director;
import edu.unl.sigett.entity.DirectorProyecto;
import edu.unl.sigett.entity.DocumentoProyecto;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.enumeration.CatalogoDocumentoProyectoEnum;
import edu.unl.sigett.enumeration.EstadoAutorEnum;
import edu.unl.sigett.enumeration.EstadoProyectoEnum;
import edu.unl.sigett.reporte.CertificadoReporte;
import edu.unl.sigett.reporte.ReporteController;
import edu.unl.sigett.service.AutorProyectoService;
import edu.unl.sigett.service.DirectorProyectoService;
import edu.unl.sigett.service.DirectorService;
import edu.unl.sigett.service.DocumentoProyectoService;
import edu.unl.sigett.service.ProyectoService;
import edu.unl.sigett.util.CabeceraController;
import edu.unl.sigett.util.PropertiesFileEnum;
import edu.unl.sigett.util.SessionSelectItems;
import java.io.File;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

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
    @Inject
    private CabeceraController cabeceraController;
    @Inject
    private DocumentoProyectoDM documentoProyectoDM;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB(lookup = "java:global/SigettService/DirectorProyectoServiceImplement!edu.unl.sigett.service.DirectorProyectoService")
    private DirectorProyectoService directorProyectoService;
    @EJB(lookup = "java:global/AcademicoService/DocenteCarreraServiceImplement!edu.jlmallas.academico.service.DocenteCarreraService")
    private DocenteCarreraService docenteCarreraService;
    @EJB(lookup = "java:global/SigettService/DirectorServiceImplement!edu.unl.sigett.service.DirectorService")
    private DirectorService directorService;
    @EJB(lookup = "java:global/ComunService/ItemServiceImplement!com.jlmallas.comun.service.ItemService")
    private ItemService itemService;
    @EJB(lookup = "java:global/SigettService/AutorProyectoServiceImplement!edu.unl.sigett.service.AutorProyectoService")
    private AutorProyectoService autorProyectoService;
    @EJB(lookup = "java:global/AcademicoService/EstudianteCarreraServiceImplement!edu.jlmallas.academico.service.EstudianteCarreraService")
    private EstudianteCarreraService estudianteCarreraService;
    @EJB(lookup = "java:global/ComunService/PersonaServiceImplement!com.jlmallas.comun.service.PersonaService")
    private PersonaService personaService;
    @EJB(lookup = "java:global/SigettService/ProyectoServiceImplement!edu.unl.sigett.service.ProyectoService")
    private ProyectoService proyectoService;
    @EJB(lookup = "java:global/SigettService/DocumentoProyectoServiceImplement!edu.unl.sigett.service.DocumentoProyectoService")
    private DocumentoProyectoService documentoProyectoService;
    @EJB(lookup = "java:global/ComunService/DocumentoServiceImplement!com.jlmallas.comun.service.DocumentoService")
    private DocumentoService documentoService;
    @EJB(lookup = "java:global/ComunService/ConfiguracionServiceImplement!com.jlmallas.comun.service.ConfiguracionService")
    private ConfiguracionService configuracionService;
    @EJB(lookup = "java:global/AcademicoService/CarreraServiceImplement!edu.jlmallas.academico.service.CarreraService")
    private CarreraService carreraService;
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

    /**
     * AUTORIZAR SUSTENTACIÓN PRIVADA DEL TRABAJO DE TITULACIÓN SELECCIONADO
     */
    public void autorizarFinalizacion() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        Item estadoProyecto = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOPROYECTO.getTipo(), EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo());
        sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorProyecto().getProyectoId().setEstadoProyectoId(estadoProyecto.getId());
        proyectoService.actualizar(sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorProyecto().getProyectoId());
        obtenerCertificado();
        this.cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("autorizar_tt") + ".", "");
    }

    public void actualizarDatosProyecto() {
        sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorProyecto().setProyectoId(proyectoService.buscarPorId(
                new Proyecto(sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorProyecto().getProyectoId().getId())));
        sessionDirectorProyecto.setRenderedBotonAutoriza(Boolean.FALSE);
        Item estadoProyecto = itemService.buscarPorId(sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorProyecto().getProyectoId().getEstadoProyectoId());
        Item tipoProyecto = itemService.buscarPorId(sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorProyecto().getProyectoId().getTipoProyectoId());
        Item categoria = itemService.buscarPorId(sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorProyecto().getProyectoId().getCatalogoProyectoId());
        sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorProyecto().getProyectoId().setEstado(estadoProyecto.getNombre());
        sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorProyecto().getProyectoId().setTipo(tipoProyecto.getNombre());
        sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorProyecto().getProyectoId().setCatalogo(categoria.getNombre());
        if (!estadoProyecto.getCodigo().equals(EstadoProyectoEnum.SUSTENTACIONPRIVADA.getTipo())) {
            sessionDirectorProyecto.setRenderedBotonAutoriza(Boolean.TRUE);
        }
    }

    public void verCertficado() {
        obtenerCertificado();
    }

    private void obtenerCertificado() {
        Item item = itemService.buscarPorCatalogoCodigo(CatalogoEnum.CATALOGODOCUMENTOPROYECTO.getTipo(),
                CatalogoDocumentoProyectoEnum.CERTIFICACIONDIRECTOR.getTipo());
        this.sessionDirectorProyecto.setRenderedMediaCertificado(Boolean.TRUE);
        Documento documentoBuscar = documentoService.buscarPorCatalogo(new Documento(null, null, item.getId(), null, null, null, null, null));
        if (documentoBuscar == null) {
            generarCertificado();
            return;
        }
        List<DocumentoProyecto> documentoProyectos = documentoProyectoService.buscar(new DocumentoProyecto(
                Boolean.TRUE, documentoBuscar.getId(), sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorProyecto().getProyectoId()));
        if (documentoProyectos == null) {
            generarCertificado();
            return;
        }
        DocumentoProyecto documentoProyecto = !documentoProyectos.isEmpty() ? documentoProyectos.get(0) : null;
        if (documentoProyecto != null) {
            sessionDirectorProyecto.setCerticadoDirector(new DocumentoProyectoDTO(documentoProyecto, documentoBuscar));
            File file = new File(sessionDirectorProyecto.getCerticadoDirector().getDocumento().getRuta());
            sessionDirectorProyecto.getCerticadoDirector().getDocumento().setContents(cabeceraController.getUtilService().obtenerBytes(file));
            sessionDirectorProyecto.setRenderedMediaCertificado(Boolean.TRUE);
            documentoProyectoDM.setDocumentoProyectoDTOSeleccionado(sessionDirectorProyecto.getCerticadoDirector());
            return;
        }
        generarCertificado();
    }

    /**
     * GENERAR NUEVO CERTIFICADO
     */
    public void generarCertificado() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        ReporteController reporteController = new ReporteController();
        String rutaReporte = request.getRealPath("/") + configuracionService.buscar(new Configuracion(
                ConfiguracionEnum.RUTAREPORTEFECERTIFICADO.getTipo())).get(0).getValor();
        Carrera carrera = carreraService.find(sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorDTO().getDocenteCarrera().getCarreraId().getId());
        Calendar fechaActual = Calendar.getInstance();
        byte[] resultado = reporteController.certificado(new CertificadoReporte(rutaReporte, carrera.getLugar(), cabeceraController.getUtilService().formatoFecha(
                fechaActual.getTime(), "EEEEE dd MMMMM yyyy"), cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "docente_carrera") + " " + carrera.getNombreTitulo(), sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorDTO().getDocenteCarrera().
                getDocenteId().getTituloDocenteId().getTituloId().getAbreviacion() + " "
                + sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorDTO().getPersona(), cuerpoCertificado(carrera), cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "certificado_titulo")));
        if (resultado == null) {
            return;
        }
        String ruta = configuracionService.buscar(new Configuracion(ConfiguracionEnum.RUTADOCUMENTOPROYECTO.getTipo())).get(0).getValor()
                + "/certificado_" + sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorProyecto().getId() + ".pdf";
        Documento documento = new Documento(null, ruta, itemService.buscarPorCatalogoCodigo(CatalogoEnum.CATALOGODOCUMENTOPROYECTO.getTipo(),
                CatalogoDocumentoProyectoEnum.CERTIFICACIONDIRECTOR.getTipo()).getId(), Double.parseDouble(resultado.length + ""), fechaActual.getTime(), resultado, null, "pdf");
        documentoService.guardar(documento);
        sessionDirectorProyecto.setCerticadoDirector(new DocumentoProyectoDTO(
                new DocumentoProyecto(Boolean.TRUE, documento.getId(), sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorProyecto().getProyectoId()), documento));
        documentoProyectoService.guardar(sessionDirectorProyecto.getCerticadoDirector().getDocumentoProyecto());
        sessionDirectorProyecto.setRenderedMediaCertificado(Boolean.TRUE);
        cabeceraController.getUtilService().generaDocumento(new File(ruta), documento.getContents());
        documentoProyectoDM.setDocumentoProyectoDTOSeleccionado(sessionDirectorProyecto.getCerticadoDirector());
    }

    /**
     * GENERAR EL CUERPO DEL INFORME DE PERTINENCIA
     *
     * @param pertinencia
     * @return
     */
    private String cuerpoCertificado(final Carrera carrera) {
        return (cabeceraController.getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "certificado_cuerpo_a") + "<br/>" + cabeceraController
                .getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "certificado_cuerpo_b") + " " + carrera.getNombreTitulo() + " " + cabeceraController.
                getValueFromProperties(PropertiesFileEnum.CONTENIDOREPORTE, "certificado_cuerpo_c") + ": <b> " + sessionDirectorProyecto.
                getDirectorProyectoDTO().getDirectorProyecto().getProyectoId().getTemaActual() + "<b/> " + cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "certificado_cuerpo_d") + " "
                + autores(sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorProyecto().getProyectoId()).toUpperCase() + ". " + cabeceraController.getValueFromProperties(
                        PropertiesFileEnum.CONTENIDOREPORTE, "certificado_cuerpo_e"));
    }

    public void cancelarFinalizacion() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        Item estadoProyecto = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADOPROYECTO.getTipo(), EstadoProyectoEnum.SEGUIMIENTO.getTipo());
        sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorProyecto().getProyectoId().setEstadoProyectoId(estadoProyecto.getId());
        proyectoService.actualizar(sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorProyecto().getProyectoId());
        this.cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("cancelar_tt") + ".", "");
    }

    public void cancelarImprimirCertificado() {
        sessionDirectorProyecto.setCerticadoDirector(new DocumentoProyectoDTO());
        sessionDirectorProyecto.setRenderedMediaCertificado(Boolean.FALSE);
        documentoProyectoDM.setDocumentoProyectoDTOSeleccionado(sessionDirectorProyecto.getCerticadoDirector());
    }
    //</editor-fold>

}
