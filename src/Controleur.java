/**
 * Created by pphelipo on 23/11/15.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Controleur implements KeyListener, ActionListener {
    Interface inter;
    Modele	modele;

    public Controleur(Modele modele){
        this.modele = modele;
        inter = new Interface(this.modele);
        inter.updaterGrille();
        inter.addKeyListener(this);
        inter.addListeners(this);
        inter.setVisible(true);
    }

    public void nouvellePartie(int n){
        modele = new Modele(n);
        modele.insertNew();
        inter.setModele(modele);
        inter.creerWidgets();
        inter.addWidget();
        inter.updaterGrille();
    }


    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_UP:
                if(modele.moveUp()){
                    modele.insertNew();
                }
                break;
            case KeyEvent.VK_DOWN:
                if(modele.moveDown()){
                    modele.insertNew();
                }
                break;
            case KeyEvent.VK_LEFT:
                if(modele.moveLeft()){
                    modele.insertNew();
                }
                break;
            case KeyEvent.VK_RIGHT:
                if(modele.moveRight()){
                    modele.insertNew();
                }
                break;
        }
        inter.updaterGrille();
        inter.updaterScore();
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {

    }

    public void actionPerformed(ActionEvent e){
        if (e.getSource() == inter.rbmi4) {
            modele.changeGrille(4);
            inter.creerWidgets();
            inter.addWidget();
            inter.updaterGrille();
        }
        if (e.getSource() == inter.rbmi5){
            modele.changeGrille(5);
            inter.creerWidgets();
            inter.addWidget();
            inter.updaterGrille();
        }
        if (e.getSource() == inter.rbmi6){
            modele.changeGrille(6);
            inter.creerWidgets();
            inter.addWidget();
            inter.updaterGrille();
        }
        if (e.getSource() == inter.mi_new){
            modele.changeGrille(modele.getTaille());
            inter.creerWidgets();
            inter.addWidget();
            inter.updaterGrille();
        }
        if (e.getSource() == inter.mi_scores){
            inter.betterScores();
        }
        if(e.getSource() == inter.mi_help){
            inter.afficherAide();
        }

    }




}