/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.documentoProyecto;

import com.jlmallas.comun.entity.Configuracion;
import com.jlmallas.comun.entity.Documento;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.enumeration.ConfiguracionEnum;
import com.jlmallas.comun.service.ConfiguracionService;
import com.jlmallas.comun.service.DocumentoService;
import com.jlmallas.comun.service.ItemService;
import edu.unl.sigett.directorProyecto.SessionDirectorProyecto;
import edu.unl.sigett.entity.DocumentoProyecto;
import edu.unl.sigett.enumeration.CatalogoDocumentoProyectoEnum;
import edu.unl.sigett.service.DocumentoProyectoService;
import edu.unl.sigett.util.CabeceraController;
import java.io.File;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author jorge-luis
 */
@Named(value = "documentoProyectoController")
@SessionScoped
public class DocumentoProyectoController implements Serializable {

    @Inject
    private DocumentoProyectoDM documentoProyectoDM;
    @Inject
    private SessionDirectorProyecto sessionDirectorProyecto;
    @Inject
    private CabeceraController cabeceraController;
    private static final Logger LOG = Logger.getLogger(DocumentoProyectoController.class.getName());
    @EJB
    private ConfiguracionService configuracionService;
    @EJB
    private DocumentoService documentoService;
    @EJB
    private DocumentoProyectoService documentoProyectoService;
    @EJB
    private ItemService itemService;

    public DocumentoProyectoController() {
    }

    public void editar(DocumentoProyectoDTO documentoProyectoDTO) {
        try {
            File file = new File(documentoProyectoDTO.getDocumento().getRuta());
            documentoProyectoDTO.getDocumento().setContents(cabeceraController.getUtilService().obtenerBytes(file));
            documentoProyectoDM.setDocumentoProyectoDTOSeleccionado(documentoProyectoDTO);
            documentoProyectoDM.setRenderedMediaDocumentoProyecto(Boolean.TRUE);
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }

    public void cancelarEdicion() {
        documentoProyectoDM.setDocumentoProyectoDTOSeleccionado(new DocumentoProyectoDTO());
        documentoProyectoDM.setRenderedMediaDocumentoProyecto(Boolean.FALSE);
    }

    public void grabar() {
        Calendar fechaActual = Calendar.getInstance();
        String ruta = configuracionService.buscar(new Configuracion(ConfiguracionEnum.RUTADOCUMENTOPROYECTO.getTipo())).get(0).getValor()
                + "/certificado_" + sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorProyecto().getId() + ".pdf";
        Documento documento = new Documento(null, ruta, itemService.buscarPorCatalogoCodigo(CatalogoEnum.CATALOGODOCUMENTOPROYECTO.getTipo(),
                CatalogoDocumentoProyectoEnum.CERTIFICACIONDIRECTOR.getTipo()).getId(), Double.parseDouble(
                        documentoProyectoDM.getDocumentoProyectoDTOSeleccionado().getDocumento().getContents().length + ""), fechaActual.getTime(),
                documentoProyectoDM.getDocumentoProyectoDTOSeleccionado().getDocumento().getContents(), null, "pdf");
        documentoService.guardar(documento);
        sessionDirectorProyecto.setCerticadoDirector(new DocumentoProyectoDTO(
                new DocumentoProyecto(Boolean.TRUE, documento.getId(), sessionDirectorProyecto.getDirectorProyectoDTO().getDirectorProyecto().getProyectoId()), documento));
        documentoProyectoService.guardar(sessionDirectorProyecto.getCerticadoDirector().getDocumentoProyecto());
        sessionDirectorProyecto.setRenderedMediaCertificado(Boolean.TRUE);
        cabeceraController.getUtilService().generaDocumento(new File(ruta), documento.getContents());
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            documentoProyectoDM.getDocumentoProyectoDTOSeleccionado().getDocumento().setTipo("pdf");
            documentoProyectoDM.getDocumentoProyectoDTOSeleccionado().getDocumento().setContents(event.getFile().getContents());
            Long size = event.getFile().getSize();
            documentoProyectoDM.getDocumentoProyectoDTOSeleccionado().getDocumento().setTamanio(size.doubleValue());
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.uploaded"), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            grabar();
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }
}
