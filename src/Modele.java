import java.io.*;

/**
 * Created by pphelipo on 23/11/15.
 * Github : https://github.com/pasterp/Jeu2048
 * License : http://www.apache.org/licenses/LICENSE-2.0
 */

public class Modele{
    int[][] grid;
    boolean[][] gridb;
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
        gridb = new boolean[n][n];
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
            //On ouvre le fichier de score correspondant à chaque taille (4, 5, 6)
            File scoreF =new File(base+(i+4)+"x"+(i+4)+".dat");
            try{
                //on lit les scores (3 entiers)
                DataInputStream fichier = new DataInputStream(new BufferedInputStream(new FileInputStream(scoreF)));
                for (int j=0; j<3; j++){
                    scores[i][j] = fichier.readInt();
                }
            }catch(IOException e){
                System.out.println("Attention : les scores n'ont pas pu être chargés !");
                //on met à zéro les trois scores si on a pas pu les charger
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
            //On ouvre le fichier de score correspondant à chaque taille (4, 5, 6)
            File scoreF =new File(base+(i+4)+"x"+(i+4)+".dat");
            try{
                //on ecrit les 3 scores
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
     * Tente d'insérer le score actuel dans les meilleurs scores. Retourne True après avoir sauvegardé les scores si c'est le cas.
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
            //On a changé le classement, il faut le sauvegarder dans les fichiers de score !
            writeScores();
            return true;
        }
        return false;
    }

    /**
     * Renvoie les meilleurs scores de la taille demandée
     * @param taille taille de grille
     * @return Tableaux des trois meilleurs scores
     */
    public int[] getScores(int taille){
        return scores[taille-4];
    }

    /**
     * Permet de changer la taille de la grille et la réinitialise.
     * @param taille Nouvelle taille pour la grille
     */
    public void changeGrille(int taille){
        grid = new int[taille][taille];
        gridb = new boolean[taille][taille];
        insertNew();
    }

    /**
     * Retourne la taille de la grille actuelle
     * @return taille de la grille
     */
    public int getTaille(){
        return grid.length;
    }

    /**
     * Retourne la grille de jeu actuelle
     * @return Tableaux à deux dimensions de Int
     */
    public int[][] getGrille(){
        return grid;
    }

    /**
     * Insère une nouvelle case 2 dans la grille à un emplacement libre.
     */
    public void insertNew(){
        int nb =0;
        //On compte les cases vides sur la grille
        for (int y=0; y<grid.length; y++) {
            for (int x=0; x<grid[y].length; x++) {
                if (grid[y][x]==0) {
                    nb++;
                }
            }
        }
        //On obtient donc une chance de 1/nombre de cases vides d'avoir un 2 dans une case.

        while(true) { // On recommence tant que l'on a pas insérer de 2
            for (int y = 0; y < grid.length; y++) {
                for (int x = 0; x < grid[y].length; x++) {
                    if (grid[y][x] == 0) {
                        if ((int) (Math.random() * nb) == 1 || nb == 1) {
                            grid[y][x] = 2;
                            return;
                        }
                    }
                }
            }
        }
    }

    private void resetGridB(){
        //Met toute la grille de boolean à false
        for (int y=0; y<gridb.length; y++) {
            for (int x=0; x<gridb.length; x++) {
                gridb[y][x] = false;
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
        resetGridB();
        do {
            change = false;
            //Selon le sens du déplacement les cases doivent fusionner dans des sens différents
            //Par exemple si on va à droite on a besoin de fusionner de droite à gauche
            for (int y=((dirY>0) ? grid.length-1 : 0); (dirY>0 && y>=0) || (dirY<=0 && y< grid.length); y=y+((dirY>0)? -1:1) )  {
                for (int x=((dirX>0) ? grid.length-1 : 0); (dirX>0 && x>=0) || (dirX<=0 && x< grid.length); x=x+((dirX>0)? -1:1) ) {
                    //Si la case n'est pas vide ou si le déplacement ne sort pas de la grille on tente de la déplacer
                    if (grid[y][x] != 0 && (y+dirY) >= 0 && (y+dirY) < grid.length && (x+dirX) >= 0 && (x+dirX) < grid.length){
                        //Si la case n'a pas encore été fusionnée et si la case visée et la meme on les fusionne
                        if ((!gridb[y][x] && grid[y][x] == grid[y+dirY][x+dirX])) {
                            //On doit fusionner
                            grid[y+dirY][x+dirX]+=grid[y][x];
                            gridb[y+dirY][x+dirX]=true;
                            grid[y][x]=0;
                            gridb[y][x]=false;
                            change = true;
                            bouge=true;
                        }

                        if (grid[y+dirY][x+dirX]==0){
                            grid[y+dirY][x+dirX]+=grid[y][x];
                            gridb[y+dirY][x+dirX]=gridb[y][x];
                            grid[y][x]=0;
                            gridb[y][x]=false;
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