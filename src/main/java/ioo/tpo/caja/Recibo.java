package ioo.tpo.caja;

import java.util.Random;

public class Recibo {
    private int id;
    private float monto;
    private boolean usado;
    private TipoRecibo tipo;

    public Recibo(float monto, TipoRecibo tipo) {
        this.id = new Random().nextInt(500);
        this.monto = monto;
        this.tipo = tipo;
        this.usado = false;
    }

    public void marcarComoUsado() {
        this.usado = true;
    }

    public boolean esReciboValido() {
        return !this.usado;
    }

    public float obtenerMonto() {
        return this.monto;
    }

    public int obtenerId() {
        return id;
    }

    public TipoRecibo obtenerTipo() {
        return tipo;
    }
}