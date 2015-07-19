/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.evaluacionTribunal;

import com.jlmallas.comun.service.ItemService;
import com.jlmallas.comun.service.PersonaService;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.jlmallas.academico.service.DocenteService;
import edu.unl.sigett.docenteUsuario.DocenteUsuarioDM;
import edu.unl.sigett.entity.EvaluacionTribunal;
import edu.unl.sigett.entity.MiembroTribunal;
import edu.unl.sigett.entity.Tribunal;
import edu.unl.sigett.miembroTribunal.MiembroTribunalDTO;
import edu.unl.sigett.service.EvaluacionTribunalService;
import edu.unl.sigett.service.MiembroTribunalService;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;

/**
 *
 * @author jorge-luis
 */
@Named(value = "evaluacionTribunalController")
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarEvaluacionTribunal",
            pattern = "/editarEvaluacionTribunal/",
            viewId = "/faces/pages/sigett/evaluacionTribunal/editarEvaluacionTribunal.xhtml"
    ),
    @URLMapping(
            id = "evaluaciones",
            pattern = "/evaluaciones/",
            viewId = "/faces/pages/sigett/evaluacionTribunal/index.xhtml"
    )
})
public class EvaluacionTribunalController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private SessionEvaluacionTribunal sessionEvaluacionTribunal;
    @Inject
    private DocenteUsuarioDM docenteUsuarioDM;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB(lookup = "java:global/SigettService/EvaluacionTribunalServiceImplement!edu.unl.sigett.service.EvaluacionTribunalService")
    private EvaluacionTribunalService evaluacionTribunalService;
    @EJB(lookup = "java:global/SigettService/MiembroTribunalServiceImplement!edu.unl.sigett.service.MiembroTribunalService")
    private MiembroTribunalService miembroTribunalService;
    @EJB(lookup = "java:global/ComunService/ItemServiceImplement!com.jlmallas.comun.service.ItemService")
    private ItemService itemService;

    //</editor-fold>
    public EvaluacionTribunalController() {
    }

    public void preRenderView() {
        buscar();
    }
//<editor-fold defaultstate="collapsed" desc="CRUD">

    private void buscar() {
        sessionEvaluacionTribunal.getEvaluacionesTribunal().clear();
        MiembroTribunal miembroTribunalBuscar = new MiembroTribunal();
        miembroTribunalBuscar.setDocenteId(docenteUsuarioDM.getDocenteUsuarioDTO().getDocente().getId());
        miembroTribunalBuscar.setEsActivo(Boolean.TRUE);
        List<MiembroTribunal> miembrosTribunal = miembroTribunalService.buscar(miembroTribunalBuscar);
        for (MiembroTribunal miembroTribunal : miembrosTribunal) {
            for (EvaluacionTribunal evaluacionTribunal : miembroTribunal.getTribunalId().getEvaluacionTribunalList()) {
                evaluacionTribunal.setCatalogoEvaluacion(itemService.buscarPorId(evaluacionTribunal.getCatalogoEvaluacionId()).getNombre());
                sessionEvaluacionTribunal.getEvaluacionesTribunal().add(evaluacionTribunal);
            }
        }
    }

    public String editar(EvaluacionTribunal evaluacionTribunal) {
        sessionEvaluacionTribunal.setEvaluacionTribunal(evaluacionTribunal);
        return "pretty:editarEvaluacionTribunal";
    }
    //</editor-fold>
}
