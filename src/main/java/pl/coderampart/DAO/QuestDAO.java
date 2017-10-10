package pl.coderampart.DAO;

import pl.coderampart.model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class QuestDAO extends AbstractDAO {

    private Connection connection;

    public QuestDAO(Connection connectionToDB) {

        connection = connectionToDB;
    }

    public ArrayList<Quest> readAll() throws SQLException {
        ArrayList<Quest> questList = new ArrayList<>();

        String query = "SELECT * FROM quests;";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Quest quest = this.createQuestFromResultSet(resultSet);
            questList.add(quest);
        }

        return questList;
    }

    public void create(Quest quest) throws SQLException{

        String query = "INSERT INTO quests VALUES (?, ?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        PreparedStatement setStatement = setPreparedStatement(statement, quest);
        statement.executeUpdate();
    }

    public void update(Quest quest) throws SQLException {

        String query = "UPDATE quests SET id = ?, name = ?, description = ?, reward = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        PreparedStatement setStatement = setPreparedStatement(statement, quest);
        setStatement.executeUpdate();
    }

    public void delete(Quest quest) throws SQLException {

        String query = "DELETE FROM quests WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, quest.getID());
        statement.executeUpdate();

    }

    private PreparedStatement setPreparedStatement(PreparedStatement statement, Quest quest) throws SQLException {
        statement.setString(1, quest.getID());
        statement.setString(2, quest.getName());
        statement.setString(3, quest.getDescription());
        statement.setInt(4, quest.getReward());

        return statement;
    }

    private Quest createQuestFromResultSet(ResultSet resultSet) throws SQLException {
        String ID = resultSet.getString("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        Integer reward = resultSet.getInt("reward");


        return new Quest(ID, name, description, reward);
    }
}