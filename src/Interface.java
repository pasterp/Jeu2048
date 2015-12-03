/**
 * Created by pphelipo on 23/11/15.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Interface extends JFrame{
    Modele modele;
    JLabel[][] grid;
    JLabel score;
    JMenuBar mmenubar;
    JMenu mmenu, msubmenu;
    JMenuItem mi_new, mi_scores;
    JRadioButtonMenuItem rbmi4, rbmi5, rbmi6;

    public Interface(Modele mod){
        modele = mod;
        creerMenu();
        creerWidgets();
        addWidget();
        setSize(500, 500);
        setVisible(true);
        setTitle("2048 v0.9");
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

        mmenubar.add(mmenu);
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
                grid[i][j].setText(""+grille[i][j]);
                grid[i][j].setFont(new Font("Serif", Font.BOLD, 20));
                grid[i][j].setForeground(new Color(250, 251, 243));
                grid[i][j].setOpaque(true);
                switch(grille[i][j]){
                    case 0:
                        grid[i][j].setText("");
                        grid[i][j].setBackground(new Color(204, 192, 180));
                        break;

                    case 2:
                        grid[i][j].setBackground(new Color(238, 228, 218));
                        grid[i][j].setForeground(new Color(125, 118, 110));
                        break;
                    case 4:
                        grid[i][j].setBackground(new Color(238, 223, 202));
                        grid[i][j].setForeground(new Color(125, 118, 110));

                        break;
                    case 8:
                        grid[i][j].setBackground(new Color(242, 177, 121));
                        break;
                    case 16:
                        grid[i][j].setBackground(new Color(233, 140, 79));
                        break;
                    case 32:
                        grid[i][j].setBackground(new Color(242, 123, 93));
                        break;
                    case 64:
                        grid[i][j].setBackground(new Color(234, 89, 58));
                        break;
                    case 128:
                        grid[i][j].setBackground(new Color(238, 150, 60));
                        break;
                    case 256:
                        grid[i][j].setBackground(new Color(241, 208, 75));
                        break;
                    case 512:
                        grid[i][j].setBackground(new Color(234, 190, 30));
                        break;
                    case 1024:
                        grid[i][j].setBackground(new Color(226, 185, 19));
                        break;
                    case 2048:
                        grid[i][j].setBackground(new Color(236, 193, 0));
                        break;
                    case 4096:
                        grid[i][j].setBackground(new Color(169, 0, 255));
                        break;

                    default:
                        grid[i][j].setBackground(new Color(204, 192, 180));
                        break;
                }
                grid[i][j].setBorder(BorderFactory.createLineBorder(new Color(185, 174, 154)));
            }
        }

        if (modele.estFinie()){
            JOptionPane d = new JOptionPane();
            if(modele.submitScore()){
                d.showMessageDialog( this, "Partie terminée ! Vous entrez dans le classement !", "Gameover",
                        JOptionPane.WARNING_MESSAGE );
            }else{
                d.showMessageDialog( this, "Partie terminée !", "Gameover",
                        JOptionPane.WARNING_MESSAGE );
            }
            JDialog fenErr = d.createDialog(this, "Gameover");
        }
    }

    public void setModele(Modele mod){
        modele = mod;
    }

    public void updaterScore(){
        score.setText("Score : "+modele.scoreActuel());
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
        mi_new.addActionListener(a);
        mi_scores.addActionListener(a);
    }

    public void afficherScores(){
        JOptionPane d = new JOptionPane();
        String scoreText ="";
        for (int i = 4; i <= 6; i++){
            scoreText += "\nGrille de "+i+"x"+i+":\n";
            for (int s : modele.getScores(i)){
                scoreText += "    "+s+"\n";
            }
        }
        d.showMessageDialog( this, scoreText, "Scores",
                JOptionPane.PLAIN_MESSAGE );
        JDialog fenErr = d.createDialog(this, "Scores");
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
                new Object[] {tabbedPane},
                "Scores",
        JOptionPane.DEFAULT_OPTION,
        JOptionPane.INFORMATION_MESSAGE, null, null, null);
    }
}