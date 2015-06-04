/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.servlet;

import edu.unl.sigett.docenteProyecto.SessionDocenteProyecto;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.unl.sigett.documentoProyecto.SessionDocumentoProyecto;
import edu.unl.sigett.usuarioCarrera.SessionUsuarioCarrera;

/**
 *
 * @author JorgeLuis
 */
@WebServlet(name = "AppServlet", urlPatterns = {"/AppServlet/*"})
public class AppServlet extends HttpServlet {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private SessionDocumentoProyecto sessionDocumentoProyecto;
    @Inject
    private SessionUsuarioCarrera sessionUsuarioCarrera;
    @Inject
    private SessionDocenteProyecto sessionDocenteProyecto;
    //</editor-fold>

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String entity = request.getParameter("entity");
            if (entity == null) {
                return;
            }
            switch (entity) {
                case "documentoProyecto":
                    if (sessionDocumentoProyecto.getDocumentoProyectoDTOSeleccionado() == null) {
                        return;
                    }
                    if (sessionDocumentoProyecto.getDocumentoProyectoDTOSeleccionado().getDocumento() == null) {
                        return;
                    }
                    if (sessionDocumentoProyecto.getDocumentoProyectoDTOSeleccionado().getDocumento().getContents() == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
                        return;
                    }
                    response.setCharacterEncoding("ISO-8859-1");
                    response.setContentType("application/pdf");
                    response.setContentLength(sessionDocumentoProyecto.getDocumentoProyectoDTOSeleccionado().getDocumento().getContents().length);
                    response.getOutputStream().write(sessionDocumentoProyecto.getDocumentoProyectoDTOSeleccionado().getDocumento().getContents());
                    response.getOutputStream().flush();
                    response.getOutputStream().close();
                    break;
                case "carrera":
                    if (sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera() == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                    if (sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera().getLogo() == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                    response.reset();
                    response.setContentType("/image/png");
                    response.getOutputStream().write(sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera().getLogo());
                    response.getOutputStream().close();
                    break;
                case "pertinencia":
                    if (sessionDocenteProyecto.getDocumentoCarreraDTO() == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                    if (sessionDocenteProyecto.getDocumentoCarreraDTO().getDocumento().getContents() == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                    response.setCharacterEncoding("ISO-8859-1");
                    response.setContentType("application/pdf");
                    response.setContentLength(sessionDocenteProyecto.getDocumentoCarreraDTO().getDocumento().getContents().length);
                    response.getOutputStream().write(sessionDocenteProyecto.getDocumentoCarreraDTO().getDocumento().getContents(), 0, sessionDocenteProyecto.getDocumentoCarreraDTO().getDocumento().getContents().length);
                    response.getOutputStream().flush();
                    response.getOutputStream().close();
                    break;
                case "fePertinencia":
                    if (sessionDocenteProyecto.getDocumentoCarreraDTO() == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                    if (sessionDocenteProyecto.getDocumentoCarreraDTO().getDocumento().getContents() == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }

                    response.setCharacterEncoding("ISO-8859-1");
                    response.setContentType("application/pdf");
                    response.setContentLength(sessionDocenteProyecto.getDocumentoCarreraDTO().getDocumento().getContents().length);
                    response.getOutputStream().write(sessionDocenteProyecto.getDocumentoCarreraDTO().getDocumento().getContents(), 0, sessionDocenteProyecto.getDocumentoCarreraDTO().getDocumento().getContents().length);
                    response.getOutputStream().flush();
                    response.getOutputStream().close();
                    break;
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
