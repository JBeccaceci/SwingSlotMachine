package ioo.tpo.gui;

import ioo.tpo.Controlador;
import ioo.tpo.maquina.Maquina;
import ioo.tpo.maquina.Tira;
import ioo.tpo.recompensa.Premio;
import ioo.tpo.recompensa.Recompensa;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MaquinaView extends JFrame {

    private JFrame parent;

    private JLabel lblMonto;
    private JLabel lblPremio;
    private JLabel lblCredito;

    private JButton agregarRecompensa;
    private JButton eliminarRecompensa;

    private List<Image> imageList;
    private int maquinaId;

    private TiraView tiraView;
    private Timer timer;

    private List<JLabel> lblTiras;

    private JLabel lblLineaDePago = new JLabel();

    private Maquina maquinaActual;

    public MaquinaView(int _id, int filas, int col, float monto, float montoMin, Recompensa recompensa) {
        this.maquinaId = _id;
        this.parent = this;

        //  Configuramos la maquina
        this.configurar(_id, filas, col, monto, montoMin, recompensa);

        Container c = this.getContentPane();
        c.setLayout(null);
        c.setBackground(new Color(77, 111, 120));

        // Configuracion de la ventana
        this.setSize(Controlador.MAQUINA_TAMANO_X, Controlador.MAQUINA_TAMANO_Y);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setUndecorated(true);
        this.pack();
        this.setVisible(true);
    }

    private void configurar(int _id, int filas, int col, float monto, float montoMin, Recompensa recompensa) {
        System.out.printf("Maquina Iniciada %d, %d, %d \n", _id, filas, col);
        //  Iniciamos los labels
        this.lblTiras = new ArrayList<>();

        //  Creamos la maquina
        List<Recompensa> recompensaList = new ArrayList<>();
        recompensaList.add(recompensa);
        this.maquinaActual = Controlador.getInstancia().crearMaquina(_id, filas, col, monto, montoMin, recompensaList);

        //  Configuramos el background
        Image background;
        try {
            background = cargarImagen("main.png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.setContentPane(new JLabel(new ImageIcon(background)));

        //  Agregar recomensa
        agregarRecompensa = new JButton("Agregar Recompensa");
        agregarRecompensa.setBounds(861, 21, 150, 40);
        agregarRecompensa.addActionListener(e -> {
            String output = JOptionPane.showInputDialog(null, "Frutilla=0; Banana=1; Uva=3; Manzana=4; Sandia=5; Guinda=6", "1,6,3,0,5=100");
            List<Premio> premioList = Controlador.getInstancia().generarStringRecompensas(output);
            if (premioList != null) {
                premioList.forEach(t -> Controlador.getInstancia().agregarPremio(this.maquinaActual, t));
            }
        });
        this.add(agregarRecompensa);

        //  Eliminar recomensa
        eliminarRecompensa = new JButton("Eliminar Recompensa");
        eliminarRecompensa.setBounds(1050, 21, 150, 40);
        eliminarRecompensa.addActionListener(e -> {
            String output = JOptionPane.showInputDialog(null, "Subindice de la recompensa", "0");
            Controlador.getInstancia().eliminarPremio(this.maquinaActual, Integer.parseInt(output));
        });
        this.add(eliminarRecompensa);

        //  Labels
        lblMonto = new JLabel();
        lblMonto.setFont(new Font("Serif", Font.PLAIN, 20));
        lblMonto.setForeground(Color.WHITE);
        lblMonto.setText(String.valueOf(Controlador.getInstancia().obtenerMontoMaquina(this.maquinaActual)));
        lblMonto.setBounds(210, 685, 60, 40);
        this.add(lblMonto);

        lblPremio = new JLabel();
        lblPremio.setFont(new Font("Serif", Font.PLAIN, 20));
        lblPremio.setForeground(Color.WHITE);
        float montoPremioMaquina = Controlador.getInstancia().obtenerPremioMaquina(this.maquinaActual);
        if (montoPremioMaquina > 0.0f) {
            lblPremio.setText("Premio " + montoPremioMaquina);
        }
        lblPremio.setBounds(610, 680, 200, 40);
        this.add(lblPremio);

        lblCredito = new JLabel();
        lblCredito.setFont(new Font("Serif", Font.PLAIN, 20));
        lblCredito.setForeground(Color.WHITE);
        float montoCreditos = Controlador.getInstancia().obtenerCreditosMaquina(this.maquinaActual);
        lblCredito.setText("Credito " + montoCreditos);
        lblCredito.setBounds(610, 700, 200, 40);
        this.add(lblCredito);

        try {
            background = cargarImagen("marco.png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JLabel marco = new JLabel(new ImageIcon(background));
        marco.setBounds(0, 0, 1344, 756);
        this.add(marco);

        //  Linea de pago
        Image payline;
        try {
            payline = cargarImagen("linea_pago.png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.lblLineaDePago = new JLabel(new ImageIcon(payline));
        this.lblLineaDePago.setBounds(200, 340, 950, 15);
        this.lblLineaDePago.setVisible(false);
        this.add(lblLineaDePago);

        //  Instanciamos las vistas
        this.tiraView = Controlador.getInstancia().getTiraView(_id);
        List<JLabel> labels = this.tiraView.generarListaLabels();
        for (JLabel lbl : labels) {
            this.add(lbl);
        }
        List<Tira> tiras = Controlador.getInstancia().obtenerMaquinaTira(this.maquinaActual);
        timer = new Timer(1, e -> {
            if (!tiraView.moverTiras(this.maquinaActual, labels, tiras)) {
                stop();
            }
        });

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("X" + e.getX());
                System.out.println("Y" + e.getY());

                //  Boton jugar
                if (e.getX() > 1035 && e.getX() < 1321) {
                    if (e.getY() > 655 && e.getY() < 751) {
                        jugar();
                    }
                }

                //  Boton cerrar
                if (e.getX() > 37 && e.getX() < 80) {
                    if (e.getY() > 23 && e.getY() < 61) {
                        close();
                    }
                }

                //  Boton ingresar dinero
                if (e.getX() > 246 && e.getX() < 418) {
                    if (e.getY() > 20 && e.getY() < 54) {
                        String nroComprobante = JOptionPane.showInputDialog(parent, "Ingresa el numero de comprobante", null);
                        if (!Objects.equals(nroComprobante, "")) {
                            float monto = Controlador.getInstancia().acreditarRecibo(maquinaActual, Integer.parseInt(nroComprobante));
                            lblCredito.setText("Credito " + monto);
                        }
                    }
                }

                // Boton retirar
                if (e.getX() > 480 && e.getX() < 814) {
                    if (e.getY() > 15 && e.getY() < 65) {
                        if (Controlador.getInstancia().obtenerCreditosMaquina(maquinaActual) > 0) {
                            int id = Controlador.getInstancia().crearRecibo(maquinaActual);
                            JOptionPane.showMessageDialog(null, "Numero de recibo " + id, "Alerta", JOptionPane.INFORMATION_MESSAGE);
                            lblCredito.setText("Credito 0");
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    private void close() {
        System.out.printf("Maquina Eliminada %d\n", this.maquinaId);
        Controlador.getInstancia().eliminarMaquina(this.maquinaId);
        this.dispose();
    }

    private void stop() {
        timer.stop();
        this.lblLineaDePago.setVisible(true);
        lblMonto.setText(String.valueOf(Controlador.getInstancia().obtenerMontoMaquina(this.maquinaActual)));

        float montoPremioMaquina = Controlador.getInstancia().obtenerPremioMaquina(this.maquinaActual);
        if (montoPremioMaquina > 0.0f) {
            lblPremio.setText("Premio " + montoPremioMaquina);
        }

        float montoCredito = Controlador.getInstancia().obtenerCreditosMaquina(this.maquinaActual);
        lblCredito.setText("Credito " + montoCredito);
    }

    public void start() {
        Controlador.getInstancia().Jugar(this.maquinaActual);

        this.lblMonto.setText(String.valueOf(Controlador.getInstancia().obtenerMontoMaquina(this.maquinaActual)));
        this.lblLineaDePago.setVisible(false);
        this.lblPremio.setText("");
        timer.start();
    }

    private void jugar() {
        if (Controlador.getInstancia().PuedeJugar(this.maquinaActual)) {
            if (Controlador.getInstancia().superaMontoMinimo(this.maquinaActual)) {
                if (JOptionPane.showConfirmDialog(null, "La maquina puede no pagar el premio, desea continuar?", "WARNING", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    this.start();
                }
            } else {
                this.start();
            }
        }
    }

    public Image cargarImagen(String image) throws IOException {
        return ImageIO.read(Objects.requireNonNull(TiraView.class.getResource("/images/" + image)));
    }
}
