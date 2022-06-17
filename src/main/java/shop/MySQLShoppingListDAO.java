package shop;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import static java.sql.DriverManager.getConnection;

public class MySQLShoppingListDAO implements ShoppingListDAO {

    private static final String INSERT_SQL = "INSERT INTO item(name, quantity) VALUES(?,?)";
    private static final String SELECT_SQL = "SELECT * FROM item ORDER By id ASC LIMIT 10";
    private static final String TRUNCATE_SQL = "TRUNCATE TABLE item";
    private String host;
    private String user;
    private String pass;

    public MySQLShoppingListDAO() {
        loadConfig();
    }

    private void loadConfig() {
        try (InputStream configFile = new FileInputStream("db-config.properties")) {
            Properties properties = new Properties();
            properties.load(configFile);
            initConfig(properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initConfig(Properties properties) {
        host = properties.get("host").toString();
        user = properties.get("username").toString();
        pass = properties.get("password").toString();
    }

    @Override
    public List<Item> findAllItems() {
        List<Item> items = new LinkedList<>();
        try (final Connection con = getConnection(host, user, pass);
             final PreparedStatement select = con.prepareStatement(SELECT_SQL)) {

            final ResultSet res = select.executeQuery();
            while (res.next()) {
                final String name = res.getString("name");
                final int quantity = res.getInt("quantity");
                items.add(new Item(name, quantity));
            }

        } catch (SQLException e) {
            throw new MainSQLException(e);
        }
        return items;
    }

    @Override
    public void saveItems(List<Item> items) {
        try (final Connection con = getConnection(host, user, pass);
             final PreparedStatement insert = con.prepareStatement(INSERT_SQL)) {
            truncateItems();

            for (Item item : items) {
                insert.setString(1, item.getName());
                insert.setInt(2, item.getQuantity());
                insert.executeUpdate();
            }
        } catch (SQLException e) {
            throw new MainSQLException(e);
        }
    }

    private void truncateItems() {
        try (final Connection con = getConnection(host, user, pass);
             final PreparedStatement truncate = con.prepareStatement(TRUNCATE_SQL);) {
            truncate.executeUpdate();
        } catch (SQLException e) {
            throw new MainSQLException(e);
        }
    }

}
