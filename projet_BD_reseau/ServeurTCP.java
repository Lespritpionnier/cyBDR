package projet_BD_reseau;

/**
 * Tuyet Tram DANG NGOC (dntt) - 2001
 * Serveur TCP
 */
import java.net.ServerSocket ;
import java.net.Socket ;
import java.io.IOException ;
import java.io.BufferedReader ;
import java.io.InputStreamReader ;
import java.io.PrintWriter ;

public class ServeurTCP {
    public static void main (String argv []) throws IOException {
        //on cree la socket du serveur et on lui attribue son port
        ServerSocket serverSocket = null ;
        serverSocket = new ServerSocket (5000) ;

        //on definit une socket qui va accepter la connexion avec le client
        Socket clientSocket = null ;
        try {
            clientSocket = serverSocket.accept () ;
        }
        catch (IOException e) {
            System.err.println ("La connexion à la socket a echoué.") ;
            System.exit (1) ;
        }

        //on definit flux_entree(read) et flux_sortie(write)
        PrintWriter flux_sortie = new PrintWriter (clientSocket.getOutputStream (), true) ;
        BufferedReader flux_entree = new BufferedReader (new InputStreamReader (clientSocket.getInputStream ())) ;

        //on definit les chaines de caractere (entree=message du client , sortie=réponse à envoyer au client)
        String chaine_entree;

        //chaine entree prend la valeur du message que le client a envoye au serveur
        chaine_entree = flux_entree.readLine();

///////////////////////// lecture du message envoyé par le client ////////////////////////////
        while ((chaine_entree) != null) {
            System.out.println ("J'ai recu " + chaine_entree) ;

            //cas de fermeture de connexion
            if (chaine_entree.equals ("Au revoir !")) {
                break ;
            }
            //cas d'acquittement
            else {
                flux_sortie.println ("Bien reçu") ;

                //vérifier ici avec un if si le format du message est le bon du genre
                //on décompose notre message
                //if (format==formatVoulu) {
                //System.out.println("Le format du paquet est le bon");
                //et l'envoie à la base de donnée

                //else{
                //System.out.println("Le format du paquet n'est pas le bon");
                //flux_sortie.println("Le format du paquet n'est pas le bon");
                //}
            }
        }

        //Après, on va devoir envoyé une réponse et ça va être la partie compliqué parce que là on a des vérifications à faire avec la bd
        //et je me demandais si ces réponse on les mettrait pas dans le "if le format du paque est le bon"
        flux_sortie.close () ;
        flux_entree.close () ;
        clientSocket.close () ;
        serverSocket.close () ;
    }
}
