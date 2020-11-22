package projet_BD_reseau;

/**
 * Tuyet Tram DANG NGOC (dntt) - 2001
 * Client TCP
 */

import java.io.IOException ;
import java.io.BufferedReader ;
import java.io.InputStreamReader ;
import java.io.PrintWriter ;
import java.net.Socket ;
import java.net.UnknownHostException ;

//Ce programme représente la partie client de la conversation entre le client réseau et le serveur réseau
//l'entree standard sera remplie par le message tapé par l'utilisateur dans la console
//Idée : on doit trouver comment lire une cdc caractère par caractère
//Si on u arrive pas, on va devoir faire quatre entrées standard pour récupérer les quatre champs d'infos à envoyer au serveur

public class ClientTCP {
    public static void main (String argv []) throws IOException {
        //on cree la socket, chaine, flux_entree et flux_sortie
        Socket socket = null ;
        PrintWriter flux_sortie = null ;
        BufferedReader flux_entree = null ;
        String chaine ;
        int compteur = 0;

        try {
            //j'ai vérifier l'adresse IP en tapant "ifconfig" dans mon terminal
            // deuxieme argument : le numero de port que l'on contacte
            socket = new Socket ("127.0.0.1", 5000) ;
            flux_sortie = new PrintWriter (socket.getOutputStream (), true) ;
            flux_entree = new BufferedReader (new InputStreamReader (socket.getInputStream ())) ;
        }
        catch (UnknownHostException e) {
            System.err.println ("L'hote est inconnu") ;
            System.exit (1) ;
        }

        // L'entree standard
        BufferedReader entree_standard = new BufferedReader ( new InputStreamReader ( System.in)) ;

        do {
///////////////////////lecture message utilisateur (en console) ////////////////////////////
            if (compteur < 4) {
                // on lit ce que l'utilisateur a tape sur l'entree standard
                chaine = entree_standard.readLine () ;

                ////// on envoie le message au serveur//////
                flux_sortie.println (chaine) ;

                compteur++;
            }

            //IL VA FALLOIR TROUVER UNE AUTRE METHODE POUR LE COMPTEUR CAR LA TROIS BADGES DIFFERENTS VONT BLOQUER LE COMPTEUR

/////////////////////// lecture de ce qu'a envoyé le serveur ////////////////////
            chaine = flux_entree.readLine () ;
            System.out.println ("Le serveur m'a repondu : " + chaine) ;

            //je voulais faire un switch case pour chaque cas mais avec le equals je crois qu'on ne peut pas

            //cas de l'alerte incendie
            if(chaine.equals("Alerte incendie")) {
                System.out.println("Alerte incendie, les portes restent ouvertes");
            }

            //cas de l'acquittement
            //Est ce que pour une notion de sécurité on mettrai pas toutes les possibilités d'autre réponse uniquement après avoir reçu un acquittement?
            //Ca pourrait permettre d'être sur que le message pour laisser les portes ouvertes n'est pas un message pirate ?
            //je sais pas si c'est une bonne idée
            //c'est le cas pour tous SAUF alerte incendie car ce n'est pas une réponse
            if (chaine.equals("Bien reçu")) {
                System.out.println("Le serveur a bien reçu notre paquet, attendons sa réponse");
            }

            //cas d'autorisation
            if (chaine.equals("Autorisé")) {
                System.out.println("Autorisation d'entrer, la porte est ouverte");
            }

            //cas d'autorisation
            if (chaine.equals("Refusé")) {
                System.out.println("Refus d'entrer, la porte reste fermée");
            }

            //cas de l'utilisateur inconnu
            if (chaine.equals("Utilisateur inconnu")) {
                System.out.println("L'utilisateur est inconnu, la porte reste fermée. Veuillez repasser votre badge");
            }

		/*//cas de l'identificateur inconnu
		if (chaine.equals("Identification inconnu")) {
			System.out.println("L'identificateur est inconnu, la porte reste fermée. Veuillez repasser votre badge");
		}*/

        } while (chaine != null) ;

        flux_sortie.close () ;
        flux_entree.close () ;
        entree_standard.close () ;
        socket.close () ;
    }
}
