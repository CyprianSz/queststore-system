package pl.coderampart.controller.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import pl.coderampart.controller.helpers.AccessValidator;
import pl.coderampart.controller.helpers.HelperController;
import pl.coderampart.model.Mentor;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class DisplayMentorsController extends AccessValidator implements HttpHandler{

    private Connection connection;
    private HelperController helper;

    public DisplayMentorsController(Connection connection) {
        this.connection = connection;
        this.helper = new HelperController(connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        validateAccess( "Admin", httpExchange, connection);
        List<Mentor> allMentors = helper.readMentorsFromDB();
        String response = "";

        response += helper.renderHeader(httpExchange, connection);
        response += helper.render("admin/adminMenu");
        response += renderDisplayMentors(allMentors);
        response += helper.render("footer");

        helper.sendResponse( response, httpExchange );
    }

    private String renderDisplayMentors(List<Mentor> allMentors) {
        String templatePath = "templates/admin/displayMentors.twig";
        JtwigTemplate template = JtwigTemplate.classpathTemplate( templatePath );
        JtwigModel model = JtwigModel.newModel();

        model.with("allMentors", allMentors);

        return template.render(model);
    }
}
