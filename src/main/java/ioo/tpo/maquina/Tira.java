package ioo.tpo.maquina;

import ioo.tpo.Controlador;

import java.util.*;

public class Tira {
    private List<Fruta> frutas;
    private Fruta resultado;
    private int posY;
    private int direccion;
    private int cambiaDireccion;
    private int maxCambioDireccion;
    private boolean termino;

    public Tira() {
        this.frutas = new ArrayList<>();
        this.resultado = Fruta.getRandom();
        this.posY = -200;
        this.direccion = -1;
        this.cambiaDireccion = 0;
        this.maxCambioDireccion = new Random().nextInt(5);
        this.termino = false;
        System.out.println("Resultado generado " + resultado);
    }

    private void agregarFruta(Fruta fruta) {
        this.frutas.add(fruta);
    }

    public boolean calcularResultado() {
        int indice = Math.abs((Controlador.LINEA_DE_PAGO - this.posY) / Controlador.SLOT_PIXELES_Y);
        if (indice <= this.frutas.size()) {
            Fruta f = this.frutas.get(indice);
            if (f.getId() == resultado.getId()) {
                int posicionFruta = Math.abs((indice * Controlador.SLOT_PIXELES_Y) - Math.abs(this.posY));
                if (Controlador.POSICION_EXACTA) {
                    if (posicionFruta > ((Controlador.LINEA_DE_PAGO - 1) - 100) && posicionFruta < ((Controlador.LINEA_DE_PAGO + 1) - 100)) {
                        System.out.println("[POSICION_EXACTA] Offset encontrado " + this.posY + " Indice " + indice + " Resultado " + resultado + " PosFruta " + posicionFruta);
                        return true;
                    }
                } else {
                    if (posicionFruta > ((Controlador.LINEA_DE_PAGO - 5) - 100) && posicionFruta < ((Controlador.LINEA_DE_PAGO + 5) - 100)) {
                        System.out.println("[POSICION_NORMAL] Offset encontrado " + this.posY + " Indice " + indice + " Resultado " + resultado + " PosFruta " + posicionFruta);
                        return true;
                    }
                }

            }
        }
        return false;
    }

    private boolean enLineaDePago() {
        int posicionFruta = Math.abs((5 * Controlador.SLOT_PIXELES_Y) - Math.abs(this.posY));
        if (Controlador.POSICION_EXACTA) {
            return posicionFruta > ((Controlador.LINEA_DE_PAGO - 1) - 100) && posicionFruta < ((Controlador.LINEA_DE_PAGO + 1) - 100);
        } else {
            return posicionFruta > ((Controlador.LINEA_DE_PAGO - 5) - 100) && posicionFruta < ((Controlador.LINEA_DE_PAGO + 5) - 100);
        }
    }

    public void generarNuevoResultado(Fruta resultado) {
        this.resultado = resultado;
        generarNuevoResultado();
    }

    public void generarNuevoResultado() {
        this.cambiaDireccion = 0;
        this.maxCambioDireccion = new Random().nextInt(5);
        this.termino = false;
    }

    public void generarFrutasEnTira(int longitud) {
        this.frutas.clear();
        for (int i = 0; i < longitud; i++) {
            this.frutas.add(Fruta.getRandom());
        }
    }

    public List<Fruta> obtenerFrutas() {
        return frutas;
    }

    public int longitudTira() {
        return this.frutas.size();
    }

    public boolean moverTiraEjeY() {
        if (termino) return false;

        //  Movemos la tira de arriba hacia abajo
        //  contando la cantidad de veces y validamos que
        //  no se pase de los bordes
        if (direccion == -1) {
            posY = posY + 1;
            if (posY > Controlador.INICIO_TIRAS_Y) {
                direccion = 1;
                cambiaDireccion++;
            }
        } else if (direccion == 1) {
            posY = posY - 1;
            if (posY < -((this.frutas.size() * Controlador.SLOT_PIXELES_Y) - Controlador.MAQUINA_TAMANO_Y)) { // TODO: 800 -> tamano de la ventana
                direccion = -1;
                cambiaDireccion++;
            }
        }

        //  Le damos una seria de movimientos iniciales para que
        //  no busque el resultado en la primera ejecucion
        if (puedeMover()) {
            if (calcularResultado()) {
                termino = true;
                return false;
            }
        }

        //  Soluciona el problema donde las tiras se quedan
        //  moviendo si no existe el resultado en la tira
        if (cambiaDireccion > maxCambioDireccion * 2 && this.enLineaDePago()) {
            System.out.println("Condicion de salida.");
            termino = true;
            return false;
        }
        return true;
    }

    public int obtenerPosY() {
        return posY;
    }

    public boolean puedeMover() {
        return (cambiaDireccion > maxCambioDireccion);
    }

    public Fruta obtenerResultado() {
        return this.resultado;
    }
}