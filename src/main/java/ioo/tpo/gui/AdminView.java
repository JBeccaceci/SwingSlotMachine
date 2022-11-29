package ioo.tpo.gui;

import ioo.tpo.Controlador;
import ioo.tpo.maquina.Fruta;
import ioo.tpo.recompensa.Premio;
import ioo.tpo.recompensa.Recompensa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class AdminView extends JFrame {

    JTextField txtFilas;
    JTextField txtColumnas;
    JTextField txtMontoMinimo;
    JTextField txtMontoMaquina;

    public AdminView() {
        Container c = this.getContentPane();
        c.setLayout(null);
        c.setBackground(Color.GRAY);

        //  Recompensas
        JLabel lblInfo3 = new JLabel("Frutilla=0; Banana=1; Uva=3; Manzana=4; Sandia=5; Guinda=6");
        lblInfo3.setBounds(300, 10, 500, 50);
        this.add(lblInfo3);

        JTextArea txtArea = new JTextArea(10, 10);
        txtArea.setBounds(300, 45, 400, 65);
        txtArea.setLineWrap(true);
        txtArea.setText("1,6,3,0,5=100\n6,3,3,1,5=100");
        this.add(txtArea);

        //  Boton iniciar
        JButton button = new JButton();
        button.setText("Iniciar");
        button.setBounds(20, 25, 100, 180);
        button.addActionListener(e -> {
            iniciarMaquina(txtArea.getText());
        });
        this.add(button);

        //  Boton generar credito
        JButton agregarCredito = new JButton();
        agregarCredito.setText("Generar Credito");
        agregarCredito.setBounds(300, 120, 140, 80);
        agregarCredito.addActionListener(e -> {
            String output = JOptionPane.showInputDialog(null,
                    "Ingrese monto",
                    "100");
            if (output.length() > 0) {
                int monto = Integer.parseInt(output);
                int id = Controlador.getInstancia().generarReciboCredito(monto);
                if (monto > 0) {
                    JOptionPane.showMessageDialog(null, "Numero de recibo generado " + id, "Alerta", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        this.add(agregarCredito);

        //  Monto inicial
        JLabel lblInfo = new JLabel("Monto inicial");
        lblInfo.setBounds(140, 10, 100, 50);
        txtMontoMaquina = new JTextField();
        txtMontoMaquina.setBounds(135, 45, 150, 20);
        txtMontoMaquina.setText("400");
        this.add(txtMontoMaquina);
        this.add(lblInfo);

        //  Monto minimo
        JLabel lblInfo2 = new JLabel("Monto Minimo");
        lblInfo2.setBounds(140, 30, 100, 100);
        txtMontoMinimo = new JTextField();
        txtMontoMinimo.setBounds(135, 90, 150, 20);
        txtMontoMinimo.setText("300");
        this.add(lblInfo2);
        this.add(txtMontoMinimo);

        //  Filas
        JLabel lblFilas = new JLabel("Filas");
        lblFilas.setBounds(140, 100, 100, 50);
        txtFilas = new JTextField();
        txtFilas.setBounds(135, 133, 150, 20);
        txtFilas.setText("3");
        this.add(lblFilas);
        this.add(txtFilas);

        //  Columnas
        JLabel lblColumnas = new JLabel("Columnas");
        lblColumnas.setBounds(140, 120, 100, 100);
        txtColumnas = new JTextField();
        txtColumnas.setBounds(135, 180, 150, 20);
        txtColumnas.setText("5");
        this.add(lblColumnas);
        this.add(txtColumnas);

        // Configuracion de la ventana
        this.setSize(800, 300);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void iniciarMaquina(String recompensas) {
        List<Premio> r1 = Controlador.getInstancia().generarStringRecompensas(recompensas);
        if (r1 == null) {
            return;
        }

        Controlador.getInstancia().getMaquinaView(new Random().nextInt(10),
                Integer.parseInt(txtFilas.getText()),
                Integer.parseInt(txtColumnas.getText()),
                Integer.parseInt(txtMontoMaquina.getText()),
                Integer.parseInt(txtMontoMinimo.getText()),
                new Recompensa(r1));
    }
}
