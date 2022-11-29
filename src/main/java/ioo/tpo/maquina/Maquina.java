package ioo.tpo.maquina;

import ioo.tpo.Controlador;
import ioo.tpo.caja.Recibo;
import ioo.tpo.caja.TipoRecibo;
import ioo.tpo.recompensa.Premio;
import ioo.tpo.recompensa.Recompensa;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Maquina {
    private int id;
    private float monto;
    private float montoMinimo;
    private float creditos;
    private int filas;
    private int columnas;
    private float costo;
    private List<Integer> resultados;
    private Recompensa recompensa;
    private List<Tira> tiras;
    private boolean jugando;
    private int tirasEnMovimiento;
    private float montoUltimoPremio;

    public Maquina(int id, int filas, int columnas, float monto, float montoMinimo) {
        this(id, filas, columnas, monto);
        this.montoMinimo = montoMinimo;
    }

    public Maquina(int id, int filas, int columnas, float monto) {
        this.id = id;
        this.tiras = new ArrayList<>();
        this.filas = filas;
        this.columnas = columnas;
        this.monto = monto;
        this.creditos = 0;
        this.jugando = false;
        this.montoUltimoPremio = 0.0f;
        this.generarTiras();
    }

    public void generarTiras() {
        for (int i = 0; i < columnas; i++) {
            Tira nuevaTira = new Tira();
            nuevaTira.generarFrutasEnTira(filas * Controlador.MUL_CANTIDAD_FRUTAS_TIRAS);
            tiras.add(nuevaTira);
        }
    }

    public List<Tira> obtenerTiras() {
        return this.tiras;
    }

    public int obtenerId() {
        return this.id;
    }

    public boolean alcanzoValorMinimo() {
        return this.monto < Controlador.VALOR_MINIMO;
    }

    public void calcularRecompensas() {
        this.jugando = false;

        List<Fruta> resultados = new ArrayList<>();
        for (Tira t : tiras) {
            resultados.add(t.obtenerResultado());
        }

        Premio p = recompensa.validarPremios(resultados);
        if (p != null) {
            float puntos = p.obtenerPuntos();
            if (puntos <= this.monto) {
                System.out.printf("Premio encontrado %f puntos acreditados", p.obtenerPuntos());
                montoUltimoPremio = puntos;
                this.monto -= puntos;
                this.creditos += puntos + Controlador.MAQUINA_COSTO_TIRO;
                return;
            }
        }

        this.monto += Controlador.MAQUINA_COSTO_TIRO;
        System.out.println("Ninguna recompensa encontrada..");
    }

    public boolean tieneCreditoDisponible() {
        return this.monto > 0;
    }

    public boolean puedeJugar() {
        if (this.jugando) {
            return false;
        }

        if (this.monto <= 0) {
            System.out.println("Maquina sin saldo");
            return false;
        }

        if (creditos <= 0) {
            System.out.println("Usuario sin saldo");
            return false;
        }

        return true;
    }

    public void jugar() {
        int premio = new Random().nextInt(2);
        if (premio == 1 && recompensa.obtenerPremios().size() > 0) {
            this.generarPremioResultado();
        } else {
            this.generarNuevosResultados();
        }

        float nuevoCreditoMonto = creditos - Controlador.MAQUINA_COSTO_TIRO;
        if (nuevoCreditoMonto < 0) {
            nuevoCreditoMonto = 0;
        }
        creditos = nuevoCreditoMonto;

        this.jugando = true;
        this.montoUltimoPremio = 0.0f;
    }

    private void generarNuevosResultados() {
        System.out.println("Premio normal generado");
        for (Tira t : tiras) {
            t.generarNuevoResultado();
        }
    }

    private void generarPremioResultado() {
        System.out.println("Premio con resultado generado");
        Premio randomPremio = recompensa.obtenerPremios().get(new Random().nextInt(recompensa.obtenerPremios().size()));

        int subindex = 0;
        List<Fruta> frutas = randomPremio.obtenerCombinacion();
        for (Tira t : tiras) {
            t.generarNuevoResultado(frutas.get(subindex));
            subindex++;
        }
    }

    public Recibo generarRecibo() {
        Recibo r = new Recibo(this.creditos, TipoRecibo.COBRO);
        creditos = 0;
        return r;
    }

    public void acreditarRecibo(Recibo recibo) {
        if (recibo.esReciboValido()) {
            this.creditos += recibo.obtenerMonto();
            recibo.marcarComoUsado();
        }
    }

    public void agregarRecompensa(Recompensa recompensa) {
        this.recompensa = recompensa;
    }

    public void eliminarRecompensa(int index) {
        if (index < 0) {
            return;
        }
        if (index > recompensa.obtenerPremios().size()) {
            return;
        }
        try {
            recompensa.obtenerPremios().remove(index);
        } catch (Exception ignored) {
        }
    }

    public void agregarPremio(Premio premio) {
        this.recompensa.agregarNuevoPremio(premio);
    }

    public void eliminarPremio(int index) {
        this.recompensa.eliminarPremio(index);
    }

    public boolean moverTiras() {
        int finalizado = 0;
        for (Tira t : tiras) {
            if (!t.moverTiraEjeY()) {
                finalizado++;
            }
        }
        if (finalizado == tiras.size()) {
            System.out.println("Buscando resultados...");
            calcularRecompensas();
            return false;
        }
        return true;
    }

    public float obtenerMonto() {
        return this.monto;
    }

    public float obtenerCreditos() {
        return this.creditos;
    }

    public float obtenerPremioMonto() {
        return this.montoUltimoPremio;
    }

    public boolean superaMontoMinimo() {
        return this.monto <= montoMinimo;
    }
}