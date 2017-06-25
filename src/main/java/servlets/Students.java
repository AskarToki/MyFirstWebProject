package servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/students")
public class Students extends MainServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String sql = "select \n" +
                "student.name, groups.name as groups, surname, patronymic, city, email, gender, \n" +
                "extract(year from sysdate)-extract(year from dt_birth) as age \n" +
                "from student \n" +
                "left join groups on groups.id = student.groups_id";

        try {
            if (!isConnect) {
                doConnect();
            }
            ResultSet rs = statement.executeQuery(sql);
            ArrayList<String[]> list = new ArrayList<String[]>();
            while (rs.next()) {
                String s[] = new String[8];

                s[0] = rs.getString("surname");
                s[1] = rs.getString("name");
                s[2] = rs.getString("patronymic");
                s[3] = rs.getString("groups");
                s[4] = rs.getString("age");
                s[5] = rs.getString("gender");
                s[6] = rs.getString("email");
                s[7] = rs.getString("city");

                if (s[0]==null) s[0]="";
                if (s[1]==null) s[1]="";
                if (s[2]==null) s[2]="";
                if (s[3]==null) s[3]="";
                if (s[4]==null) s[4]="";
                if (s[5]==null) s[5]="";
                if (s[6]==null) s[6]="";
                if (s[7]==null) s[7]="";

                list.add(s);
            }
            rs.close();
            doDisconnect();
            request.setAttribute("list", list);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/students.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
