/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.miembroTribunal;

import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.service.ItemService;
import com.jlmallas.comun.service.PersonaService;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.jlmallas.academico.entity.Docente;
import edu.jlmallas.academico.service.DocenteService;
import edu.unl.sigett.docenteUsuario.DocenteUsuarioDM;
import edu.unl.sigett.entity.EvaluacionTribunal;
import edu.unl.sigett.entity.MiembroTribunal;
import edu.unl.sigett.service.MiembroTribunalService;
import edu.unl.sigett.util.CabeceraController;
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
@Named(value = "miembroTribunalController")
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "editarMiembroTribunal",
            pattern = "/editarTribunal/",
            viewId = "/faces/pages/sigett/miembroTribunal/editarMiembroTribunal.xhtml"
    ),
    @URLMapping(
            id = "miembroTribunales",
            pattern = "/tribunales/",
            viewId = "/faces/pages/sigett/miembroTribunal/index.xhtml"
    )
})
public class MiembroTribunalController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private DocenteUsuarioDM docenteUsuarioDM;
    @Inject
    private SessionMiembroTribunal sessionMiembroTribunal;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB(lookup = "java:global/SigettService/MiembroTribunalServiceImplement!edu.unl.sigett.service.MiembroTribunalService")
    private MiembroTribunalService miembroTribunalService;
    @EJB(lookup = "java:global/ComunService/PersonaServiceImplement!com.jlmallas.comun.service.PersonaService")
    private PersonaService personaService;
    @EJB(lookup = "java:global/ComunService/ItemServiceImplement!com.jlmallas.comun.service.ItemService")
    private ItemService itemService;
    @EJB(lookup = "java:global/AcademicoService/DocenteServiceImplement!edu.jlmallas.academico.service.DocenteService")
    private DocenteService docenteService;

    //</editor-fold>
    public MiembroTribunalController() {
    }
    
    public void preRenderView() {
        buscar();
    }
    
    private void buscar() {
        this.sessionMiembroTribunal.getMiembrosTribunalDTO().clear();
        this.sessionMiembroTribunal.getFilterMiembrosTribunalDTO().clear();
        MiembroTribunal miembroTribunalBuscar = new MiembroTribunal();
        miembroTribunalBuscar.setDocenteId(docenteUsuarioDM.getDocenteUsuarioDTO().getDocente().getId());
        miembroTribunalBuscar.setEsActivo(Boolean.TRUE);
        List<MiembroTribunal> miembrosTribunal = miembroTribunalService.buscar(miembroTribunalBuscar);
        for (MiembroTribunal miembroTribunal : miembrosTribunal) {
            MiembroTribunalDTO miembroTribunalDTO = new MiembroTribunalDTO(miembroTribunal,
                    docenteService.buscarPorId(new Docente(miembroTribunal.getDocenteId())), null);
            miembroTribunalDTO.setPersona(personaService.buscarPorId(new Persona(miembroTribunalDTO.getDocente().getId())));
            miembroTribunal.setCargo(itemService.buscarPorId(miembroTribunal.getCargoId()).getNombre());
            sessionMiembroTribunal.getMiembrosTribunalDTO().add(miembroTribunalDTO);
        }
        this.sessionMiembroTribunal.setFilterMiembrosTribunalDTO(sessionMiembroTribunal.getMiembrosTribunalDTO());
    }
    

}
