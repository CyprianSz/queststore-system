package pl.coderampart.DAO;

import pl.coderampart.model.Group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GroupDAO extends AbstractDAO {

    private Connection connection;

    public GroupDAO(Connection connectionToDB) {

        connection = connectionToDB;

    }

    public ArrayList<Group> readAll() throws SQLException{
        ArrayList<Group> groupList = new ArrayList<>();

        String query = "SELECT * FROM groups;";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Group group = this.createGroupFromResultSet(resultSet);
            groupList.add(group);
        }

        return groupList;
    }

    public Group getByID(String ID) throws SQLException{
        Group group = null;

        String query = "SELECT * FROM groups WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, ID);
        ResultSet resultSet = statement.executeQuery();

        group = this.createGroupFromResultSet(resultSet);

        return group;
    }

    public void create(Group group) throws SQLException{


        String query = "INSERT INTO groups VALUES (?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        PreparedStatement setStatement = setPreparedStatement(statement, group);
        statement.executeUpdate();

    }

    public void update(Group group) throws SQLException {

        String query = "UPDATE groups SET id = ?, name = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        PreparedStatement setStatement = setPreparedStatement(statement, group);
        setStatement.executeUpdate();


    }

    public void delete(Group group) throws SQLException{

        String query =  "DELETE FROM groups WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, group.getID());
        statement.executeUpdate();

    }

    private PreparedStatement setPreparedStatement(PreparedStatement statement, Group group) throws SQLException {
        statement.setString(1, group.getID());
        statement.setString(2, group.getName());

        return statement;
    }

    private Group createGroupFromResultSet(ResultSet resultSet) throws SQLException {
        String ID = resultSet.getString("id");
        String name = resultSet.getString("name");

        return new Group(ID, name);
    }
}