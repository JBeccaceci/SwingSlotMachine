package ioo.tpo.recompensa;

import ioo.tpo.maquina.Fruta;

import java.util.*;

/**
 * La recompensa es el conjunto de los premios con sus respectivos puntos
 * que posee una maquina
 */
public class Recompensa {
    private List<Premio> premios;

    public Recompensa() {
        this.premios = new ArrayList<>();
    }

    public Recompensa(List<Premio> premios) {
        this.premios = premios;
    }

    public Premio validarPremios(List<Fruta> combinacion) {
        if (premios.size() == 0)
            return null;

        for (Premio premio: this.premios) {
            if (premio.esCombinacionValida(combinacion)) {
                return premio;
            }
        }
        return null;
    }

    public void registrarNuevoPremio(List<Fruta> combinacion, float puntos, int filas) throws Exception {
        if (filas != combinacion.size()) {
            throw new Exception("combinacion invalida");
        }
        this.premios.add(new Premio(combinacion, puntos));
    }

    public void agregarNuevoPremio(Premio premio) {
        this.premios.add(premio);
    }

    public void eliminarPremio(int index) {
        if (index < 0) {
            return;
        }
        if (index > premios.size()) {
            return;
        }
        this.premios.remove(index);
    }

    public List<Premio> obtenerPremios() {
        return premios;
    }
}