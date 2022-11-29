package ioo.tpo;

import ioo.tpo.caja.Caja;
import ioo.tpo.caja.Recibo;
import ioo.tpo.gui.MaquinaView;
import ioo.tpo.gui.TiraView;
import ioo.tpo.maquina.Fruta;
import ioo.tpo.maquina.Maquina;
import ioo.tpo.maquina.Tira;
import ioo.tpo.recompensa.Premio;
import ioo.tpo.recompensa.Recompensa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Juan M Beccaceci
 */
public class Controlador {
    public static final boolean POSICION_EXACTA = true;

    public static final int MAQUINA_TAMANO_X = 1344;
    public static final int MAQUINA_TAMANO_Y = 756;

    public static final int SLOT_PIXELES_X = 150;
    public static final int SLOT_PIXELES_Y = 150;
    public static final int VALOR_MINIMO = 100;

    public static final int NUMERO_DE_IMAGENES = 7;

    public static final int TIRA_PADDING_Y = 100;
    public static final int INICIO_TIRAS_Y = 1;

    public static final int INICIO_TIRA_X = 220;
    public static final int TIRA_PADDING_X = 40;

    public static final int LINEA_DE_PAGO = MAQUINA_TAMANO_Y / 2;

    public static final int MUL_CANTIDAD_FRUTAS_TIRAS = 6;

    public static final int MAX_TIRA_POSICION_Y = 600; // TODO HARDCODED
    public static final int MAQUINA_COSTO_TIRO = 25;
    public static final int MAQUINA_COSTO_MINIMO = 5;

    private static Controlador instancia;
    private List<Maquina> maquinas;
    private Caja caja;

    public Controlador() {
        this.maquinas = new ArrayList<>();
        this.caja = new Caja();
    }

    public static Controlador getInstancia() {
        if (instancia == null)
            instancia = new Controlador();
        return instancia;
    }

    public Maquina crearMaquina(int id, int filas, int col) {
        Maquina maquina = new Maquina(id, filas, col, 0);
        this.maquinas.add(maquina);
        return maquina;
    }

    public Maquina crearMaquina(int id, int filas, int col, float monto, float montoMin, List<Recompensa> recompensas) {
        Maquina maquina = new Maquina(id, filas, col, monto, montoMin);
        this.maquinas.add(maquina);
        recompensas.forEach(maquina::agregarRecompensa);
        return maquina;
    }

    public void eliminarMaquina(int id) {
        this.maquinas.removeIf(m -> m.obtenerId() == id);
    }

    public Caja crearCaja() {
        Caja caja = new Caja();
        this.caja = caja;
        return caja;
    }

    public void Jugar(Maquina maquina) {
        maquina.jugar();
    }

    public boolean PuedeJugar(Maquina maquina) {
        return maquina.puedeJugar();
    }

    public void crearRecompensa(Maquina maquina, Recompensa recompensa) {
        maquina.agregarRecompensa(recompensa);
    }

    public void eliminarRecompensa(Maquina maquina, int index) {
        maquina.eliminarRecompensa(index);
    }

    public void agregarPremio(Maquina maquina, Premio premio) {
        maquina.agregarPremio(premio);
    }

    public void eliminarPremio(Maquina maquina, int index) {
        maquina.eliminarPremio(index);
    }

    public Maquina obtenerMaquinaPorId(int id) {
        Optional<Maquina> maq = this.maquinas.stream().filter(maquina -> maquina.obtenerId() == id).findFirst();
        return maq.orElse(null);
    }

    public float obtenerMontoMaquina(Maquina maquina) {
        return maquina.obtenerMonto();
    }

    public float obtenerCreditosMaquina(Maquina maquina) {
        return maquina.obtenerCreditos();
    }

    public float obtenerPremioMaquina(Maquina maquina) {
        return maquina.obtenerPremioMonto();
    }

    private Premio crearPremio(List<Fruta> combinacion, float monto) {
        return new Premio(combinacion, monto);
    }

    public int crearRecibo(Maquina maquina) {
        Recibo rec = maquina.generarRecibo();
        caja.agregarRecibo(rec);
        return rec.obtenerId();
    }

    public int generarReciboCredito(float monto) {
        Recibo recibo = caja.generarCredito(monto);
        return recibo.obtenerId();
    }

    public float acreditarRecibo(Maquina maquina, int id) {
        Recibo recibo = caja.obtenerReciboCredito(id);
        if (recibo != null) {
            maquina.acreditarRecibo(recibo);
            return recibo.obtenerMonto();
        }
        return 0.0f;
    }

    public List<Tira> obtenerMaquinaTira(Maquina maquina) {
        return maquina.obtenerTiras();
    }

    public boolean moverTiras(Maquina maquina) {
        return maquina.moverTiras();
    }

    public boolean superaMontoMinimo(Maquina maquina) {
        return maquina.superaMontoMinimo();
    }

    public List<Premio> generarStringRecompensas(String recompensas) {
        List<Premio> premioList = new ArrayList<>();
        try {
            String[] premios = recompensas.split("\n");

            Recompensa recompensa = new Recompensa();
            List<Fruta> combinacionList;
            for (String p : premios) {
                combinacionList = new ArrayList<>();

                String[] resultado = p.split("=");

                String[] frutas = resultado[0].split(",");
                for (String fruta : frutas) {
                    Optional<Fruta> fr = Fruta.getById(Integer.parseInt(fruta));
                    if (fr.isPresent()) {
                        combinacionList.add(fr.get());
                    }
                }
                premioList.add(new Premio(combinacionList, Float.parseFloat(resultado[1])));
            }
            return premioList;
        } catch (Exception e) {
            return null;
        }

    }

    public TiraView getTiraView(int _id) {
        return new TiraView(_id);
    }

    public MaquinaView getMaquinaView(int _id, int filas, int col, float monto, float montoMin, Recompensa recompensa) {
        return new MaquinaView(_id, filas, col, monto, montoMin, recompensa);
    }
}