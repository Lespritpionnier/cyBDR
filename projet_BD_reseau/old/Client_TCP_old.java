package projet_BD_reseau.old;

/**
 * DE SOUSA Julia, GASTEBOIS Emma et WANG Haozheng - 2020
 * Client TCP
 */

import java.io.IOException ;
import java.io.BufferedReader ;
import java.io.InputStreamReader ;
import java.io.PrintWriter ;
import java.net.Socket ;
import java.net.UnknownHostException ;
import java.util.HashMap;

public class Client_TCP_old {
    static HashMap<String, Integer> toBlock = new HashMap<String, Integer>();
    //Voici list à mémoriser, voilà les donnés current
    static String id;
    static String numero;
    static String date;
    static String heure;

    public static void main (String argv []) throws IOException {
        Socket socket = null ;
        PrintWriter flux_sortie = null ;
        BufferedReader flux_entree = null ;
        String chaineConsole ;
        String chaineServeur;
        int compteur = 0;
        boolean acquittement = false;
        boolean verif;


        try {
            //IP DEPEND DE CHACUN - OU METTRE LOCALHOST
            //EMMA : 192.168.0.30
            //JULIA : 172.20.10.4
            //HAOZHENG :192.168.1.7
            socket = new Socket ("172.20.10.4", 5000) ;
            flux_sortie = new PrintWriter (socket.getOutputStream (), true) ;
            flux_entree = new BufferedReader (new InputStreamReader (socket.getInputStream ())) ;
        }
        catch (UnknownHostException e) {
            System.err.println ("L'hote est inconnu") ;
            System.exit (1) ;
        }

        System.out.println("Format du paquet à envoyer = XX XXXXXXXX XXXX-XX-XX XX:XX:XX"); //YYYY-MM-JJ
        BufferedReader entree_standard = new BufferedReader ( new InputStreamReader ( System.in)) ;

        do {

/////////////////////// LECTURE DU MSG PASSÉ EN CONSOLE ////////////////////////////

            //je ne sais pas si pour lire chaineConsole il ne faut pas faire comme pour flux_entree ci-dessous et utiliser while
            if (compteur < 4) {
                chaineConsole = entree_standard.readLine ();
                verif = decomp(chaineConsole);
                if (verif) {
                    flux_sortie.println (chaineConsole) ;
                }
            }

            //je pensais on pourrait mettre compteur dans les if des msg reçu du serveur
            //Peut être quand on appelle la méthode de vérification de caractères on décrémente compteur ?? Je pense que c'est la bonne solution

/////////////////////// LECTURE DE LA RÉPONSE DU SERVEUR ///////////////////////
            chaineServeur = flux_entree.readLine () ;
            System.out.println ("Le serveur m'a repondu : " + chaineServeur) ;

            if(chaineServeur.equals("Alerte incendie")) {
                System.out.println("Alerte incendie, les portes restent ouvertes.");
            }

            else if (chaineServeur.equals("Bien reçu")) {
                System.out.println("Le serveur a bien reçu notre paquet, attendons sa réponse.");
                acquittement = true;
            }

            if (acquittement) {
                if (chaineServeur.equals("Autorisé")) {
                    System.out.println("Autorisation d'entrer, la porte est ouverte.");
                    //DEMANDE DE REMPLIR HISTORIQUE_SALLE
                    acquittement = false;
                    compteur=0;
                }
                else if (chaineServeur.equals("Refusé")) {
                    System.out.println("Refus d'entrer, la porte reste fermée.");
                    acquittement = false;
                    int fois = toBlock.get(id);
                    if(fois==3){

                    }else {
                        toBlock.put(id,fois+1);
                    }
                    compteur++;
                }
                else if (chaineServeur.equals("Utilisateur inconnu")) {
                    System.out.println("L'utilisateur est inconnu, la porte reste fermée. Veuillez repasser votre badge.");
                    acquittement = false;
                    compteur++;
                }
                else if (chaineServeur.equals("Lecteur inconnu")) {
                    System.out.println("Ce lecteur est inconnu, la porte reste fermée. Méfiez-vous, c'est peut-être un lecteur pirate.");
                    acquittement = false;
                    compteur++;
                }
            }

        } while (chaineServeur != null) ;

        flux_sortie.close () ;
        flux_entree.close () ;
        entree_standard.close () ;
        socket.close () ;
    }

    /////////////////////// VERIFICATION DE LA CHAINE PASSÉE EN PARAMETRE ///////////////////////
    public static boolean decomp(String chaine) {
        System.out.println("Votre information est : \""+chaine+"\"");
        String[] chaine2=chaine.split(" ");

        //LA CHAINE DOIT CONTENIR 4 "MSG"
        if (chaine2.length ==4) {
            id = chaine2[0];
            numero = chaine2[1];
            date = chaine2[2];
            heure = chaine2[3];
            System.out.println("identificateur : \""+id+"\"\nnumero : \""+numero+"\"\ndate : \""+date+"\"\nheure : \""+heure+"\"");
            if(!toBlock.containsKey(id)) {
                toBlock.put(id,0);
            }
            return true;
        } else {
            id = null;
            numero = null;
            date = null;
            heure = null;
            System.out.println("Les informations ne correspondent pas à ce qui est attendu !");
            return false;
        }
    }
}
