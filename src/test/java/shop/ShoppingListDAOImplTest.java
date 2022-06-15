package shop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import static java.sql.DriverManager.getConnection;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

// Integration Test
public class ShoppingListDAOImplTest {

    public static final String ERROR = "Connection to database is impossible.";
    public static final String INSERT_SQL = "INSERT INTO item(name, quantity) VALUES(?,?)";
    public static final String SELECT_SQL = "SELECT * FROM item order by id DESC limit 1";
    public static final String DELETE_SQL = "DELETE FROM item order by id DESC limit 1";
    private String host;
    private String user;
    private String pass;

    @BeforeEach
    void setUp() {
        try( FileInputStream configFile = new FileInputStream("db-config.properties")) {
            Properties properties = new Properties();
            properties.load(configFile);
            host = properties.get("host").toString();
            user = properties.get("username").toString();
            pass = properties.get("password").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void should_connect_to_mysql_database() {
        try(final Connection con = getConnection(host, user, pass)) {
            if (con == null)
                fail(ERROR);

        } catch (SQLException e) {
            e.printStackTrace();
            fail(ERROR);
        }
    }

    @Test
    void should_insert_and_read_and_delete_an_item_into_item_table() {
        try(final Connection con = getConnection(host, user, pass);
            final PreparedStatement insert = con.prepareStatement(INSERT_SQL);
            final PreparedStatement select = con.prepareStatement(SELECT_SQL);
            final PreparedStatement delete = con.prepareStatement(DELETE_SQL)) {
           insert.setString(1,"Orange");
           insert.setInt(2,5);
           insert.executeUpdate();

            final ResultSet res = select.executeQuery();
            while (res.next()) {
                final String name = res.getString("name");
                final int quantity = res.getInt("quantity");

                assertThat(name).isEqualTo("Orange");
                assertThat(quantity).isEqualTo(5);
            }

            delete.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            fail(ERROR);
        }
    }
}
