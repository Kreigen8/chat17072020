package server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class SimpleAuthService implements AuthService {

    private class UserData {
        String login;
        String password;
        String nickname;

        public UserData(String login, String password, String nickname) {
            this.login = login;
            this.password = password;
            this.nickname = nickname;
        }
    }

    private List<UserData> users;

    public SimpleAuthService() throws SQLException {

        users = new ArrayList<>();

  //          Server.psInsert = Server.connection.prepareStatement("SELECT * FROM accounts WHERE id = " + i + ";");
        ResultSet rs = Server.stmt.executeQuery("SELECT * FROM accounts");
        while (rs.next()) {
            users.add(new UserData(rs.getString("login"), rs.getString("password"), rs.getString("nickname")));
        }
        rs.close();


//        for (int i = 1; i <= 10; i++) {
//            users.add(new UserData("l" + i, "p" + i, "nick" + i));
//        }
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        for (UserData user : users) {
            if (user.login.equals(login) && user.password.equals(password)) {
                return user.nickname;
            }
        }

        return null;
    }

    @Override
    public boolean registration(String login, String password, String nickname) throws SQLException {
        for (UserData user : users) {
            if (user.login.equals(login) || user.nickname.equals(nickname)) {
                return false;
            }
        }
        users.add(new UserData(login, password, nickname));

        PreparedStatement ps = Server.connection.prepareStatement("INSERT INTO accounts (login, password, nickname) VALUES (?, ?, ?);");
        ps.setString(1, login);
        ps.setString(2, password);
        ps.setString(3, nickname);
//        ResultSet resultSet = ps.executeQuery();
        Server.stmt.executeUpdate(String.valueOf(ps));
        //Server.stmt.executeUpdate("INSERT INTO accounts (login, password, nickname) VALUES (?, ?, ?);");

        return true;
    }
}
