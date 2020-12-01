package projet_BD_reseau;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;

public class Insert_value {

    public static void insertHistorique_salle(Connection connection, String id_lecteur, String numero, String date) {
        Statement statement = null;
        Statement statement2 = null;
        Statement statement3=null;
        ResultSet rsI = null;
        ResultSet rsSalle = null;
        //On considère que la table historique_salle n'est jamais vide

        try {
            String queryI = "SELECT * FROM historique_salle ORDER BY numero_historique_salle DESC LIMIT 1;";
            statement2 = connection.createStatement();
            rsI=statement2.executeQuery(queryI);

            String querySalle = "SELECT * FROM salle WHERE id_lecteur = '"+id_lecteur+"'";
            statement3 = connection.createStatement();
            rsSalle=statement3.executeQuery(querySalle);

            if((rsI.next()==true)&&(rsSalle.next()==true)) {
                int i = Integer.parseInt(rsI.getString("numero_historique_salle"));
                i = i+1;
                String salle = rsSalle.getString("numero_salle");
                String query = "INSERT INTO historique_salle (numero_historique_salle, numero_salle, utilisateur, date_presence) VALUES ('"
                        +i+"', '"+salle+"', '"+numero+"', '"+date+"');";
                statement = connection.createStatement();
                statement.executeUpdate(query);
                System.out.println("Successfull insertion");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void insert(Connection connection, String numero, String nom, String prenom, String identifiant, String mdp) {
        Statement statement = null;

        try {
            String query = "INSERT INTO utilisateur (numero, nom, prenom, identifiant, mdp) VALUES ('"+numero+"', '"+nom+"', '"+prenom+"', '"+identifiant+"', '"+mdp+"');";
            statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Successfull insertion");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
