package projet_BD_reseau;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Select {

    public static boolean selectUtilisateur (Connection con, String numero, String id_lecteur) {
        Statement statement = null;
        ResultSet rs = null;

        try {
            String query = "SELECT * FROM utilisateur WHERE numero = '"+numero+"';";
            statement = con.createStatement();
            rs=statement.executeQuery(query);

            if(rs.next()==true) {
                System.out.print("\nLa requête a été envoyée. Le lecteur "+id_lecteur+" dit : bonjour "+rs.getString("prenom")+" !");
                return true;
            }else {
                System.out.print("\nLa requête a été envoyée. Le lecteur "+id_lecteur+" dit : ce numero d'utilisateur n'existe pas.");
                return false;
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean selectLecteur (Connection con, String id_lecteur) {
        Statement statement = null;
        ResultSet rs = null;

        try {
            String query = "SELECT * FROM salle WHERE id_lecteur = '"+id_lecteur+"';";
            statement = con.createStatement();
            rs=statement.executeQuery(query);

            if(rs.next()==true) {
                System.out.print("\nLa requête a été envoyée. Le Lecteur "+rs.getString("id_lecteur")+" existe bien.");
                return true;
            }else {
                System.out.print("\nLa requête a été envoyée mais ce lecteur n'existe pas.");
                return false;
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean selectEtu(Connection con, String id_lecteur, String numero, String date, String heure) {
        Statement statement = null;
        ResultSet rs = null;
        Boolean res = false; //res sera à true si on rentre dans rs.next, sinon, il sera a faux
        Boolean exist = false;

        //pour vérifier le numero et la query on fait dans la boucle while avec un boolean

        try {
            String query = "SELECT numero_salle FROM salle WHERE id_lecteur = '"+id_lecteur+"'";
            String query1 = "SELECT code_cours FROM cours WHERE heure_debut <= '"+heure+"' AND heure_fin >= '"
                    +heure+"' AND date_jour = '"+date+"' AND salle = ("+query+")";
            String query2 = "SELECT * FROM inscrit WHERE cours_inscrit = ("+query1+");";
            statement = con.createStatement();
            rs=statement.executeQuery(query2);
            while(rs.next()) {
                String numeroetu = rs.getString("numero_etudiant");
                if (numeroetu.equals(numero)) {
                    exist = true;
                }
                res = true;
            }
            if (res==true) {
                System.out.println("Il y a au moins un étudiant qui est bien inscrit pour ce cours.");
                if (exist==true) {
                    System.out.println("c'est le bon étudiant");
                    return true;
                }
                else {
                    System.out.println("toi t'es pas inscrit mais quelqu'un d'autre oui");
                    return false;
                }
            }else {
                System.out.println("Pas d'étudiant inscrits.");
                return false;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean selectProf(Connection con, String id_lecteur, String numero, String date, String heure) {
        Statement statement = null;
        ResultSet rs = null;
        Boolean res = false; //res sera à true si on rentre dans rs.next, sinon, il sera a faux

        try {
            String query = "SELECT numero_salle FROM salle WHERE id_lecteur = '"+id_lecteur+"'";
            String query1 = "SELECT * FROM cours WHERE heure_debut <= '"+heure+"' AND heure_fin >= '"+heure+"' AND date_jour = '"
                    +date+"' AND salle=("+query+");";
            statement = con.createStatement();
            rs=statement.executeQuery(query1);
            while(rs.next()) {
                System.out.print("\nLa requête a été envoyée, le numero est : "+rs.getString("enseignant"));
                System.out.println(" ");
                res = true;
            }
            if (res==true) {
                System.out.println("Cet enseignant va bien donner un cours dans cette salle.");
                return true;
            }else {
                System.out.println("Cet enseignant ne donne PAS de cours ici");
                return false;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
