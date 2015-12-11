/**
 * Created by pphelipo on 23/11/15.
 * Github : https://github.com/pasterp/Jeu2048
 * License : http://www.apache.org/licenses/LICENSE-2.0
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Interface extends JFrame{
    Modele modele;
    JLabel[][] grid;
    JLabel score;
    JMenuBar mmenubar;
    JMenu mmenu, msubmenu, msubmenubase;
    JMenuItem mi_new, mi_scores, mi_help, miGiveUp;
    JRadioButtonMenuItem rbmi4, rbmi5, rbmi6, rbmiB2, rbmiB3;

    public Interface(Modele mod){
        modele = mod;
        creerMenu();
        creerWidgets();
        addWidget();
        setSize(500, 500);
        setTitle("2048 v1.5");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void creerMenu(){
        mmenubar = new JMenuBar();
        mmenu = new JMenu("Options");

        mi_new = new JMenuItem("Nouvelle Partie");

        mmenu.add(mi_new);
        mi_scores = new JMenuItem("Scores");
        mmenu.add(mi_scores);

        msubmenu = new JMenu("Taille");
        ButtonGroup group = new ButtonGroup();
        rbmi4 = new JRadioButtonMenuItem("4x4");
        if(modele.getTaille()==4)
            rbmi4.setSelected(true);
        group.add(rbmi4);
        msubmenu.add(rbmi4);

        rbmi5 = new JRadioButtonMenuItem("5x5");
        if(modele.getTaille()==5)
            rbmi5.setSelected(true);
        group.add(rbmi5);
        msubmenu.add(rbmi5);

        rbmi6 = new JRadioButtonMenuItem("6x6");
        if(modele.getTaille()==6)
            rbmi6.setSelected(true);
        group.add(rbmi6);
        msubmenu.add(rbmi6);

        mmenu.add(msubmenu);

        msubmenubase = new JMenu("Chiffre de base");
        ButtonGroup base = new ButtonGroup();
        rbmiB2 = new JRadioButtonMenuItem("Base 2");
        rbmiB2.setSelected(true);
        base.add(rbmiB2);
        msubmenubase.add(rbmiB2);
        rbmiB3 = new JRadioButtonMenuItem("Base 3");
        base.add(rbmiB3);
        msubmenubase.add(rbmiB3);

        mmenu.add(msubmenubase);


        mi_help = new JMenuItem("Aide");
        mmenu.add(mi_help);

        mmenubar.add(mmenu);

        miGiveUp = new JMenuItem("Abandonner");
        mmenubar.add(miGiveUp);
    }

    public void creerWidgets(){
        grid = new JLabel[modele.getTaille()][modele.getTaille()];
        for (int i=0; i<modele.getTaille(); i++)
            for (int j=0; j<modele.getTaille(); j++)
                grid[i][j] = new JLabel("", SwingConstants.CENTER);
        score = new JLabel("Score : "+modele.scoreActuel(), SwingConstants.CENTER);
    }

    public void updaterGrille(){
        int[][] grille = modele.getGrille();
        for (int i=0; i<modele.getTaille(); i++) {
            for (int j=0; j<modele.getTaille(); j++) {
                grid[i][j].setText("" + grille[i][j]);
                grid[i][j].setFont(new Font("Serif", Font.BOLD, 20));
                grid[i][j].setForeground(new Color(250, 251, 243));
                grid[i][j].setOpaque(true);

                if (grille[i][j] == 0){
                    grid[i][j].setText("");
                    grid[i][j].setBackground(new Color(204, 192, 180));
                }else
                if (grille[i][j] == modele.getBaseNombre(1)){
                    grid[i][j].setBackground(new Color(238, 228, 218));
                    grid[i][j].setForeground(new Color(125, 118, 110));
                }else
                if (grille[i][j] == modele.getBaseNombre(2)){
                    grid[i][j].setBackground(new Color(238, 223, 202));
                    grid[i][j].setForeground(new Color(125, 118, 110));
                }else
                if (grille[i][j] == modele.getBaseNombre(3)){
                    grid[i][j].setBackground(new Color(242, 177, 121));
                }else
                if (grille[i][j] == modele.getBaseNombre(4)){
                    grid[i][j].setBackground(new Color(233, 140, 79));
                }else
                if (grille[i][j] == modele.getBaseNombre(5)){
                    grid[i][j].setBackground(new Color(242, 123, 93));
                }else
                if (grille[i][j] == modele.getBaseNombre(6)){
                    grid[i][j].setBackground(new Color(234, 89, 58));
                }else
                if (grille[i][j] == modele.getBaseNombre(7)){
                    grid[i][j].setBackground(new Color(238, 150, 60));
                }else
                if (grille[i][j] == modele.getBaseNombre(8)){
                    grid[i][j].setBackground(new Color(241, 208, 75));
                }else
                if (grille[i][j] == modele.getBaseNombre(9)){
                    grid[i][j].setBackground(new Color(234, 190, 30));
                }else
                if (grille[i][j] == modele.getBaseNombre(10)){
                    grid[i][j].setBackground(new Color(226, 185, 19));
                }else
                if (grille[i][j] == modele.getBaseNombre(11)){
                    grid[i][j].setBackground(new Color(236, 193, 0));
                }else
                if (grille[i][j] == modele.getBaseNombre(12)){
                    grid[i][j].setBackground(new Color(169, 0, 255));
                }
                else{
                    grid[i][j].setBackground(new Color(204, 192, 180));
                }
                grid[i][j].setBorder(BorderFactory.createLineBorder(new Color(185, 174, 154)));
            }
        }
        updaterScore();

        if (modele.estFinie()){
            JOptionPane d = new JOptionPane();
            if(modele.submitScore()){
                d.showMessageDialog( this, "Partie terminée ! Vous entrez dans le classement !", "Gameover",
                        JOptionPane.WARNING_MESSAGE );
            }else{
                d.showMessageDialog( this, "Partie terminée !", "Gameover",
                        JOptionPane.WARNING_MESSAGE );
            }
            d.createDialog(this, "Gameover");
            modele.changeGrille(modele.getTaille());
        }
    }

    public void updaterScore(){
        score.setText("Score : " + modele.scoreActuel());
    }

    public void addWidget(){
        getContentPane().removeAll();
        JPanel panel = new JPanel(new BorderLayout());
        JPanel pgrid = new JPanel(new GridLayout(modele.getTaille(),modele.getTaille()));
        for (int i=0; i<modele.getTaille(); i++)
            for(int j=0; j<modele.getTaille(); j++)
                pgrid.add(grid[i][j]);

        panel.add(score, BorderLayout.NORTH);
        panel.add(pgrid, BorderLayout.CENTER);

        setJMenuBar(mmenubar);
        setContentPane(panel);
        getContentPane().doLayout();
        update(getGraphics());
    }

    public void addListeners(ActionListener a){
        rbmi4.addActionListener(a);
        rbmi5.addActionListener(a);
        rbmi6.addActionListener(a);
        rbmiB2.addActionListener(a);
        rbmiB3.addActionListener(a);
        mi_new.addActionListener(a);
        mi_scores.addActionListener(a);
        mi_help.addActionListener(a);
        miGiveUp.addActionListener(a);
    }

    public void betterScores(){
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel[] contents = new JPanel[3];
        for (int i = 4; i <= 6; i++){
            contents[i-4] = new JPanel();
            contents[i-4] = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JPanel f = new JPanel();
            f.setLayout(new BoxLayout(f, BoxLayout.Y_AXIS));
            for (int s : modele.getScores(i)){
                f.add(new JLabel(""+s));
            }
            contents[i-4].add(f);
            tabbedPane.addTab(""+i+"x"+i, contents[i-4]);
        }

        JOptionPane.showOptionDialog(null,
                new Object[]{tabbedPane},
                "Scores",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE, null, null, null);
    }

    public void afficherAide(){
        JOptionPane d = new JOptionPane();
        d.showMessageDialog( this, "Le but du jeu est de faire un score le plus élevé possible.\nLe score est la somme des valeurs de la grille.", "Aide",
                JOptionPane.INFORMATION_MESSAGE );
        d.createDialog(this, "Aide");
    }
}