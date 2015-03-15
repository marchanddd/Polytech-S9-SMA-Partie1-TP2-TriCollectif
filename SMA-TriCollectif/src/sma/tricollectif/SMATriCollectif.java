package sma.tricollectif;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Coordonnees;

public class SMATriCollectif {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        initiate(10, 4, 20, 1, 8, 0.1f, 0.3f);
        //start(50, 20, 200, 1, 8, 0.1f, 0.3f);
    }//main()
    
    public static void initiate(int taille, int nbAgents, int nbObjets, int i, int t, float kp, float km) {
        Grille grille = new Grille(taille);
        ArrayList<Agent> la = new ArrayList();
        for (int x = 0; x < nbAgents; x++) {
            Agent a = new Agent(String.valueOf(x), grille, generateCoordinate(grille), i, t, kp, km);
            la.add(a);
            try {
                grille.putAgent(a);
            } catch (Exception ex) {
                Logger.getLogger(SMATriCollectif.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (int x = 0; x < nbObjets; x++) {
            try {
                grille.putObjet(generateCoordinate(grille), new Objet('A'));
                grille.putObjet(generateCoordinate(grille), new Objet('B'));
            } catch (Exception ex) {
                Logger.getLogger(SMATriCollectif.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        for (Agent a : la) {
            a.start();
        }
        while (true) { //tant que le tri n'est pas satisfaisant on boucle 
            grille.print();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(SMATriCollectif.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//start()
    
    public static Coordonnees generateCoordinate(Grille grille) {
        Coordonnees c;
        do {
            c = new Coordonnees(
                    (int) (Math.random() * grille.getTaille()), 
                    (int) (Math.random() * grille.getTaille())
            );
        } while (! grille.isLibre(c));
        return c;
    }
    
}
