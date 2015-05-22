/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.managed.session;

import com.jlmallas.comun.entity.Item;
import edu.jlmallas.academico.entity.ReporteMatricula;
import edu.unl.sigett.academico.dto.EstudianteCarreraDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionEstudianteCarrera implements Serializable {

    private EstudianteCarreraDTO estudianteCarreraDTO;
    private List<ReporteMatricula> reporteMatriculasWS;
    private ReporteMatricula reporteMatricula;
    private List<ReporteMatricula> reporteMatriculas;
    private List<EstudianteCarreraDTO> estudiantesCarreraDTO;
    private List<EstudianteCarreraDTO> filterEstudiantesCarreraDTO;
    private EstudianteCarreraDTO estudianteCarreraDTOWS;
    private ReporteMatricula reporteMatriculaUltimo;
    private ReporteMatricula reporteMatriculaPrimer;
    private String tipoDocumento;
    private String genero;
    private List<Item> tiposDocumento;
    private List<Item> generos;
    private String key;
    private int keyEntero;
    private int keyEnteroMatricula;
    private boolean renderedEditar;
    private boolean renderedCrear;
    private Boolean renderedInformacionEstudio;
    private Boolean renderedEsAptoAspirante;
    private int keyEnteroRm;
    private int keyEnteroEstadoEstudiantesParelelo;
    private int contadorEstadoEP;
    private int keyWsParalelosCarreraEntero;

    public SessionEstudianteCarrera() {
        this.reporteMatriculas=new ArrayList<>();
        this.reporteMatriculaPrimer=new ReporteMatricula();
        this.reporteMatriculaUltimo = new ReporteMatricula();
        this.filterEstudiantesCarreraDTO = new ArrayList<>();
        this.reporteMatriculasWS = new ArrayList<>();
        this.reporteMatricula = new ReporteMatricula();
        this.estudianteCarreraDTOWS = new EstudianteCarreraDTO();
        this.estudiantesCarreraDTO = new ArrayList<>();
        this.estudianteCarreraDTO = new EstudianteCarreraDTO();
    }

    public EstudianteCarreraDTO getEstudianteCarreraDTO() {
        return estudianteCarreraDTO;
    }

    public void setEstudianteCarreraDTO(EstudianteCarreraDTO estudianteCarreraDTO) {
        this.estudianteCarreraDTO = estudianteCarreraDTO;
    }

    public List<EstudianteCarreraDTO> getEstudiantesCarreraDTO() {
        return estudiantesCarreraDTO;
    }

    public void setEstudiantesCarreraDTO(List<EstudianteCarreraDTO> estudiantesCarreraDTO) {
        this.estudiantesCarreraDTO = estudiantesCarreraDTO;
    }

    public EstudianteCarreraDTO getEstudianteCarreraDTOWS() {
        return estudianteCarreraDTOWS;
    }

    public void setEstudianteCarreraDTOWS(EstudianteCarreraDTO estudianteCarreraDTOWS) {
        this.estudianteCarreraDTOWS = estudianteCarreraDTOWS;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public List<Item> getTiposDocumento() {
        return tiposDocumento;
    }

    public void setTiposDocumento(List<Item> tiposDocumento) {
        this.tiposDocumento = tiposDocumento;
    }

    public List<Item> getGeneros() {
        return generos;
    }

    public void setGeneros(List<Item> generos) {
        this.generos = generos;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getKeyEntero() {
        return keyEntero;
    }

    public void setKeyEntero(int keyEntero) {
        this.keyEntero = keyEntero;
    }

    public boolean isRenderedEditar() {
        return renderedEditar;
    }

    public void setRenderedEditar(boolean renderedEditar) {
        this.renderedEditar = renderedEditar;
    }

    public boolean isRenderedCrear() {
        return renderedCrear;
    }

    public void setRenderedCrear(boolean renderedCrear) {
        this.renderedCrear = renderedCrear;
    }

    public Boolean getRenderedInformacionEstudio() {
        return renderedInformacionEstudio;
    }

    public void setRenderedInformacionEstudio(Boolean renderedInformacionEstudio) {
        this.renderedInformacionEstudio = renderedInformacionEstudio;
    }


    public Boolean getRenderedEsAptoAspirante() {
        return renderedEsAptoAspirante;
    }

    public void setRenderedEsAptoAspirante(Boolean renderedEsAptoAspirante) {
        this.renderedEsAptoAspirante = renderedEsAptoAspirante;
    }

   

    public int getKeyEnteroRm() {
        return keyEnteroRm;
    }

    public void setKeyEnteroRm(int keyEnteroRm) {
        this.keyEnteroRm = keyEnteroRm;
    }

    public ReporteMatricula getReporteMatricula() {
        return reporteMatricula;
    }

    public void setReporteMatricula(ReporteMatricula reporteMatricula) {
        this.reporteMatricula = reporteMatricula;
    }

    public List<ReporteMatricula> getReporteMatriculasWS() {
        return reporteMatriculasWS;
    }

    public void setReporteMatriculasWS(List<ReporteMatricula> reporteMatriculasWS) {
        this.reporteMatriculasWS = reporteMatriculasWS;
    }

    public int getKeyEnteroMatricula() {
        return keyEnteroMatricula;
    }

    public void setKeyEnteroMatricula(int keyEnteroMatricula) {
        this.keyEnteroMatricula = keyEnteroMatricula;
    }

    public int getKeyEnteroEstadoEstudiantesParelelo() {
        return keyEnteroEstadoEstudiantesParelelo;
    }

    public void setKeyEnteroEstadoEstudiantesParelelo(int keyEnteroEstadoEstudiantesParelelo) {
        this.keyEnteroEstadoEstudiantesParelelo = keyEnteroEstadoEstudiantesParelelo;
    }

    public int getContadorEstadoEP() {
        return contadorEstadoEP;
    }

    public void setContadorEstadoEP(int contadorEstadoEP) {
        this.contadorEstadoEP = contadorEstadoEP;
    }

    public List<EstudianteCarreraDTO> getFilterEstudiantesCarreraDTO() {
        return filterEstudiantesCarreraDTO;
    }

    public void setFilterEstudiantesCarreraDTO(List<EstudianteCarreraDTO> filterEstudiantesCarreraDTO) {
        this.filterEstudiantesCarreraDTO = filterEstudiantesCarreraDTO;
    }

    public int getKeyWsParalelosCarreraEntero() {
        return keyWsParalelosCarreraEntero;
    }

    public void setKeyWsParalelosCarreraEntero(int keyWsParalelosCarreraEntero) {
        this.keyWsParalelosCarreraEntero = keyWsParalelosCarreraEntero;
    }

    public ReporteMatricula getReporteMatriculaUltimo() {
        return reporteMatriculaUltimo;
    }

    public void setReporteMatriculaUltimo(ReporteMatricula reporteMatriculaUltimo) {
        this.reporteMatriculaUltimo = reporteMatriculaUltimo;
    }

    public ReporteMatricula getReporteMatriculaPrimer() {
        return reporteMatriculaPrimer;
    }

    public void setReporteMatriculaPrimer(ReporteMatricula reporteMatriculaPrimer) {
        this.reporteMatriculaPrimer = reporteMatriculaPrimer;
    }

    public List<ReporteMatricula> getReporteMatriculas() {
        return reporteMatriculas;
    }

    public void setReporteMatriculas(List<ReporteMatricula> reporteMatriculas) {
        this.reporteMatriculas = reporteMatriculas;
    }

}
