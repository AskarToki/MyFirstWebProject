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

@WebServlet("/groups")
public class Groups extends MainServlet{

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String sql = "select name, surname, patronymic, city, email, gender, \n" +
                "extract(year from sysdate)-extract(year from dt_birth) as age from student where groups_id = "+ request.getParameter("id");
        try {
            if (!isConnect) {
                doConnect();
            }
            ResultSet rs = statement.executeQuery(sql);
            ArrayList<String[]> list = new ArrayList<String[]>();
            while (rs.next()) {
                String s[] = new String[7];
                s[0] = rs.getString("surname");
                s[1] = rs.getString("name");
                s[2] = rs.getString("patronymic");
                s[3] = rs.getString("age");
                s[4] = rs.getString("gender");
                s[5] = rs.getString("email");
                s[6] = rs.getString("city");

                if (s[0]==null) s[0]="";
                if (s[1]==null) s[1]="";
                if (s[2]==null) s[2]="";
                if (s[3]==null) s[3]="";
                if (s[4]==null) s[4]="";
                if (s[5]==null) s[5]="";
                if (s[6]==null) s[6]="";

                list.add(s);
            }
            rs.close();
            doDisconnect();
            request.setAttribute("list", list);
            request.setAttribute("isRec", true);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/groups.jsp");
            dispatcher.forward(request, response);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String sql = "with stud_cnt as (\n" +
                "select groups.id as groups_id, count(student.id) as stud_cnt \n" +
                "from groups\n" +
                "left join student on student.groups_id = groups.id\n" +
                "group by groups.id\n" +
                ")\n" +
                "select groups.id, groups.name, stud_cnt.stud_cnt, count(distinct subject_el.id) as subj_cnt\n" +
                "from groups\n" +
                "left join stud_cnt on stud_cnt.groups_id = groups.id\n" +
                "left join subject_el on subject_el.groups_id = groups.id\n" +
                "group by groups.id, groups.name, stud_cnt.stud_cnt\n" +
                "order by groups.name";

        try {
            if (!isConnect) {
                doConnect();
            }
            ResultSet rs = statement.executeQuery(sql);
            ArrayList<String[]> list = new ArrayList<String[]>();
            while (rs.next()) {
                String s[] = new String[4];
                s[0] = rs.getString("id");
                s[1] = rs.getString("name");
                s[2] = rs.getString("stud_cnt");
                s[3] = rs.getString("subj_cnt");
                if (s[2]==null) s[3]="0";
                if (s[3]==null) s[4]="0";
                list.add(s);
            }
            rs.close();
            doDisconnect();
            request.setAttribute("list", list);
            request.setAttribute("isRec", false);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/groups.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
