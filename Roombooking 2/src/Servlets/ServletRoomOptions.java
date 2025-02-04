package Servlets;

import Classes.Rooms.AbstractRoom;
import Classes.Rooms.Grouproom;
import Tools.DbFunctionality;
import Tools.DbTool;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Author Hanne, Henriette, Hedda, Trym, Brisdalen
 */

@WebServlet(name = "Servlets.ServletRoomOptions", urlPatterns = {"/Servlets.ServletRoomOptions"})
public class ServletRoomOptions extends AbstractPostServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            //prints the beginning of a  html-page.
            printNav(out);
            //retrieves the value of button Add Room
            String action = request.getParameter("action").toLowerCase();

            if(action.contains("add")) {
                //retrieves the data in the text-box Add RoomID
                int roomID = Integer.parseInt(request.getParameter("Add_roomID"));
                //retrieves the Room name from the text-box Room Name
                String roomName = request.getParameter("Add_roomName");
                //retrieves the Room building from the text-box Room Building
                String roomBuilding = request.getParameter("Add_roomBuilding");
                //retrieves the room capacity from the text-box Room Capacity
                int maxCapacity = Integer.parseInt(request.getParameter("maxCapacity"));

                DbTool dbTool = new DbTool();
                //establishes connection to database
                Connection connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();
                //
                AbstractRoom room = new Grouproom(roomID, roomName, roomBuilding, maxCapacity);
                // TODO: Bruker kun grupperom typen for nå
                //Adds the room to the database
                dbFunctionality.addRoom(room, connection);
                //TODO: legg til annen knapp for å forbli logget inn.
                //prints return button.
                addHomeButton(out);

            // TODO: Lag HTML side med action som fjerner et rom
            } else if(action.contains("delete")) {
                //retrieves the
                int roomID = Integer.parseInt(request.getParameter("Delete_roomID"));
                DbTool dbTool = new DbTool();
                Connection connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();

                dbFunctionality.deleteRoom(roomID, connection);

                addHomeButton(out);

            } else if(action.contains("show")) {
                DbTool dbTool = new DbTool();
                Connection connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();
                dbFunctionality.printRooms(out, connection);


            } else if (action.contains("gotoprofile")) {
                ServletContext servletContext = getServletContext();
                servletContext.getRequestDispatcher("/profile.html").forward(request,response);

            } else if(action.contains("reserve")) {
                DbTool dbTool = new DbTool();
                Connection connection = dbTool.dbLogIn(out);
                //todo add reservation.. and order generation
            }

            scriptBootstrap(out);
            out.print("</body>");
            out.print("</html>");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
