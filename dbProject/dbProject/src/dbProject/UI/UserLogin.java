package dbProject.UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

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
                showMovieInfo();
            }
        });

        myInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	showUserInfo();
                // 내 정보 화면을 실행하는 코드를 추가
            }
        });

        frame.setVisible(true);
    }

    
    private static void showUserInfo() {
        JFrame userInfoFrame = new JFrame("내 정보");
        userInfoFrame.setSize(600, 400);
        userInfoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        userInfoFrame.setLayout(new BorderLayout());

        JTable userInfoTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(userInfoTable);
        userInfoFrame.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton cancelButton = new JButton("예매 취소");
        JButton viewButton = new JButton("조회");
        JButton changeMovieButton = new JButton("영화 변경");
        JButton changeScheduleButton = new JButton("일정 변경");
        buttonPanel.add(cancelButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(changeMovieButton);
        buttonPanel.add(changeScheduleButton);
        userInfoFrame.add(buttonPanel, BorderLayout.SOUTH);

        try (Connection conn = DriverManager.getConnection(URL, userID, userPW)) {
            String customerID = userID;

            // 사용자의 예매 정보를 조회하는 쿼리
            String query = "SELECT t.TicketID, m.MovieTitle, s.ShowDate, s.StartTime, sc.ScreenID, st.SeatRow, st.SeatColumn, t.SellingPrice " +
                           "FROM Tickets t " +
                           "JOIN ScreeningInfo s ON t.ScreeningID = s.ScreeningID " +
                           "JOIN Movies m ON s.MovieID = m.MovieID " +
                           "JOIN Screen sc ON s.ScreenID = sc.ScreenID " +
                           "JOIN Seats st ON t.SeatID = st.SeatID " +
                           "JOIN BookInfo b ON t.BookingNum = b.BookingNum " +
                           "WHERE b.CustomerID = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, customerID);
            ResultSet resultSet = statement.executeQuery();

            userInfoTable.setModel(buildTableModel(resultSet));
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "개인정보 조회 중 오류가 발생했습니다.");
        }

        // 예매 취소 버튼 동작 구현
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userInfoTable.getSelectedRow();
                if (selectedRow != -1) {
                    int ticketID = (int) userInfoTable.getValueAt(selectedRow, 0);
                    cancelBooking(ticketID);
                    ((DefaultTableModel) userInfoTable.getModel()).removeRow(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(null, "취소할 예매 정보를 선택해주세요.");
                }
            }
        });

        // 조회 버튼 동작 구현
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userInfoTable.getSelectedRow();
                if (selectedRow != -1) {
                    String movieTitle = (String) userInfoTable.getValueAt(selectedRow, 1);
                    showAllScreenings(movieTitle);
                } else {
                    JOptionPane.showMessageDialog(null, "조회할 예매 정보를 선택해주세요.");
                }
            }
        });

        // 영화 변경 버튼 동작 구현
        changeMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userInfoTable.getSelectedRow();
                if (selectedRow != -1) {
                    int ticketID = (int) userInfoTable.getValueAt(selectedRow, 0);
                    changeBooking(ticketID);
                } else {
                    JOptionPane.showMessageDialog(null, "변경할 예매 정보를 선택해주세요.");
                }
            }
        });

        // 일정 변경 버튼 동작 구현
        changeScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userInfoTable.getSelectedRow();
                if (selectedRow != -1) {
                    int ticketID = (int) userInfoTable.getValueAt(selectedRow, 0);
                    changeScreeningSchedule(ticketID);
                } else {
                    JOptionPane.showMessageDialog(null, "변경할 예매 정보를 선택해주세요.");
                }
            }
        });

        userInfoFrame.setVisible(true);
    }


    private static void showMovieInfo() {
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

        searchFrame.add(searchPanel, BorderLayout.NORTH);

        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        searchFrame.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton screeningInfoButton = new JButton("상영 정보");
        buttonPanel.add(screeningInfoButton);
        searchFrame.add(buttonPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String movieName = movieNameField.getText().trim();
                String directorName = directorNameField.getText().trim();
                String actorName = actorNameField.getText().trim();
                String genre = genreField.getText().trim();

                StringBuilder queryBuilder = new StringBuilder("SELECT * FROM Movies");

                if (!movieName.isEmpty() || !directorName.isEmpty() || !actorName.isEmpty() || !genre.isEmpty()) {
                    queryBuilder.append(" WHERE ");
                    boolean addedCondition = false;
                    if (!movieName.isEmpty()) {
                        queryBuilder.append("MovieTitle LIKE '%").append(movieName).append("%'");
                        addedCondition = true;
                    }
                    if (!directorName.isEmpty()) {
                        if (addedCondition) {
                            queryBuilder.append(" AND ");
                        }
                        queryBuilder.append("Director LIKE '%").append(directorName).append("%'");
                        addedCondition = true;
                    }
                    if (!actorName.isEmpty()) {
                        if (addedCondition) {
                            queryBuilder.append(" AND ");
                        }
                        queryBuilder.append("Actors LIKE '%").append(actorName).append("%'");
                        addedCondition = true;
                    }
                    if (!genre.isEmpty()) {
                        if (addedCondition) {
                            queryBuilder.append(" AND ");
                        }
                        queryBuilder.append("Genre LIKE '%").append(genre).append("%'");
                    }
                }

                try (Connection conn = DriverManager.getConnection(URL, userID, userPW)) {
                    String query = queryBuilder.toString();
                    PreparedStatement statement = conn.prepareStatement(query);
                    ResultSet resultSet = statement.executeQuery();

                    table.setModel(buildTableModel(resultSet));
                    JOptionPane.showMessageDialog(null, "해당 조건을 만족하는 영화를 조회합니다.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "영화 조회 중 오류가 발생했습니다.");
                }
            }
        });

        // 테이블 행 선택 이벤트 리스너 추가
        table.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int selectedRow = table.getSelectedRow(); // 선택된 행의 인덱스를 가져옴

                // 기존의 모든 ActionListener 제거
                for (ActionListener al : screeningInfoButton.getActionListeners()) {
                    screeningInfoButton.removeActionListener(al);
                }

                screeningInfoButton.setEnabled(true); // 상영 정보 버튼 활성화
                screeningInfoButton.addActionListener(e -> {
                    String movieTitle = table.getValueAt(selectedRow, 1).toString(); // 선택된 행의 영화 제목을 가져옴
                    showScreeningInfo(movieTitle); // 상영 정보 창 표시 메소드 호출
                });
            }
        });

        screeningInfoButton.setEnabled(false); // 초기 상태에서는 상영 정보 버튼 비활성화

        searchFrame.setVisible(true);
    }

    private static void showScreeningInfo(String movieTitle) {
        JFrame screeningFrame = new JFrame("상영 정보");
        screeningFrame.setSize(600, 400);  // 크기를 조금 더 키움
        screeningFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        screeningFrame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("영화 제목: " + movieTitle);
        screeningFrame.add(titleLabel, BorderLayout.NORTH);

        JTable screeningTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(screeningTable);
        screeningFrame.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton bookButton = new JButton("예매");
        buttonPanel.add(bookButton);
        screeningFrame.add(buttonPanel, BorderLayout.SOUTH);

        try (Connection conn = DriverManager.getConnection(URL, userID, userPW)) {
            String findMovieIDQuery = "SELECT MovieID FROM Movies WHERE MovieTitle = ?";
            PreparedStatement findMovieIDStmt = conn.prepareStatement(findMovieIDQuery);
            findMovieIDStmt.setString(1, movieTitle);
            ResultSet movieIDResultSet = findMovieIDStmt.executeQuery();

            if (movieIDResultSet.next()) {
                int movieID = movieIDResultSet.getInt("MovieID");

                String screeningQuery = "SELECT si.ScreeningID, si.MovieID, m.MovieTitle, si.ScreenID, si.ShowDate, si.StartTime " +
                                        "FROM ScreeningInfo si " +
                                        "JOIN Movies m ON si.MovieID = m.MovieID " +
                                        "WHERE si.MovieID = ?";
                PreparedStatement screeningStmt = conn.prepareStatement(screeningQuery);
                screeningStmt.setInt(1, movieID);
                ResultSet screeningResultSet = screeningStmt.executeQuery();

                screeningTable.setModel(buildTableModel(screeningResultSet));

                screeningTable.getSelectionModel().addListSelectionListener(event -> {
                    if (!event.getValueIsAdjusting() && screeningTable.getSelectedRow() != -1) {
                        int selectedRow = screeningTable.getSelectedRow(); // 선택된 행의 인덱스를 가져옴

                        // 기존의 모든 ActionListener 제거
                        for (ActionListener al : bookButton.getActionListeners()) {
                            bookButton.removeActionListener(al);
                        }

                        bookButton.setEnabled(true); // 예매 버튼 활성화
                        bookButton.addActionListener(e -> {
                            int screeningID = (int) screeningTable.getValueAt(selectedRow, 0); // 선택된 행의 ScreeningID를 가져옴
                            int screenID = (int) screeningTable.getValueAt(selectedRow, 3); // 선택된 행의 ScreenID를 가져옴
                            String showDate = screeningTable.getValueAt(selectedRow, 4).toString(); // 선택된 행의 상영일을 가져옴
                            String startTime = screeningTable.getValueAt(selectedRow, 5).toString(); // 선택된 행의 상영 시작 시간을 가져옴
                            showSeatSelection(screeningID, screenID, movieTitle, showDate, startTime); // 좌석 선택 창 표시 메소드 호출
                        });
                    }
                });

            } else {
                JOptionPane.showMessageDialog(null, "해당 영화의 정보를 찾을 수 없습니다.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "상영 정보 조회 중 오류가 발생했습니다.");
        }

        screeningFrame.setVisible(true);
    }

    private static void showSeatSelection(int screeningID, int screenID, String movieTitle, String showDate, String startTime) {
        JFrame seatFrame = new JFrame("좌석 선택");
        seatFrame.setSize(600, 400);
        seatFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        try (Connection conn = DriverManager.getConnection(URL, userID, userPW)) {
            // 상영관의 가로 및 세로 좌석 수를 가져오기 위한 쿼리
            String screenSizeQuery = "SELECT ScreenRows, ScreenColumns FROM Screen WHERE ScreenID = ?";
            PreparedStatement screenSizeStmt = conn.prepareStatement(screenSizeQuery);
            screenSizeStmt.setInt(1, screenID);
            ResultSet screenSizeResultSet = screenSizeStmt.executeQuery();

            int rows = 0;
            int cols = 0;
            if (screenSizeResultSet.next()) {
                rows = screenSizeResultSet.getInt("ScreenRows");
                cols = screenSizeResultSet.getInt("ScreenColumns");
            }

            // 좌석 정보를 가져오기 위한 쿼리
            String seatQuery = "SELECT SeatID, SeatRow, SeatColumn FROM Seats WHERE ScreenID = ?";
            PreparedStatement seatStmt = conn.prepareStatement(seatQuery);
            seatStmt.setInt(1, screenID);
            ResultSet seatResultSet = seatStmt.executeQuery();

            JPanel seatPanel = new JPanel(new GridLayout(rows, cols)); // 가로 x 세로 좌석 배치
            while (seatResultSet.next()) {
                int seatID = seatResultSet.getInt("SeatID");
                int seatRow = seatResultSet.getInt("SeatRow");
                int seatColumn = seatResultSet.getInt("SeatColumn");

                JButton seatButton = new JButton(seatRow + "-" + seatColumn);

                // 좌석의 예매 상태를 확인하기 위한 쿼리
                String ticketQuery = "SELECT * FROM Tickets WHERE ScreeningID = ? AND SeatID = ?";
                PreparedStatement ticketStmt = conn.prepareStatement(ticketQuery);
                ticketStmt.setInt(1, screeningID);
                ticketStmt.setInt(2, seatID);
                ResultSet ticketResultSet = ticketStmt.executeQuery();

                if (ticketResultSet.next()) {
                    seatButton.setBackground(Color.RED); // 예매된 좌석은 빨간색으로 표시
                    seatButton.setEnabled(false); // 예매된 좌석은 클릭 불가
                } else {
                    seatButton.setBackground(Color.GREEN); // 사용 가능한 좌석은 녹색으로 표시
                    seatButton.setEnabled(true); // 사용 가능한 좌석은 클릭 가능

                    // 좌석 선택 시 결제 창으로 이동하는 기능 추가
                    seatButton.addActionListener(e -> {
                    	seatFrame.dispose();
                        showPaymentWindow(screeningID, seatID, movieTitle, showDate, startTime, screenID);
                    });
                }

                seatPanel.add(seatButton);
            }

            seatFrame.add(new JScrollPane(seatPanel));
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "좌석 조회 중 오류가 발생했습니다.");
        }

        seatFrame.setVisible(true);
    }

    private static void showPaymentWindow(int screeningID, int seatID, String movieTitle, String showDate, String startTime, int screenID) {
        JFrame paymentFrame = new JFrame("결제 정보");
        paymentFrame.setSize(400, 300);
        paymentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        paymentFrame.setLayout(new GridLayout(0, 2, 10, 10));

        // 가격 정보
        JLabel priceLabel = new JLabel("가격:");
        JLabel priceValueLabel = new JLabel("15000원");

        // 할인쿠폰 사용 버튼
        JButton couponButton = new JButton("할인쿠폰 사용");
        JLabel discountedPriceLabel = new JLabel("할인된 가격:");
        JLabel discountedPriceValueLabel = new JLabel("15000원");

        // 결제 방법 선택
        JLabel paymentMethodLabel = new JLabel("결제 방법:");
        String[] paymentMethods = {"현금", "카드"};
        JComboBox<String> paymentMethodComboBox = new JComboBox<>(paymentMethods);

        JButton confirmButton = new JButton("확인");
        JButton cancelButton = new JButton("취소");

        paymentFrame.add(priceLabel);
        paymentFrame.add(priceValueLabel);
        paymentFrame.add(couponButton);
        paymentFrame.add(new JLabel()); // Placeholder
        paymentFrame.add(discountedPriceLabel);
        paymentFrame.add(discountedPriceValueLabel);
        paymentFrame.add(paymentMethodLabel);
        paymentFrame.add(paymentMethodComboBox);
        paymentFrame.add(new JLabel()); // Placeholder
        paymentFrame.add(confirmButton);
        paymentFrame.add(new JLabel()); // Placeholder
        paymentFrame.add(cancelButton);

        // 할인쿠폰 사용 버튼 클릭 시 할인 적용
        couponButton.addActionListener(e -> {
            discountedPriceValueLabel.setText("13500원");
        });

        confirmButton.addActionListener(e -> {
            String paymentMethod = (String) paymentMethodComboBox.getSelectedItem();
            int finalPrice = discountedPriceValueLabel.getText().equals("13500원") ? 13500 : 15000;

            try (Connection conn = DriverManager.getConnection(URL, userID, userPW)) {
                // 예매 정보를 삽입하는 쿼리
                String insertBookingQuery = "INSERT INTO BookInfo (PaymentMethod, PaymentStatus, CustomerID, PaymentDate) VALUES (?, ?, ?, NOW())";
                PreparedStatement bookingStmt = conn.prepareStatement(insertBookingQuery, Statement.RETURN_GENERATED_KEYS);
                bookingStmt.setString(1, paymentMethod);
                bookingStmt.setString(2, "Completed");
                bookingStmt.setString(3, userID); // 현재 로그인한 고객 ID로 대체
                bookingStmt.executeUpdate();

                ResultSet generatedKeys = bookingStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int bookingNum = generatedKeys.getInt(1);

                    // 티켓 정보를 삽입하는 쿼리
                    String insertTicketQuery = "INSERT INTO Tickets (ScreeningID, ScreenID, SeatID, BookingNum, TicketInsurance, StandardPrice, SellingPrice) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement ticketStmt = conn.prepareStatement(insertTicketQuery);
                    ticketStmt.setInt(1, screeningID);
                    ticketStmt.setInt(2, screenID);
                    ticketStmt.setInt(3, seatID);
                    ticketStmt.setInt(4, bookingNum);
                    ticketStmt.setBoolean(5, true); // 티켓 발권 여부를 true로 설정
                    ticketStmt.setInt(6, 15000); // 표준 가격은 15000원 고정
                    ticketStmt.setInt(7, finalPrice); // 판매 가격은 할인 적용된 가격
                    ticketStmt.executeUpdate();
                }

                // 티켓 출력 정보 메시지
                JOptionPane.showMessageDialog(null, "영화 제목: " + movieTitle + "\n상영일: " + showDate + "\n상영관: " + screenID + "\n상영 시간: " + startTime + "\n\n티켓이 출력되었습니다.");
                paymentFrame.dispose();

                // 좌석 선택 창을 다시 표시하여 좌석 상태를 업데이트
                showSeatSelection(screeningID, screenID, movieTitle, showDate, startTime);

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "결제 처리 중 오류가 발생했습니다.");
            }
        });

        cancelButton.addActionListener(e -> paymentFrame.dispose());

        paymentFrame.setVisible(true);
    }

    private static void showAllScreenings(String movieTitle) {
        JFrame screeningFrame = new JFrame("영화 상영 일정");
        screeningFrame.setSize(600, 400);
        screeningFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        screeningFrame.setLayout(new BorderLayout());

        JTable screeningTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(screeningTable);
        screeningFrame.add(scrollPane, BorderLayout.CENTER);

        try (Connection conn = DriverManager.getConnection(URL, userID, userPW)) {
            // 특정 영화의 모든 상영 일정을 조회하는 쿼리
            String query = "SELECT s.ScreeningID, m.MovieTitle, s.ShowDate, s.StartTime, sc.ScreenID, sc.ScreenRows, sc.ScreenColumns " +
                           "FROM ScreeningInfo s " +
                           "JOIN Movies m ON s.MovieID = m.MovieID " +
                           "JOIN Screen sc ON s.ScreenID = sc.ScreenID " +
                           "WHERE m.MovieTitle = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, movieTitle);
            ResultSet resultSet = statement.executeQuery();

            screeningTable.setModel(buildTableModel(resultSet));
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "상영 일정 조회 중 오류가 발생했습니다.");
        }

        screeningFrame.setVisible(true);
    }
    
    private static void cancelBooking(int ticketID) {
        try (Connection conn = DriverManager.getConnection(URL, userID, userPW)) {
            // 예매 취소 쿼리: 티켓 정보를 삭제
            String deleteTicketQuery = "DELETE FROM Tickets WHERE TicketID = ?";
            PreparedStatement deleteTicketStmt = conn.prepareStatement(deleteTicketQuery);
            deleteTicketStmt.setInt(1, ticketID);
            deleteTicketStmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "예매가 취소되었습니다.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "예매 취소 중 오류가 발생했습니다.");
        }
    }

    private static void changeScreeningSchedule(int ticketID) {
        JFrame changeScheduleFrame = new JFrame("상영 일정 변경");
        changeScheduleFrame.setSize(600, 400);
        changeScheduleFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        changeScheduleFrame.setLayout(new BorderLayout());

        JTable scheduleTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        changeScheduleFrame.add(scrollPane, BorderLayout.CENTER);

        // 현재 예매된 영화의 상영 일정을 표시
        try (Connection conn = DriverManager.getConnection(URL, userID, userPW)) {
            String query = "SELECT s.ScreeningID, m.MovieTitle, s.ShowDate, s.StartTime, sc.ScreenID, sc.ScreenRows, sc.ScreenColumns " +
                           "FROM ScreeningInfo s " +
                           "JOIN Movies m ON s.MovieID = m.MovieID " +
                           "JOIN Screen sc ON s.ScreenID = sc.ScreenID " +
                           "WHERE s.MovieID = (SELECT MovieID FROM Tickets WHERE TicketID = ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, ticketID);
            ResultSet resultSet = statement.executeQuery();

            scheduleTable.setModel(buildTableModel(resultSet));
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "상영 일정 조회 중 오류가 발생했습니다.");
        }

        JPanel buttonPanel = new JPanel();
        JButton selectButton = new JButton("선택");
        buttonPanel.add(selectButton);
        changeScheduleFrame.add(buttonPanel, BorderLayout.SOUTH);

        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = scheduleTable.getSelectedRow();
                if (selectedRow != -1) {
                    int newScreeningID = (int) scheduleTable.getValueAt(selectedRow, 0);
                    updateScreeningSchedule(ticketID, newScreeningID);
                    changeScheduleFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "변경할 상영 일정을 선택해주세요.");
                }
            }
        });

        changeScheduleFrame.setVisible(true);
    }

    private static void updateScreeningSchedule(int ticketID, int newScreeningID) {
        try (Connection conn = DriverManager.getConnection(URL, userID, userPW)) {
            // 상영 일정을 업데이트하는 쿼리
            String updateScheduleQuery = "UPDATE Tickets SET ScreeningID = ? WHERE TicketID = ?";
            PreparedStatement updateScheduleStmt = conn.prepareStatement(updateScheduleQuery);
            updateScheduleStmt.setInt(1, newScreeningID);
            updateScheduleStmt.setInt(2, ticketID);
            updateScheduleStmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "상영 일정이 변경되었습니다.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "상영 일정 변경 중 오류가 발생했습니다.");
        }
    }
    private static void changeBooking(int ticketID) {
        JFrame changeBookingFrame = new JFrame("영화 변경");
        changeBookingFrame.setSize(800, 600);
        changeBookingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        changeBookingFrame.setLayout(new BorderLayout());

        JTable movieTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(movieTable);
        changeBookingFrame.add(scrollPane, BorderLayout.CENTER);

        // 모든 영화의 상영 정보를 표시
        try (Connection conn = DriverManager.getConnection(URL, userID, userPW)) {
            String query = "SELECT si.ScreeningID, m.MovieTitle, si.ShowDate, si.StartTime, sc.ScreenID " +
                           "FROM ScreeningInfo si " +
                           "JOIN Movies m ON si.MovieID = m.MovieID " +
                           "JOIN Screen sc ON si.ScreenID = sc.ScreenID";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            movieTable.setModel(buildTableModel(resultSet));
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "영화 상영 정보 조회 중 오류가 발생했습니다.");
        }

        JPanel buttonPanel = new JPanel();
        JButton selectButton = new JButton("선택");
        buttonPanel.add(selectButton);
        changeBookingFrame.add(buttonPanel, BorderLayout.SOUTH);

        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = movieTable.getSelectedRow();
                if (selectedRow != -1) {
                    int newScreeningID = (int) movieTable.getValueAt(selectedRow, 0);
                    updateBooking(ticketID, newScreeningID);
                    changeBookingFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "변경할 상영 정보를 선택해주세요.");
                }
            }
        });

        changeBookingFrame.setVisible(true);
    }

    private static void updateBooking(int ticketID, int newScreeningID) {
        try (Connection conn = DriverManager.getConnection(URL, userID, userPW)) {
            // 예매 정보를 업데이트하는 쿼리
            String updateBookingQuery = "UPDATE Tickets SET ScreeningID = ? WHERE TicketID = ?";
            PreparedStatement updateBookingStmt = conn.prepareStatement(updateBookingQuery);
            updateBookingStmt.setInt(1, newScreeningID);
            updateBookingStmt.setInt(2, ticketID);
            updateBookingStmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "예매가 변경되었습니다.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "예매 변경 중 오류가 발생했습니다.");
        }
    }



 // ResultSet을 DefaultTableModel로 변환하는 메소드
    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnName(i));
        }
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                vector.add(rs.getObject(i));
            }
            data.add(vector);
        }
        return new DefaultTableModel(data, columnNames);
    }

}
