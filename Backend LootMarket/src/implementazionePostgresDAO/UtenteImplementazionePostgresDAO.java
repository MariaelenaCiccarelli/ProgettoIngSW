package implementazionePostgresDAO;

import Database.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.SQLException;

public class UtenteImplementazionePostgresDAO {

    private Connection connection;

    public UtenteImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}