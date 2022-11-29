package ioo.tpo.gui;

import ioo.tpo.Controlador;
import ioo.tpo.maquina.Fruta;
import ioo.tpo.maquina.Maquina;
import ioo.tpo.maquina.Tira;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TiraView {
    private List<Image> imageList;
    private final int maquinaId;

    public TiraView(int _id) {
        this.maquinaId = _id;
        this.cargarListaImagenes();
    }

    private void cargarListaImagenes() {
        this.imageList = new ArrayList<>();
        BufferedImage image;
        for (int i = 0; i < Controlador.NUMERO_DE_IMAGENES; i++) {
            try {
                image = ImageIO.read(Objects.requireNonNull(TiraView.class.getResource("/images/fruta" + i + ".png")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            imageList.add(image);
        }
    }

    private List<Icon> generarTiras() {
        Maquina maquina = Controlador.getInstancia().obtenerMaquinaPorId(this.maquinaId);
        if (maquina == null) {
            return new ArrayList<>();
        }
        List<Icon> resultado = new ArrayList<>();

        List<Tira> tiras = maquina.obtenerTiras();
        for (Tira t : tiras) {
            BufferedImage img = new BufferedImage(Controlador.SLOT_PIXELES_X, Controlador.SLOT_PIXELES_Y * t.longitudTira(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics();

            Color transparent = new Color(0, 0, 0, 0);
            g.setColor(transparent);
            g.setComposite(AlphaComposite.Src);

            g.fillRect(0, 0, Controlador.SLOT_PIXELES_X, Controlador.SLOT_PIXELES_Y * t.longitudTira());

            List<Fruta> frutas = t.obtenerFrutas();
            for (int i = 0; i < t.longitudTira(); i++) {
                g.drawImage(imageList.get(frutas.get(i).getId()), 0, i * Controlador.SLOT_PIXELES_Y, null);
            }
            g.dispose();
            resultado.add(new ImageIcon(img));
        }

        return resultado;
    }

    public List<JLabel> generarListaLabels() {
        List<JLabel> resultado = new ArrayList<>();

        //  Instanciamos las vistas
        List<Icon> tiraIcons = generarTiras();
        for (int i = 0; i < tiraIcons.size(); i++) {
            JLabel bImg = new JLabel(tiraIcons.get(i));
            bImg.setBounds(Controlador.INICIO_TIRA_X + ((i * Controlador.SLOT_PIXELES_X) + (Controlador.TIRA_PADDING_X * i)), Controlador.TIRA_PADDING_Y,
                    tiraIcons.get(i).getIconWidth(), tiraIcons.get(i).getIconHeight());
            resultado.add(bImg);
        }

        return resultado;
    }

    public boolean moverTiras(Maquina maquina, List<JLabel> lbls, List<Tira> tiras) {
        if (!maquina.moverTiras())
            return false;

        for (int i = 0; i < lbls.size(); i++) {
            lbls.get(i).setBounds(lbls.get(i).getX(), tiras.get(i).obtenerPosY(), lbls.get(i).getWidth(), lbls.get(i).getHeight());
        }
        return true;
    }

    public TiraView toView(int _id) {
        return new TiraView(_id);
    }
}
