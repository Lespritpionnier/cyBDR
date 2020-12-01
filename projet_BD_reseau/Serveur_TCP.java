package projet_BD_reseau;

/**
 * DE SOUSA Julia, GASTEBOIS Emma et WANG Haozheng - 2020
 * Client TCP
 */

import java.net.ServerSocket ;
import java.net.Socket ;
import java.sql.Connection;
import java.sql.DriverManager;
import java.io.IOException ;
import java.io.BufferedReader ;
import java.io.InputStreamReader ;
import java.io.PrintWriter ;

public class Serveur_TCP {
    public static void main (String[] args) throws IOException {
        String username = "postgres"; //a personnaliser
        String password = "juju2816"; //a personnaliser

        Connection con= null;
        con = get_Connection(username,password);

        ServerSocket serverSocket = null ;
        serverSocket = new ServerSocket (5000) ;

        Socket clientSocket = null ;
        try {
            clientSocket = serverSocket.accept () ;
        }
        catch (IOException e) {
            System.err.println ("La connexion à la socket a echoué.") ;
            System.exit (1) ;
        }

        PrintWriter flux_sortie = new PrintWriter (clientSocket.getOutputStream (), true) ;
        BufferedReader flux_entree = new BufferedReader (new InputStreamReader (clientSocket.getInputStream ())) ;
        // BufferedReader entree_standard = new BufferedReader ( new InputStreamReader ( System.in)) ;

        String chaine_entree;
        // String chaine_console;
        boolean valide = false; //savoir si le msg est valide
        boolean requete = false; //requete sql (autorisé à entrer ou non)
        boolean reponseEnvoyee = false;

        // chaine_console = entree_standard.readLine () ;

///////////////////////// LECTURE DU MSG ENVOYÉ PAR LE CLIENT ////////////////////////////
        while ((chaine_entree = flux_entree.readLine()) != null) {
            System.out.println ("J'ai recu " + chaine_entree) ;

            if (chaine_entree.equals ("Au revoir !")) {
                flux_sortie.println ("Au revoir !");
                break ;
            }
            else {

                String id_lecteur = recupID(chaine_entree);
                String numero = recupNumero(chaine_entree);
                String date = recupDate(chaine_entree);
                String heure = recupHeure(chaine_entree);

                if(id_lecteur!=null && numero!=null && date!=null && heure!=null) {
                    //commencer a différencier les cas en fontion de la requete si jamais le paquet est bon
                    valide = formatMessageValide(id_lecteur, numero, date, heure);

                    if (valide == true) {
                        System.out.println("paquet valide");

                        //lecteur existe ?
                        Boolean lecteur = Select.selectLecteur(con, id_lecteur);
                        if (lecteur == true) {
                            //utilisateur existe ?
                            Boolean utilisateur = Select.selectUtilisateur(con, numero, id_lecteur);
                            if(utilisateur == true) {
                                if (isEtu(numero).equals("1")) {
                                    //c'est un etu
                                    Boolean etudiant = Select.selectEtu(con, id_lecteur, numero, date, heure);
                                    if (etudiant == true) {
                                        Insert_value.insertHistorique_salle(con, id_lecteur, numero, date);
                                        requete = true;
                                    }
                                }else {
                                    Boolean professeur = Select.selectProf(con, id_lecteur, numero, date, heure);
                                    if (professeur == true) {
                                        Insert_value.insertHistorique_salle(con, id_lecteur, numero, date);
                                        requete = true;
                                    }
                                }
                            }
                            else {
                                flux_sortie.println ("Bien reçu-Utilisateur inconnu");
                                requete = false;
                                reponseEnvoyee = true;
                            }
                        }
                        else {
                            flux_sortie.println ("Bien reçu-Lecteur inconnu");
                            requete = false;
                            reponseEnvoyee = true;
                        }

                        if (requete==true && reponseEnvoyee==false) {
                            flux_sortie.println ("Bien reçu-Autorisé");
                            requete = false;
                            reponseEnvoyee = false;
                        }
                        else if (requete==false && reponseEnvoyee==false){
                            //si l'utilisateur n'a pas cours
                            flux_sortie.println ("Bien reçu-Refusé");
                            requete = false;
                            reponseEnvoyee = false;
                        }
                    }
                    else {
                        System.out.println("paquet invalide");
                        flux_sortie.println("Bien reçu-Paquet non valide");
                        requete = false;
                    }
                }
                else {
                    System.out.println("paquet invalide");
                    flux_sortie.println("Bien reçu-Paquet non valide");
                    requete = false;
                }
            }
        }

        //je ne sais pas si on doit mettre ce if dans la boucle while il faudrait faire des tests dans une autre classe
        //et je ne sais pas si pour lire chaine_console il ne faut pas faire comme pour chaine_entree ci-dessus et utiliser while

       	/*if(chaine_console.equals("incendie")) {
       		flux_sortie.println ("Alerte incendie") ;
       	}*/

        flux_sortie.close () ;
        flux_entree.close () ;
        clientSocket.close () ;
        serverSocket.close () ;
    }

    public static Connection get_Connection(String username, String password) {
        Connection con= null;
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/projetBD",username,password);

            if(con !=null) {
                System.out.println("connexion établie");
            }
            else {
                System.out.println("Problème de connexion");
            }
        }

        catch (Exception e) {
            System.out.println(e);
        }
        return con;
    }

    public static String recupID (String chaine) {
        //DÉCOMPOSER LE MSG DU CLIENT EN "PETITS MSG" : ID NUMERO DATE HEURE
        String[] chaine2=chaine.split(" ");

        //DÉCOMPOSER LE CHAQUE "MSG" EN CARACTERES UNIQUES AFIN DE VÉRIFIER LEUR TAILLE
        String id =chaine2[0];
        return id;
    }

    public static String recupNumero (String chaine) {
        try {
            String[] chaine2=chaine.split(" ");
            String numero =chaine2[1];
            return numero;
        }
        catch(ArrayIndexOutOfBoundsException e) {
            System.err.println ("Le paquet n'est pas correct") ;
            return null;
        }
    }

    public static String recupDate (String chaine) {
        try {
            String[] chaine2=chaine.split(" ");
            String date =chaine2[2];
            return date;
        }
        catch(ArrayIndexOutOfBoundsException e) {
            System.err.println ("Le paquet n'est pas correct") ;
            return null;
        }
    }

    public static String recupHeure (String chaine) {
        try {
            String[] chaine2=chaine.split(" ");
            String heure =chaine2[3];
            return heure;
        }
        catch(ArrayIndexOutOfBoundsException e) {
            System.err.println ("Le paquet n'est pas correct") ;
            return null;
        }
    }

    public static String isEtu (String numero) {
        try {
            String[] numeroTab=numero.split("");
            if (numeroTab[1].equals("1")) {
                return numeroTab[1];
            }else {
                return "2";
            }
        }
        catch(ArrayIndexOutOfBoundsException e) {
            System.err.println ("Le paquet n'est pas correct") ;
            return "0";
        }
    }

    public static boolean  formatMessageValide(String id, String numero, String date, String heure) {
        //On décompose les données
        String[] idTab=id.split("");
        String[] numeroTab=numero.split("");
        String[] dateTab=date.split("");
        String[] heureTab=heure.split("");

        //TESTER SI LE MSG EST BIEN FORMÉ
        if (idTab.length == 2 && numeroTab.length == 8 && dateTab.length == 10 && heureTab.length == 8){
            System.out.println("Le format du paquet est le bon");
            return true;
        }else {
            System.out.println("Le format du paquet n'est pas le bon");
            return false;
        }
    }
}
