package uce.proyect.web.util;

import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import uce.proyect.domain.Usuario;

@Named
@ViewScoped
public class PlantillaController implements Serializable {

    private static final long serialVersionUID = 1L;

    public void verificarSesion() {

        try {

            Usuario usuario = (Usuario) FacesContext.getCurrentInstance()
                    .getExternalContext().getSessionMap().get("usuario");

            if (usuario == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("./../permisos.xhtml");
            }
        } catch (Exception e) {

        }

    }
    
    public void verificarSesion2() {

        try {

            Usuario usuario = (Usuario) FacesContext.getCurrentInstance()
                    .getExternalContext().getSessionMap().get("usuario");

            if (usuario == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("./../../permisos.xhtml");
            }
        } catch (Exception e) {

        }

    }

}
