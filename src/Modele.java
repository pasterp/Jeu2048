import java.io.*;

/**
 * Created by pphelipo on 23/11/15.
 */

public class Modele{
    int[][] grid;
    int[][] scores;

    /**
     * Creer une nouvelle partie avec une taille de grille de 4x4.
     */
    public Modele(){
        this(4);
    }

    /**
     * Creer une nouvelle partie avec une grille de taille n.
     * @param n Taille de la grille désirée
     */
    public Modele(int n){
        grid = new int[n][n];
        scores = new int[3][3];
        loadScores();
        insertNew();
    }

    /**
     * Charge les scores dans le modèle actuel
     */
    public void loadScores(){
        String base = "./scores_";
        for (int i =0; i<scores.length ; i++){
            File scoreF =new File(base+(i+4)+"x"+(i+4)+".dat");
            try{
                //on lit les scores
                DataInputStream fichier = new DataInputStream(new BufferedInputStream(new FileInputStream(scoreF)));
                for (int j=0; j<3; j++){
                    scores[i][j] = fichier.readInt();
                }
            }catch(IOException e){
                System.out.println("Attention : les scores n'ont pas pu être chargés !");
                //on met à zéro les trois scores
                for (int j=0; j<3; j++)
                    scores[i][j]=0;
            }
        }
    }

    /**
     * Sauvegarde les scores
     */
    public void writeScores(){
        String base = "./scores_";
        for (int i =0; i<scores.length ; i++){
            File scoreF =new File(base+(i+4)+"x"+(i+4)+".dat");
            try{
                //on lit les scores
                DataOutputStream fichier = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(scoreF)));
                for (int s: scores[i]){
                    fichier.writeInt(s);
                }
                fichier.close();
            }catch(IOException e){
                //Impossible d'enregistrer les scores
                System.out.println("Attention : les scores n'ont pas pu être sauvegardés !");
            }
        }
    }

    /**
     * Tente d'insérer le score actuel dans les meilleurs scores. Retourne True après avoir sauvegarder les scores si c'est le cas.
     * @return
     */
    public boolean submitScore(){
        int tmp = scoreActuel();
        for (int j=0; j<3; j++){
            if (tmp > scores[grid.length-4][j]){
                int t = scores[grid.length-4][j];
                scores[grid.length-4][j] = tmp;
                tmp = t;
            }
        }
        if (tmp != scoreActuel()){
            writeScores();
            return true;
        }
        return false;
    }

    public int[] getScores(int taille){
        return scores[taille-4];
    }

    /**
     * Permet de changer la taille de la grille et la réinitialise.
     * @param taille Nouvelle taille pour la grille
     */
    public void changeGrille(int taille){
        grid = new int[taille][taille];
        insertNew();
    }

    public int getTaille(){
        return grid.length;
    }

    public int[][] getGrille(){
        return grid;
    }

    /**
     * Insère une nouvelle case 2 dans la grille à un emplacement libre.
     */
    public void insertNew(){
        int nb =0;
        for (int y=0; y<grid.length; y++) {
            for (int x=0; x<grid[y].length; x++) {
                if (grid[y][x]==0) {
                    nb++;
                }
            }
        }
        boolean insert = false;
        while(insert==false){
            for (int y=0; y<grid.length; y++){
                for (int x=0; x<grid[y].length; x++){
                    if (grid[y][x]==0 && insert==false){
                        if ((int)(Math.random()*nb) == 1  || nb == 1){
                            grid[y][x]=2;
                            insert=true;
                        }
                    }
                    if (insert)
                        break;
                }
                if (insert)
                    break;
            }
        }
    }

    /**
     * Fait tous les déplacemens possibles en direction de l'offset passé en paramètres.
     * @param dirY décalage vertical
     * @param dirX décalage horizontal
     * @return
     */
    private boolean move(int dirY, int dirX){
        boolean change =false, bouge=false;
        do {
            change = false;
            for (int y=0; y<grid.length; y++) {
                for (int x=0; x<grid.length; x++) {
                    if (grid[y][x] != 0 && (y+dirY) >= 0 && (y+dirY) < grid.length && (x+dirX) >= 0 && (x+dirX) < grid.length){
                        if (grid[y][x] == grid[y+dirY][x+dirX] || grid[y+dirY][x+dirX]==0) {
                            //On doit fusionner
                            grid[y+dirY][x+dirX]+=grid[y][x];
                            grid[y][x]=0;
                            change = true;
                            bouge=true;
                        }
                    }
                }
            }
        }while(change);

        return bouge;
    }

    /**
     * Retourne le score actuel (Somme de toutes les cases sur la grille).
     * @return
     */
    public int scoreActuel(){
        int s=0;
        for(int y=0; y<grid.length ; y++)
            for(int x=0; x<grid.length; x++)
                s+=grid[y][x];
        return s;
    }

    /**
     * Retourne True si la grille ne peut plus bouger et donc que la partie est terminée;
     * @return
     */
    public boolean estFinie(){
        boolean bloque = true;
        for(int y=0; y<grid.length ; y++) {
            for (int x = 0; x < grid.length; x++) {
                if (grid[y][x] == 0 || (y + 1 < grid.length && grid[y][x] == grid[y + 1][x]) || (x+1<grid.length && grid[y][x] == grid[y][x + 1])) {
                    bloque = false;
                    break;
                }
            }
            if (!bloque)
                break;
        }
        return bloque;
    }

    /**
     * Déplace les cases vers la droite
     * @return
     */
    public boolean moveRight(){ return this.move(0,1); }
    /**
     * Déplace les cases vers la gauche
     * @return
     */
    public boolean moveLeft() { return this.move(0,-1);}
    /**
     * Déplace les cases vers le haut
     * @return
     */
    public boolean moveUp()   { return this.move(-1,0);}
    /**
     * Déplace les cases vers le bas
     * @return
     */
    public boolean moveDown() { return this.move(1,0); }
}