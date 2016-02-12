package demineur;

import java.util.Random;

/**
 * Created by aprouzeau on 13/01/2016.
 */
public class DemineurModel {


    // 0 : Normal
    // 1 : Mine

    private int[][] grille;


    public DemineurModel(int n, int nombreMines){
        this.grille = new int[n][n];
        this.placerMines(nombreMines);
        this.printGrille();
    }

    public int[][] getGrille() {
        return grille;
    }

    public void setGrille(int[][] grille) {
        this.grille = grille;
    }

    public void setCase(int x, int y, int v){
        this.grille[x][y] = v;
    }

    public int getCase(int x, int y){
        return this.grille[x][y];
    }

    //Fonction pour placer un nombre de mines n aléatoirement
    public void placerMines(int nbMine){
        int nbMineToReduce = nbMine;
        if(nbMineToReduce < this.grille.length*this.grille.length){
            while(nbMineToReduce > 0){
                int i = DemineurModel.randomInt(0, this.grille.length-1);
                int j = DemineurModel.randomInt(0, this.grille.length-1);
                if(this.grille[i][j] == 0){
                    this.grille[i][j] = 1;
                    nbMineToReduce --;
                }
            }
        }
        else{
            System.out.println("Erreur : Il y a trop de mine par rapport au nombre de case...");
        }
    }

    //Fonction qui vérifie si une case est une mine
    public boolean estMine(int x, int y){
        return this.grille[x][y] == 1;
    }

    //Fonction pour calculer le nombre de mines sur les cases adjacentes, retourne -1 si la case demandée est une mine
    public int nombreDeVoisin(int x, int y){
        if(estMine(x, y)){
            return -1;
        }
        int nbDeMine = 0;
        for(int i = x-1; i < x+2; i++){
            for(int j = y-1; j < y+2; j++){
                if(i >= 0 && i<this.grille.length && j >= 0 && j<this.grille.length){
                    nbDeMine += this.grille[i][j];
                }
            }
        }
        return nbDeMine;
    }

    //Fonction pour afficher la grille sur la console
    public void printGrille(){
        for(int i = 0; i<this.grille.length; i++){
            for(int j = 0; j<this.grille.length; j++){
                System.out.print(" "+this.grille[i][j]+" ");
            }
            System.out.println("\n");
        }
    }

    //Fonction permettant de générer un entier entre min et max
    public static int randomInt(int min, int max){
        Random rand = new Random();
        int randomInt = rand.nextInt((max - min) + 1) + min;
        return randomInt;
    }

}
