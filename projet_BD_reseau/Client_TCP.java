package projet_BD_reseau;

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
import java.util.Timer;
import java.util.TimerTask;

public class Client_TCP {
    static HashMap<String, Integer> toBlock = new HashMap<String, Integer>();
    //Voici list à mémoriser, voilà les donnés current
    static String id;
    static String numero;
    static String date;
    static String heure;
    static deBlock toto = new deBlock();
    static Timer tata = new Timer();

    public static void main (String[] args) throws IOException {
        Socket socket = null ;
        PrintWriter flux_sortie = null ;
        BufferedReader flux_entree = null ;
        String chaineConsole ;
        String[] reponse;
        boolean acquittement = false;
        boolean verif;

        try {
            //IP DEPEND DE CHACUN - OU METTRE LOCALHOST
            //EMMA : 192.168.0.30
            //JULIA : 172.20.10.4 (partage de co) ou 192.168.1.67 (wifi maison) ou 192.168.1.35 (wifi spac) ou 192.168.1.35 (wifi mamie)
            //HAOZHENG : 192.168.1.7
            socket = new Socket ("192.168.1.35", 5000) ;
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

            chaineConsole = entree_standard.readLine ();
            flux_sortie.println (chaineConsole) ;

            //je ne sais pas si pour lire chaineConsole il ne faut pas faire comme pour flux_entree ci-dessous et utiliser while
        		chaineConsole = entree_standard.readLine ();
        		verif = decomp(chaineConsole);
        		if(verif) {
        		    if (toBlock.get(id)==3){
                        System.out.println("Lentement! Vous êtes déjà bloqué!");
                    }else {
                        flux_sortie.println (chaineConsole) ;
                    }
        		}

            //je pensais on pourrait mettre compteur dans les if des msg reçu du serveur
            //Peut être quand on appelle la méthode de vérification de caractères on décrémente compteur ?? Je pense que c'est la bonne solution

/////////////////////// LECTURE DE LA RÉPONSE DU SERVEUR ///////////////////////

            chaineConsole = flux_entree.readLine () ;
            reponse = decompReponse(chaineConsole);

            if (reponse!=null) {
                System.out.println ("Le serveur m'a repondu : " + chaineConsole) ;

                if(chaineConsole.equals("Au revoir !")) {
                    System.out.println("TCHAO");
                    break;
                }

                if(chaineConsole.equals("Alerte incendie")) {
                    System.out.println("Alerte incendie, les portes restent ouvertes.");
                }

                if (reponse[0].equals("Bien reçu")) {
                    System.out.println("Le serveur a bien reçu notre paquet, attendons sa réponse.");
                    acquittement = true;
                }

                if (acquittement) {
                    if (reponse[1].equals("Autorisé")) {
                        System.out.println("Autorisation d'entrer, la porte est ouverte.");
                        //DEMANDE DE REMPLIR HISTORIQUE_SALLE
                        acquittement = false;
                        toBlock.remove(id);
                    }
                    if (reponse[1].equals("Refusé")) {
                        System.out.println("Refus d'entrer, la porte reste fermée.");
                        acquittement = false;
                        if(toBlock.containsKey(id)){
                            if(toBlock.get(id)!=2){
                                toBlock.put(id,(toBlock.get(id)+1));
                            }else {
                                System.out.println("Vérifiez bien votre badge il y a peut-être un problem! ");
                                toto.setId(id);
                                tata.schedule(toto,1000);
                            }
                        }else {
                            toBlock.put(id,1);
                        }
                    }
                    if (reponse[1].equals("Utilisateur inconnu")) {
                        System.out.println("L'utilisateur est inconnu, la porte reste fermée. Veuillez repasser votre badge.");
                        acquittement = false;
                        if(toBlock.containsKey(id)){
                            if(toBlock.get(id)!=2){
                                toBlock.put(id,(toBlock.get(id)+1));
                            }else {
                                toBlock.put(id,(toBlock.get(id)+1));
                                System.out.println("Vérifiez bien votre badge il y a peut-être un problem! ");
                                toto.setId(id);
                                tata.schedule(toto,1000);
                            }
                        }else {
                            toBlock.put(id,1);
                        }
                    }
                    if (reponse[1].equals("Lecteur inconnu")) {
                        System.out.println("Ce lecteur est inconnu, la porte reste fermée. Méfiez-vous, c'est peut-être un lecteur pirate.");
                        acquittement = false;
                        if(toBlock.containsKey(id)){
                            if(toBlock.get(id)!=2){
                                toBlock.put(id,(toBlock.get(id)+1));
                            }else {
                                toBlock.put(id,(toBlock.get(id)+1));
                                System.out.println("Vérifiez bien votre badge il y a peut-être un problem! ");
                                toto.setId(id);
                                tata.schedule(toto,1000);
                            }
                        }else {
                            toBlock.put(id,1);
                        }
                    }
                    if (reponse[1].equals("Paquet non valide")) {
                        System.out.println("Le serveur n'a pas lu le paquet. Veuillez envoyer un paquet avec le format demandé");
                        acquittement = false;
                        if(toBlock.containsKey(id)){
                            if(toBlock.get(id)!=2){
                                toBlock.put(id,(toBlock.get(id)+1));
                            }else {
                                System.out.println("Vérifiez bien votre badge il y a peut-être un problem! ");
                                toBlock.put(id,(toBlock.get(id)+1));
                                toto.setId(id);
                                tata.schedule(toto,1000);
                            }
                        }else {
                            toBlock.put(id,1);
                        }
                    }
                }
            }else {
                System.out.println("Pas de réponse");
            }
        } while (chaineConsole != null) ;

        flux_sortie.close () ;
        flux_entree.close () ;
        entree_standard.close () ;
        socket.close () ;
    }

    /////////////////////// VERIFICATION DE LA CHAINE PASSÉE EN PARAMETRE ///////////////////////
    public static boolean decomp(String chaine) {

        System.out.println("Votre information est : \""+chaine+"\"");
        String[] chaine2=chaine.split(" ");

        if (chaine2.length ==4) {
            id = chaine2[0];
            numero = chaine2[1];
            date = chaine2[2];
            heure = chaine2[3];
            System.out.println("identificateur : \""+id+"\"\nnumero : \""+numero+"\"\ndate : \""+date+"\"\nheure : \""+heure+"\"");
            return true;
        } else {
            System.out.println("Les informations ne correspondent pas à ce qui est attendu !");
            return false;
        }
    }

    public static String[] decompReponse(String chaineReponse) {
        try {
            String[] chaine=chaineReponse.split("-");
            return chaine;
        }
        catch(NullPointerException e) {
            System.err.println ("Le paquet n'est pas correct") ;
            return null;
        }
    }

    static class deBlock extends TimerTask{
        String id;
        public void setId(String id) {
            this.id = id;
        }
        @Override
        public void run() {
            toBlock.put(id,0);
        }
    }
}

