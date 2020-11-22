package projet_BD_reseau;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class testDecompoString {
    public static void main (String argv []) throws IOException {
        String chaine ;
        System.out.println("Veuillez entrer les informations de votre badge ci-dessous. \nLes informations doivent être de la forme"
                + "\"XXX XXXXXXXX XXXX XXXX\".\n");
        BufferedReader entree_standard = new BufferedReader ( new InputStreamReader ( System.in)) ;

        String id;
        String numero;
        String date;
        String heure;
        int i = 3;

        do {
            chaine = entree_standard.readLine () ;
            System.out.println("Votre information est : \""+chaine+"\"");
            String[] chaine2=chaine.split("\\s");

            //Avec ce if else je traite le cas du manque d'information mais il faut aussi traiter le cas du surplus d'information
            if (i > 0 && i < chaine2.length) {
                id = chaine2[0];
                numero = chaine2[1];
                date = chaine2[2];
                heure = chaine2[3];
                System.out.println("identificateur : \""+id+"\"\nnumero : \""+numero+"\"\ndate : \""+date+"\"\nheure : \""+heure+"\"");
            } else {
                System.out.println("Vous n'avez pas rentrer les informations nécessaires !");
            }
        }
        while(chaine != null);

        entree_standard.close () ;
    }
}
