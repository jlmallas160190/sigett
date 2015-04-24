/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.managed.session;

import edu.jlmallas.academico.entity.Area;
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
public class SessionArea implements Serializable {

    private Area area;
    private List<Area> areasWS;

    public SessionArea() {
        this.areasWS= new ArrayList<>();
        this.area = new Area();
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public List<Area> getAreasWS() {
        return areasWS;
    }

    public void setAreasWS(List<Area> areasWS) {
        this.areasWS = areasWS;
    }

}
