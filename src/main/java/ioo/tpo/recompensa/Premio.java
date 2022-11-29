package ioo.tpo.recompensa;

import ioo.tpo.maquina.Fruta;

import java.util.*;

/**
 * Los premios representan una combinacion de frutas que se van a comprobar
 * a la hora de jugar
 */
public class Premio {

    private List<Fruta> combinacion;
    private float puntos;

    public Premio() {
    }

    public Premio(List<Fruta> combinacion, float puntos) {
        this.combinacion = combinacion;
        this.puntos = puntos;
    }

    public boolean esCombinacionValida(List<Fruta> _combinacion) {
        List<Fruta> tmpCombinacion = this.combinacion;

        for (Fruta fruta: this.combinacion) {
            int indexFruit = _combinacion.indexOf(fruta);
            if (indexFruit == -1) {
                return false;
            }
            _combinacion.remove(fruta);
            if (_combinacion.size() == 0) {
                return true;
            }
        }
        return true;
    }

    public float obtenerPuntos() {
        return puntos;
    }

    public List<Fruta> obtenerCombinacion() {
        return combinacion;
    }
}