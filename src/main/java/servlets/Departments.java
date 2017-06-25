package servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/departments")
public class Departments extends MainServlet{

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String sql = "select department.name as department, groups.name as groups, groups.id as groups_id\n" +
                "from department\n" +
                "left join groups on groups.department_id = department.id\n" +
                "where department.id = "+ request.getParameter("id");
        try {
            if (!isConnect) {
                doConnect();
            }
            ResultSet rs = statement.executeQuery(sql);
            ArrayList<String[]> list = new ArrayList<String[]>();
            while (rs.next()) {
                String s[] = new String[3];
                s[0] = rs.getString("department");
                s[1] = rs.getString("groups");
                s[2] = rs.getString("groups_id");
                list.add(s);
            }
            rs.close();
            doDisconnect();
            request.setAttribute("list", list);
            request.setAttribute("isRec", true);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/departments.jsp");
            dispatcher.forward(request, response);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String sql = "with \n" +
                " stud_cnt as (\n" +
                "   select dep.id as department_id, count(stud.id) stud_cnt\n" +
                "   from department dep\n" +
                "   join groups gr on gr.department_id = dep.id\n" +
                "   join student stud on stud.groups_id = gr.id\n" +
                "   group by dep.id\n" +
                "), \n" +
                " subj_cnt as (\n" +
                "   select dep.id as department_id, count(distinct se.subject_id) as subj_cnt\n" +
                "   from department dep\n" +
                "   join groups gr on gr.department_id = dep.id\n" +
                "   left join subject_el se on se.groups_id = gr.id\n" +
                "   group by dep.id\n" +
                ")\n" +
                "\n" +
                "select \n" +
                "  dep.id, dep.name, \n" +
                "  count(gr.id) as group_cnt,\n" +
                "  stud_cnt.stud_cnt,\n" +
                "  subj_cnt.subj_cnt\n" +
                "from department dep\n" +
                " left join groups gr on gr.department_id = dep.id\n" +
                " left join stud_cnt on stud_cnt.department_id = dep.id\n" +
                " left join subj_cnt on subj_cnt.department_id = dep.id\n" +
                "group by dep.id, dep.name, stud_cnt.stud_cnt, subj_cnt.subj_cnt";

        try {
            if (!isConnect) {
                doConnect();
            }
            ResultSet rs = statement.executeQuery(sql);
            ArrayList<String[]> list = new ArrayList<String[]>();
            while (rs.next()) {
                String s[] = new String[5];
                s[0] = rs.getString("id");
                s[1] = rs.getString("name");
                s[2] = rs.getString("group_cnt");
                s[3] = rs.getString("stud_cnt");
                s[4] = rs.getString("subj_cnt");
                if (s[2]==null) s[2]="0";
                if (s[3]==null) s[3]="0";
                if (s[4]==null) s[4]="0";
                list.add(s);
            }
            rs.close();
            doDisconnect();
            request.setAttribute("list", list);
            request.setAttribute("isRec", false);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/departments.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
