package servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;


@WebServlet("/main")
public class MainServlet extends HttpServlet {

    private static final String DBURL = "jdbc:oracle:thin:@db-srv:1521:orcl";
    private static final String DBUSER = "XTBD_FINAL0117";
    private static final String DBPASS = "111";

    protected Connection con;
    protected Statement statement;

    protected boolean isConnect = false;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        super.doPost(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isConnect) {
            doConnect();
        }
        try {
            checkDbTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String varTextA = "Hello World!";
        request.setAttribute("textA", varTextA);
        String varTextB = "It JSP.";
        request.setAttribute("textB", varTextB);
        ArrayList<String> list = new ArrayList<String>();
        list.add("first");
        list.add("sectomn");
        list.add("three");
        doDisconnect();
        request.setAttribute("list", list);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
    }

    protected void doConnect() {
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            con = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
            statement = con.createStatement();
            isConnect = true;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    protected void doDisconnect() {
        try {
            statement.close();
            con.close();
            isConnect = false;
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    //метод проверяет, есть ли в базе нужные нам таблицы, если нету то добавляет их
    private void checkDbTables() throws SQLException {
        String sql = "select * from tab where tname = 'STUDENT'";
        ResultSet rs = statement.executeQuery(sql);
        try {
            if (!rs.next()) {
                sql = "create table Department (\n" +
                        "  id number(19) not null,\n" +
                        "  name nvarchar2(200)\n" +
                        ")";
                statement.executeQuery(sql);

                sql = "alter table Department add constraint pk_Department primary key (id)";
                statement.executeQuery(sql);

                sql = "create table Groups (\n" +
                        "  id number(19) not null,\n" +
                        "  name nvarchar2(200),\n" +
                        "  department_id number(19)\n" +
                        ")";
                statement.executeQuery(sql);

                sql = "alter table Groups add constraint pk_Groups primary key (id)";
                statement.executeQuery(sql);

                sql = "create table Student (\n" +
                        "  id number(19) not null,\n" +
                        "  groups_id number(19),\n" +
                        "  surname nvarchar2(200),\n" +
                        "  name nvarchar2(200),\n" +
                        "  patronymic nvarchar2(200),\n" +
                        "  dt_birth date,\n" +
                        "  gender nvarchar2(200),\n" +
                        "  email nvarchar2(200),\n" +
                        "  city nvarchar2(200)\n" +
                        ")";
                statement.executeQuery(sql);

                sql = "alter table Student add constraint pk_Student primary key (id)";
                statement.executeQuery(sql);

                sql = "create table Subject (\n" +
                        "  id number(19) not null,\n" +
                        "  name nvarchar2(200)\n" +
                        ")";
                statement.executeQuery(sql);

                sql = "alter table Subject add constraint pk_Subject primary key (id)";
                statement.executeQuery(sql);

                sql = "create table Subject_el (\n" +
                        "  id number(19) not null,\n" +
                        "  subject_id number(19),\n" +
                        "  groups_id number(19)\n" +
                        ")";
                statement.executeQuery(sql);

                sql = "alter table Subject_el add constraint pk_Subject_el primary key (id)";
                statement.executeQuery(sql);

                statement.executeQuery("create sequence seq_Department start with 1000");
                statement.executeQuery("create sequence seq_Groups start with 1000");
                statement.executeQuery("create sequence seq_Student start with 1000");
                statement.executeQuery("create sequence seq_Subject start with 1000");
                statement.executeQuery("create sequence seq_Subject_el start with 1000");

                statement.executeQuery("alter table Groups add constraint fk_Groups_department_id\n" +
                        "foreign key(department_id) references Department(id)");

                statement.executeQuery("alter table Student add constraint fk_Student_groups_id\n" +
                        "foreign key(groups_id) references Groups(id)");

                statement.executeQuery("alter table Subject_el add constraint fk_Subject_el_subject_id\n" +
                        "foreign key(subject_id) references Subject(id)");

                statement.executeQuery("alter table Subject_el add constraint fk_Subject_el_groups_id\n" +
                        "foreign key(groups_id) references Groups(id)");

                fillTestData();
            }
        } finally {
            rs.close();
        }
    }

    //заполняет тестовыми данными
    private void fillTestData() throws SQLException {
        statement.executeQuery("INSERT INTO DEPARTMENT (ID, NAME) VALUES " +
                "(100, 'Информатика')");
        statement.executeQuery("INSERT INTO DEPARTMENT (ID, NAME) VALUES " +
                "(101, 'Архитектурный')");

        statement.executeQuery("INSERT INTO GROUPS (ID, NAME, Department_id) VALUES " +
                "(100, 'инф-1', 100)");
        statement.executeQuery("INSERT INTO GROUPS (ID, NAME, Department_id) VALUES " +
                "(101, 'инф-2', 100)");
        statement.executeQuery("INSERT INTO GROUPS (ID, NAME, Department_id) VALUES " +
                "(102, 'инф-3', 100)");
        statement.executeQuery("INSERT INTO GROUPS (ID, NAME, Department_id) VALUES " +
                "(103, 'арх-1', 101)");
        statement.executeQuery("INSERT INTO GROUPS (ID, NAME, Department_id) VALUES " +
                "(104, 'арх-2', 101)");
        statement.executeQuery("INSERT INTO GROUPS (ID, NAME, Department_id) VALUES " +
                "(105, 'арх-3', 101)");

        statement.executeQuery("INSERT INTO Subject(ID, NAME) VALUES " +
                "(100, 'информатика')");
        statement.executeQuery("INSERT INTO Subject(ID, NAME) VALUES " +
                "(101, 'матанализ')");
        statement.executeQuery("INSERT INTO Subject(ID, NAME) VALUES " +
                "(102, 'черчение')");
        statement.executeQuery("INSERT INTO Subject(ID, NAME) VALUES " +
                "(103, 'история Казахстана')");
        statement.executeQuery("INSERT INTO Subject(ID, NAME) VALUES " +
                "(104, 'базы данных')");
        statement.executeQuery("INSERT INTO Subject(ID, NAME) VALUES " +
                "(105, 'психология')");

        statement.executeQuery("INSERT INTO Subject_el(ID, Subject_id, Groups_id) VALUES " +
                "(seq_Subject_el.nextval, 100, 100)");
        statement.executeQuery("INSERT INTO Subject_el(ID, Subject_id, Groups_id) VALUES " +
                "(seq_Subject_el.nextval, 100, 101)");
        statement.executeQuery("INSERT INTO Subject_el(ID, Subject_id, Groups_id) VALUES " +
                "(seq_Subject_el.nextval, 100, 102)");

        statement.executeQuery("INSERT INTO Subject_el(ID, Subject_id, Groups_id) VALUES " +
                "(seq_Subject_el.nextval, 101, 100)");
        statement.executeQuery("INSERT INTO Subject_el(ID, Subject_id, Groups_id) VALUES " +
                "(seq_Subject_el.nextval, 101, 101)");
        statement.executeQuery("INSERT INTO Subject_el(ID, Subject_id, Groups_id) VALUES " +
                "(seq_Subject_el.nextval, 101, 102)");
        statement.executeQuery("INSERT INTO Subject_el(ID, Subject_id, Groups_id) VALUES " +
                "(seq_Subject_el.nextval, 101, 103)");
        statement.executeQuery("INSERT INTO Subject_el(ID, Subject_id, Groups_id) VALUES " +
                "(seq_Subject_el.nextval, 101, 104)");
        statement.executeQuery("INSERT INTO Subject_el(ID, Subject_id, Groups_id) VALUES " +
                "(seq_Subject_el.nextval, 101, 105)");

        statement.executeQuery("INSERT INTO Subject_el(ID, Subject_id, Groups_id) VALUES " +
                "(seq_Subject_el.nextval, 102, 103)");
        statement.executeQuery("INSERT INTO Subject_el(ID, Subject_id, Groups_id) VALUES " +
                "(seq_Subject_el.nextval, 102, 104)");
        statement.executeQuery("INSERT INTO Subject_el(ID, Subject_id, Groups_id) VALUES " +
                "(seq_Subject_el.nextval, 102, 105)");

        statement.executeQuery("INSERT INTO Subject_el(ID, Subject_id, Groups_id) VALUES " +
                "(seq_Subject_el.nextval, 103, 100)");
        statement.executeQuery("INSERT INTO Subject_el(ID, Subject_id, Groups_id) VALUES " +
                "(seq_Subject_el.nextval, 103, 101)");
        statement.executeQuery("INSERT INTO Subject_el(ID, Subject_id, Groups_id) VALUES " +
                "(seq_Subject_el.nextval, 103, 102)");
        statement.executeQuery("INSERT INTO Subject_el(ID, Subject_id, Groups_id) VALUES " +
                "(seq_Subject_el.nextval, 103, 103)");
        statement.executeQuery("INSERT INTO Subject_el(ID, Subject_id, Groups_id) VALUES " +
                "(seq_Subject_el.nextval, 103, 104)");
        statement.executeQuery("INSERT INTO Subject_el(ID, Subject_id, Groups_id) VALUES " +
                "(seq_Subject_el.nextval, 103, 105)");

        statement.executeQuery("INSERT INTO Subject_el(ID, Subject_id, Groups_id) VALUES " +
                "(seq_Subject_el.nextval, 104, 100)");
        statement.executeQuery("INSERT INTO Subject_el(ID, Subject_id, Groups_id) VALUES " +
                "(seq_Subject_el.nextval, 104, 101)");
        statement.executeQuery("INSERT INTO Subject_el(ID, Subject_id, Groups_id) VALUES " +
                "(seq_Subject_el.nextval, 104, 102)");

        statement.executeQuery("INSERT INTO Subject_el(ID, Subject_id, Groups_id) VALUES " +
                "(seq_Subject_el.nextval, 105, 100)");
        statement.executeQuery("INSERT INTO Subject_el(ID, Subject_id, Groups_id) VALUES " +
                "(seq_Subject_el.nextval, 105, 101)");
        statement.executeQuery("INSERT INTO Subject_el(ID, Subject_id, Groups_id) VALUES " +
                "(seq_Subject_el.nextval, 105, 102)");
        statement.executeQuery("INSERT INTO Subject_el(ID, Subject_id, Groups_id) VALUES " +
                "(seq_Subject_el.nextval, 105, 103)");
        statement.executeQuery("INSERT INTO Subject_el(ID, Subject_id, Groups_id) VALUES " +
                "(seq_Subject_el.nextval, 105, 104)");
        statement.executeQuery("INSERT INTO Subject_el(ID, Subject_id, Groups_id) VALUES " +
                "(seq_Subject_el.nextval, 105, 105)");


        statement.executeQuery("INSERT INTO Student(ID, groups_id, surname, name, patronymic, dt_birth, gender, email, city) VALUES " +
                "(seq_Student.nextval, 100, 'Ivanov', 'Petr','Ivanovich', to_date('04.03.1992','dd.mm.yyyy'), 'male', 'ivanov@mail.ru', 'almaty')");
        statement.executeQuery("INSERT INTO Student(ID, groups_id, surname, name, patronymic, dt_birth, gender, email, city) VALUES " +
                "(seq_Student.nextval, 100, 'Petrov', 'Nikolay','', to_date('05.05.1991','dd.mm.yyyy'), 'male', 'petr@mail.ru', 'almaty')");
        statement.executeQuery("INSERT INTO Student(ID, groups_id, surname, name, patronymic, dt_birth, gender, email, city) VALUES " +
                "(seq_Student.nextval, 100, 'Sidorov', 'Maksim','Ivanovich', to_date('04.04.1992','dd.mm.yyyy'), 'male', 'sidorov@mail.ru', 'astana')");
        statement.executeQuery("INSERT INTO Student(ID, groups_id, surname, name, patronymic, dt_birth, gender, email, city) VALUES " +
                "(seq_Student.nextval, 100, 'Kisheva', 'Anna','', to_date('01.02.1991','dd.mm.yyyy'), 'female', 'anna@mail.ru', 'almaty')");
        statement.executeQuery("INSERT INTO Student(ID, groups_id, surname, name, patronymic, dt_birth, gender, email, city) VALUES " +
                "(seq_Student.nextval, 100, 'Lobanov', 'Fedor','Petrovich', to_date('28.06.1992','dd.mm.yyyy'), 'male', 'fedor@mail.ru', 'astana')");

        statement.executeQuery("INSERT INTO Student(ID, groups_id, surname, name, patronymic, dt_birth, gender, email, city) VALUES " +
                "(seq_Student.nextval, 104, 'Ivanov', 'Maksim','', to_date('04.06.1990','dd.mm.yyyy'), 'male', 'ivanov_m@mail.ru', 'moscow')");
        statement.executeQuery("INSERT INTO Student(ID, groups_id, surname, name, patronymic, dt_birth, gender, email, city) VALUES " +
                "(seq_Student.nextval, 104, 'Babenko', 'Nikolay','', to_date('01.01.1991','dd.mm.yyyy'), 'male', 'petr@mail.ru', 'karaganda')");
        statement.executeQuery("INSERT INTO Student(ID, groups_id, surname, name, patronymic, dt_birth, gender, email, city) VALUES " +
                "(seq_Student.nextval, 104, 'Ostapenko', 'Nikolay','Ivanovich', to_date('04.04.1993','dd.mm.yyyy'), 'male', 'sidr@mail.ru', 'karaganda')");
        statement.executeQuery("INSERT INTO Student(ID, groups_id, surname, name, patronymic, dt_birth, gender, email, city) VALUES " +
                "(seq_Student.nextval, 104, 'Kareva', 'Tanya','', to_date('23.11.1991','dd.mm.yyyy'), 'female', 'tanya@mail.ru', 'astana')");
        statement.executeQuery("INSERT INTO Student(ID, groups_id, surname, name, patronymic, dt_birth, gender, email, city) VALUES " +
                "(seq_Student.nextval, 104, 'Utkina', 'Katya','Nikolaevna', to_date('22.05.1991','dd.mm.yyyy'), 'female', 'katya@mail.ru', 'astana')");

    }

}

