/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.managed.session;

import com.jlmallas.comun.entity.Item;
import edu.jlmallas.academico.entity.EstadoLaboral;
import edu.jlmallas.academico.entity.TituloDocente;
import edu.unl.sigett.academico.dto.DocenteCarreraAux;
import edu.unl.sigett.entity.LineaInvestigacion;
import edu.unl.sigett.entity.LineaInvestigacionDocente;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.model.DualListModel;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionDocenteCarrera implements Serializable {

    private DocenteCarreraAux docenteCarreraAux;
    private DocenteCarreraAux docenteCarreraAuxWS;
    private List<DocenteCarreraAux> docenteCarreraAuxs;
    private String estadoLaboral;
    private String titulo;
    private String tipoDocumento;
    private String genero;
    private List<LineaInvestigacionDocente> lineaInvestigacionDocentesRemovidos;
    private DualListModel<LineaInvestigacion> lineasInvestigacionDualList;
    private List<Item> tiposDocumento;
    private List<Item> generos;
    private List<EstadoLaboral> estadoLaborales;
    private List<TituloDocente> titulos;
    private String key;
    private String keyWSUnidadesDocenteParalelo;
    private int keyEntero;
    private int keyEnteroWSUnidadesDocenteParalelo;
    private int i;
    private boolean renderedEditar;
    private boolean renderedNoEditar;
    private boolean renderedCrear;

    public SessionDocenteCarrera() {
        this.docenteCarreraAuxWS=new DocenteCarreraAux();
        this.titulos = new ArrayList<>();
        this.estadoLaborales = new ArrayList<>();
        this.tiposDocumento = new ArrayList<>();
        this.generos = new ArrayList<>();
        this.docenteCarreraAux = new DocenteCarreraAux();
        this.docenteCarreraAuxs = new ArrayList<>();
    }

    public DocenteCarreraAux getDocenteCarreraAux() {
        return docenteCarreraAux;
    }

    public void setDocenteCarreraAux(DocenteCarreraAux docenteCarreraAux) {
        this.docenteCarreraAux = docenteCarreraAux;
    }

    public List<DocenteCarreraAux> getDocenteCarreraAuxs() {
        return docenteCarreraAuxs;
    }

    public void setDocenteCarreraAuxs(List<DocenteCarreraAux> docenteCarreraAuxs) {
        this.docenteCarreraAuxs = docenteCarreraAuxs;
    }

    public String getEstadoLaboral() {
        return estadoLaboral;
    }

    public void setEstadoLaboral(String estadoLaboral) {
        this.estadoLaboral = estadoLaboral;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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

    public List<LineaInvestigacionDocente> getLineaInvestigacionDocentesRemovidos() {
        return lineaInvestigacionDocentesRemovidos;
    }

    public void setLineaInvestigacionDocentesRemovidos(List<LineaInvestigacionDocente> lineaInvestigacionDocentesRemovidos) {
        this.lineaInvestigacionDocentesRemovidos = lineaInvestigacionDocentesRemovidos;
    }

    public DualListModel<LineaInvestigacion> getLineasInvestigacionDualList() {
        return lineasInvestigacionDualList;
    }

    public void setLineasInvestigacionDualList(DualListModel<LineaInvestigacion> lineasInvestigacionDualList) {
        this.lineasInvestigacionDualList = lineasInvestigacionDualList;
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

    public boolean isRenderedNoEditar() {
        return renderedNoEditar;
    }

    public void setRenderedNoEditar(boolean renderedNoEditar) {
        this.renderedNoEditar = renderedNoEditar;
    }

    public boolean isRenderedCrear() {
        return renderedCrear;
    }

    public void setRenderedCrear(boolean renderedCrear) {
        this.renderedCrear = renderedCrear;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
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

    public List<EstadoLaboral> getEstadoLaborales() {
        return estadoLaborales;
    }

    public void setEstadoLaborales(List<EstadoLaboral> estadoLaborales) {
        this.estadoLaborales = estadoLaborales;
    }

    public List<TituloDocente> getTitulos() {
        return titulos;
    }

    public void setTitulos(List<TituloDocente> titulos) {
        this.titulos = titulos;
    }

    public DocenteCarreraAux getDocenteCarreraAuxWS() {
        return docenteCarreraAuxWS;
    }

    public void setDocenteCarreraAuxWS(DocenteCarreraAux docenteCarreraAuxWS) {
        this.docenteCarreraAuxWS = docenteCarreraAuxWS;
    }

    public int getKeyEnteroWSUnidadesDocenteParalelo() {
        return keyEnteroWSUnidadesDocenteParalelo;
    }

    public void setKeyEnteroWSUnidadesDocenteParalelo(int keyEnteroWSUnidadesDocenteParalelo) {
        this.keyEnteroWSUnidadesDocenteParalelo = keyEnteroWSUnidadesDocenteParalelo;
    }

    public String getKeyWSUnidadesDocenteParalelo() {
        return keyWSUnidadesDocenteParalelo;
    }

    public void setKeyWSUnidadesDocenteParalelo(String keyWSUnidadesDocenteParalelo) {
        this.keyWSUnidadesDocenteParalelo = keyWSUnidadesDocenteParalelo;
    }

}
