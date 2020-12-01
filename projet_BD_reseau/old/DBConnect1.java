package projet_BD_reseau.old;

import java.sql.*;

import static java.sql.DriverManager.getConnection;

public class DBConnect1 {
    public static void main(String[] args) throws Exception {

        /* 1) PostgreSQL的连接信息 */

        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "l";

        /* 2) 定义JDBC驱动 */
        //Class.forName("org.postgresql.Driver");

        /* 3) 连接PostgreSQL */
        try (Connection con = getConnection(url, user, password)) {
            try (Statement st = con.createStatement()) {

                /* 4) 执行SELECT语句 */
                try (ResultSet rs = st.executeQuery("\n" +
                        "CREATE TABLE utilisateur( \n" +
                        "\tnumero CHAR(8),\n" +
                        "\tnom VARCHAR(30),\n" +
                        "\tprenom VARCHAR(30),\n" +
                        "\tdate_naissance DATE,\n" +
                        "\tidentifiant VARCHAR(50),\n" +
                        "\tmdp VARCHAR(10),\n" +
                        "\tCONSTRAINT utilisateur_pk PRIMARY KEY (numero)\n" +
                        ");\n")) {

                    /* 5) 显示结果画面 */
                    rs.next();
                    System.out.print(rs.getInt("col_1"));

                    /* 6) 切断与PostgreSQL的连接 */
                    rs.close();
                }
                st.close();
            }
            con.close();
        }
    }
}

