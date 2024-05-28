package dbProject.UI;

import java.sql.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserLogin extends JFrame {
    private static final String URL = "jdbc:mysql://localhost:3306/db1";
    private static final String userID = "user1";
    private static final String userPW = "user1";

    private JTextField userIdField = null;
    private JPasswordField userPasswordField = null;

    public UserLogin() {
        if (MainInterface.isUserLoggedIn()) {
            userWindows();
        } else {
            setTitle("유저 로그인");
            setSize(300, 200);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new FlowLayout());

            userIdField = new JTextField(15);
            add(new JLabel("유저 ID : "));
            add(userIdField);

            userPasswordField = new JPasswordField(15);
            add(new JLabel("유저 PW : "));
            add(userPasswordField);

            JButton loginButton = new JButton("로그인");
            add(loginButton);

            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String enteredID = userIdField.getText();
                    String enteredPW = new String(userPasswordField.getPassword());

                    if (userID.equals(enteredID) && userPW.equals(enteredPW)) {
                        JOptionPane.showMessageDialog(null, "로그인 성공");
                        userWindows();
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "로그인 실패");
                    }
                }
            });
        }
    }

    private static void userWindows() {
        JFrame frame = new JFrame("사용자 메뉴");
        frame.setSize(300, 150);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(0, 1, 10, 10));

        JButton movieSearchButton = new JButton("영화 조회");
        frame.add(movieSearchButton);

        JButton myInfoButton = new JButton("내 정보");
        frame.add(myInfoButton);

        movieSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchMovie();
            }
        });

        myInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 내 정보 화면을 실행하는 코드를 추가
            }
        });

        frame.setVisible(true);
    }

    private static void searchMovie() {
        JFrame searchFrame = new JFrame("영화 조회");
        searchFrame.setSize(600, 400);
        searchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        searchFrame.setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new GridLayout(0, 2, 10, 10));

        JTextField movieNameField = new JTextField(15);
        searchPanel.add(new JLabel("영화명: "));
        searchPanel.add(movieNameField);

        JTextField directorNameField = new JTextField(15);
        searchPanel.add(new JLabel("감독명: "));
        searchPanel.add(directorNameField);

        JTextField actorNameField = new JTextField(15);
        searchPanel.add(new JLabel("배우명: "));
        searchPanel.add(actorNameField);

        JTextField genreField = new JTextField(15);
        searchPanel.add(new JLabel("장르: "));
        searchPanel.add(genreField);

        JButton searchButton = new JButton("조회");
        JButton cancelButton = new JButton("취소");
        searchPanel.add(new JLabel());
        searchPanel.add(searchButton);
        searchPanel.add(new JLabel());
        searchPanel.add(cancelButton);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String movieName = movieNameField.getText().trim();
                String directorName = directorNameField.getText().trim();
                String actorName = actorNameField.getText().trim();
                String genre = genreField.getText().trim();

                StringBuilder queryBuilder = new StringBuilder("SELECT * FROM Movies");

                // 조건 추가
                if (!movieName.isEmpty() || !directorName.isEmpty() || !actorName.isEmpty() || !genre.isEmpty()) {
                    queryBuilder.append(" WHERE ");
                    boolean addedCondition = false;
                    if (!movieName.isEmpty()) {
                        queryBuilder.append("MovieTitle = '").append(movieName).append("'");
                        addedCondition = true;
                    }
                    if (!directorName.isEmpty()) {
                        if (addedCondition) {
                            queryBuilder.append(" AND ");
                        }
                        queryBuilder.append("Director = '").append(directorName).append("'");
                        addedCondition = true;
                    }
                    if (!actorName.isEmpty()) {
                        if (addedCondition) {
                            queryBuilder.append(" AND ");
                        }
                        queryBuilder.append("Actors = '").append(actorName).append("'");
                        addedCondition = true;
                    }
                    if (!genre.isEmpty()) {
                        if (addedCondition) {
                            queryBuilder.append(" AND ");
                        }
                        queryBuilder.append("Genre = '").append(genre).append("'");
                    }
                }

                try (Connection conn = DriverManager.getConnection(URL, userID, userPW)) {
                    String query = queryBuilder.toString();
                    PreparedStatement statement = conn.prepareStatement(query);
                    ResultSet resultSet = statement.executeQuery();

                    JTable table = new JTable(buildTableModel(resultSet));
                    JScrollPane scrollPane = new JScrollPane(table);
                    searchFrame.getContentPane().removeAll(); // 이전에 추가된 컴포넌트 제거
                    searchFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);
                    searchFrame.revalidate();
                    searchFrame.repaint();

                    JOptionPane.showMessageDialog(null, "해당 조건을 만족하는 영화를 조회합니다.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "영화 조회 중 오류가 발생했습니다.");
                }
            }
        });




        searchFrame.add(searchPanel, BorderLayout.CENTER);
        searchFrame.setVisible(true);
    }


    // ResultSet을 DefaultTableModel로 변환
    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        // 컬럼명 가져오기
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnName(i));
        }
        // 데이터 가져오기
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                vector.add(rs.getObject(i));
            }
            data.add(vector);
        }
        // 테이블 모델 생성
        return new DefaultTableModel(data, columnNames);
    }
}
