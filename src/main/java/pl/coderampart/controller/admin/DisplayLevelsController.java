package pl.coderampart.controller.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.DAO.LevelDAO;
import pl.coderampart.model.Level;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DisplayLevelsController implements HttpHandler{

    private Connection connection;
    private LevelDAO levelDAO;

    public DisplayLevelsController(Connection connection) {
        this.connection = connection;
        this.levelDAO = new LevelDAO(this.connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        List<Level> allLevels = readLevelsFromDB();
        String method = httpExchange.getRequestMethod();
        String response = "";
        response += render("header");
        response += render("admin/adminMenu");
        response += renderDisplayLevels(allLevels);
        response += render("footer");
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String render(String fileName){
        String templatePath = "templates/" + fileName + ".twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        return template.render(model);
    }

    private List<Level> readLevelsFromDB(){
        List<Level> allLevels = null;

        try{
            allLevels = levelDAO.readAll();
        } catch (SQLException e){
            e.printStackTrace();
        }

        return allLevels;
    }
}
