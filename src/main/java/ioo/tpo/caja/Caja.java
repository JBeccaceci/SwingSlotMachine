package ioo.tpo.caja;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Caja {
    private float total;
    private List<Recibo> reciboList;

    public Caja() {
        this.reciboList = new ArrayList<>();
    }

    public Recibo generarCredito(float monto) {
        Recibo recibo = new Recibo(monto, TipoRecibo.INGRESO);
        reciboList.add(recibo);
        return recibo;
    }

    public float cobrar(int id) {
        Optional<Recibo> recibo = reciboList.stream().filter(r -> r.obtenerId() == id).findFirst();
        if (recibo.isPresent()) {
            Recibo r = recibo.get();
            if (r.esReciboValido()) {
                r.marcarComoUsado();
                return r.obtenerMonto();
            }
        }
        return 0.0f;
    }

    public Recibo obtenerReciboCredito(int id) {
        Optional<Recibo> recibo = reciboList.stream().filter(r -> r.obtenerId() == id).findFirst();
        if (recibo.isPresent()) {
            Recibo r = recibo.get();
            if (r.obtenerTipo() == TipoRecibo.INGRESO) {
                return r;
            }
        }
        return null;
    }

    public void agregarRecibo(Recibo recibo) {
        this.reciboList.add(recibo);
    }
}