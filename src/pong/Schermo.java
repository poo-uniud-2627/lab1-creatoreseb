// Pong — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
package pong;

import geometria.Posizione;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Mostra la partita: sessanta volte al secondo la fa avanzare di un passo e la ridisegna; la fa
 * partire con SPAZIO. Non prende nessuna decisione di gioco (quelle sono della Partita).
 */
class Schermo extends JPanel {
    private final Partita partita;
    private final Tastiera tastiera;

    private Schermo(Partita partita, Tastiera tastiera) {
        this.partita = partita;
        this.tastiera = tastiera;
        Campo campo = partita.getCampo();
        setPreferredSize(new Dimension(campo.getLarghezza(), campo.getAltezza()));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(tastiera);
        new Timer(16, e -> passo()).start();
    }

    static void mostra(Partita partita, Tastiera tastiera) {
        JFrame finestra = new JFrame("Pong - POO 2026-27");
        finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Schermo schermo = new Schermo(partita, tastiera);
        finestra.add(schermo);
        finestra.pack();
        finestra.setResizable(false);
        finestra.setLocationRelativeTo(null);
        finestra.setVisible(true);
        schermo.requestFocusInWindow();
    }

    private void passo() {
        if (!partita.isInCorso() && tastiera.premuto(KeyEvent.VK_SPACE)) partita.avvia();
        partita.aggiorna();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Campo campo = partita.getCampo();
        int larghezza = campo.getLarghezza();
        int altezza = campo.getAltezza();

        g2.setColor(Color.DARK_GRAY);
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float[]{10, 10}, 0));
        g2.drawLine(larghezza / 2, 0, larghezza / 2, altezza);

        g2.setColor(Color.WHITE);
        disegna(g2, partita.getRacchettaSinistra());
        disegna(g2, partita.getRacchettaDestra());
        Palla palla = partita.getPalla();
        int r = palla.getRaggio();
        Posizione p = palla.getPosizione();
        g2.fillOval((int) Math.round(p.x() - r), (int) Math.round(p.y() - r), 2 * r, 2 * r);

        g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 48));
        g2.drawString(String.valueOf(partita.getPunti(Lato.SINISTRA)), larghezza / 2 - 80, 60);
        g2.drawString(String.valueOf(partita.getPunti(Lato.DESTRA)), larghezza / 2 + 50, 60);

        g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        String livello = "Livello " + partita.getLivello() + " di " + partita.getNumeroLivelli()
                + "   ·   vite: " + partita.getVite();
        g2.drawString(livello, (larghezza - g2.getFontMetrics().stringWidth(livello)) / 2, altezza - 20);

        if (!partita.isInCorso()) {
            g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 22));
            String messaggio;
            if (!partita.isIniziata()) messaggio = "Premi SPAZIO per cominciare";
            else if (partita.isCompletato()) messaggio = "Hai vinto tutti i livelli! SPAZIO per ricominciare";
            else if (partita.isPerso()) messaggio = "Vite finite. SPAZIO per ricominciare dal livello 1";
            else if (partita.getVincitore() == partita.getUmano()) messaggio = "Livello superato! SPAZIO per il livello " + partita.getLivello();
            else if (partita.getVincitore() != null) messaggio = "Hai perso una vita. SPAZIO per rigiocare il livello";
            else messaggio = "Partita finita senza vincitore?! SPAZIO per ricominciare";
            int lunghezza = g2.getFontMetrics().stringWidth(messaggio);
            g2.drawString(messaggio, (larghezza - lunghezza) / 2, altezza / 2 + 60);
        }
    }

    private void disegna(Graphics2D g2, Racchetta racchetta) {
        g2.fillRect(racchetta.getX(), (int) Math.round(racchetta.getY()),
                racchetta.getLarghezza(), racchetta.getAltezza());
    }
}
