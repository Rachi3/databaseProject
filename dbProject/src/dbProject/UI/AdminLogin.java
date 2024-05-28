package dbProject.UI;

import dbProject.UI.*;
import java.sql.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminLogin extends JFrame {
	private static final String URL = "jdbc:mysql://localhost:3306/db1";
	
	dbQuery query=new dbQuery();
	//관리자 ID/PW
	private static final String adminUser="root";
	private static final String adminPassword="1234";
	private static boolean loggedIn = false;
	private int attributeCnt = 0; // 멤버 변수로 선언
	
	static private String ID="root";
	static private String PW="1234";
	
	private JTextField adminIdField=null;
    private JPasswordField adminPasswordField=null;
    
    public AdminLogin() {
    	if(MainInterface.isLoggedIn())
    	{
    		openControllWindow();
    	}
    	else {
        setTitle("관리자 로그인");
        setSize(300,170);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        
        //아이디 입력받기
        adminIdField=new JTextField(20);
        add(new JLabel("관리자 ID : "));
        add(adminIdField);
        
        //비번 입력받기
        adminPasswordField=new JPasswordField(20);
        add(new JLabel("관리자 PW : "));
        add(adminPasswordField);
        
        JButton loginButton=new JButton("로그인");
        add(loginButton);
        //입력받고 비교하기
       
        loginButton.addActionListener(new ActionListener() {
        @Override
        
        public void actionPerformed(ActionEvent e) {
        	String enteredID=adminIdField.getText();
        	String enteredPW=new String(adminPasswordField.getPassword());
        	//getPassword가 char[]를 리턴하기에 String으로 형변환
        	
        	if(ID.equals(enteredID)&&PW.equals(enteredPW)) {
        		JOptionPane.showMessageDialog(null,"로그인 성공");
        		
        		openControllWindow();//관리자 매소드 실행          
        		dispose();
        	}
        	else {
        		JOptionPane.showMessageDialog(null,"로그인 실패");
        	}
        
        }
        });
    }
             
    }
    private void openControllWindow() {
    	JFrame optionFrame = new JFrame("관리자 기능");
    	optionFrame.setSize(300,200);
    	optionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	optionFrame.setLayout(new FlowLayout());
    	
    	JButton initDbButton=new JButton("DB 초기화");
    	JButton insertDbButton=new JButton("입력");
    	JButton deleteDbButton=new JButton("삭제");
    	JButton updateDbButton=new JButton("수정");
    	JButton viewTableButton=new JButton("모든 테이블 보기");
    	
    	optionFrame.add(initDbButton);
    	optionFrame.add(insertDbButton);
    	optionFrame.add(deleteDbButton);
    	optionFrame.add(updateDbButton);
    	optionFrame.add(viewTableButton);
    	
    	optionFrame.setVisible(true);
    	initDbButton.addActionListener(e->resetDB());
    	insertDbButton.addActionListener(e->insertDB());
    	deleteDbButton.addActionListener(e->deleteTable());
    	updateDbButton.addActionListener(e->updateTable());
    	viewTableButton.addActionListener(e->showAllTable());
    }
    
    private void updateTable() {
        JFrame updateFrame = new JFrame("테이블 선택");
        updateFrame.setSize(300, 400);
        updateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        updateFrame.setLayout(new FlowLayout());

        try {
            Connection conn = DriverManager.getConnection(URL, adminUser, adminPassword);
            Statement stmt = conn.createStatement();
            // 테이블 이름들을 가져오는 쿼리
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet resultSet = metaData.getTables("db1", null, "%", new String[]{"TABLE"});

            // 테이블 이름들을 버튼으로 생성
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(0, 1));
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                JButton button = new JButton(tableName);
                panel.add(button);

                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // 새로운 창 생성
                        JFrame tableFrame = new JFrame(tableName);
                        tableFrame.setSize(600, 400);
                        tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        tableFrame.setLayout(new BorderLayout());

                        try {
                            // 선택된 테이블의 데이터를 조회하는 쿼리
                            String selectQuery = "SELECT * FROM " + tableName;
                            ResultSet rs = stmt.executeQuery(selectQuery);

                            // 테이블 데이터를 저장할 Vector
                            Vector<Vector<Object>> data = new Vector<>();
                            Vector<String> columnNames = new Vector<>();

                            // 테이블의 컬럼 이름 가져오기
                            ResultSetMetaData rsMetaData = rs.getMetaData();
                            int columnCount = rsMetaData.getColumnCount();
                            for (int i = 1; i <= columnCount; i++) {
                                columnNames.add(rsMetaData.getColumnName(i));
                            }

                            // 테이블의 데이터를 Vector에 저장
                            while (rs.next()) {
                                Vector<Object> row = new Vector<>();
                                for (int i = 1; i <= columnCount; i++) {
                                    row.add(rs.getObject(i));
                                }
                                data.add(row);
                            }

                            // 테이블 생성 및 데이터 추가
                            JTable table = new JTable(data, columnNames);
                            JScrollPane scrollPane = new JScrollPane(table);

                            // 수정 버튼 추가
                            JButton updateButton = new JButton("수정");
                            tableFrame.add(scrollPane, BorderLayout.CENTER);
                            tableFrame.add(updateButton, BorderLayout.SOUTH);

                            // 수정 버튼 클릭 이벤트 처리
                            updateButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    // 선택된 행의 데이터 가져오기
                                    int selectedRow = table.getSelectedRow();
                                    if (selectedRow == -1) {
                                        JOptionPane.showMessageDialog(null, "수정할 행을 선택하세요.");
                                        return;
                                    }

                                    // 선택된 행의 데이터를 입력창에 미리 채우기
                                    JPanel inputPanel = new JPanel(new GridLayout(0, 1));
                                    Vector<Object> rowData = data.get(selectedRow);
                                    JTextField[] textFields = new JTextField[columnCount];
                                    for (int i = 0; i < columnCount; i++) {
                                        textFields[i] = new JTextField(String.valueOf(rowData.get(i)));
                                        inputPanel.add(new JLabel(columnNames.get(i) + ":"));
                                        inputPanel.add(textFields[i]);
                                    }

                                    // 입력창을 팝업으로 표시
                                    int result = JOptionPane.showConfirmDialog(null, inputPanel, "행 수정", JOptionPane.OK_CANCEL_OPTION);
                                    if (result == JOptionPane.OK_OPTION) {
                                        // 수정된 데이터 가져오기
                                        try {
                                            StringBuilder updateQuery = new StringBuilder("UPDATE ");
                                            updateQuery.append(tableName).append(" SET ");

                                            boolean isFirstColumn = true;
                                            for (int i = 0; i < columnCount; i++) {
                                                if (!isFirstColumn) {
                                                    updateQuery.append(", ");
                                                }

                                                // 컬럼의 데이터 타입을 가져와서 Boolean인지 아닌지 확인
                                                int dataType = rsMetaData.getColumnType(i + 1);
                                                boolean isBoolean = (dataType == Types.BOOLEAN || dataType == Types.BIT);

                                                updateQuery.append(columnNames.get(i)).append(" = ");
                                                if (isBoolean) {
                                                    String inputValue = textFields[i].getText().trim().toLowerCase();
                                                    if (inputValue.equals("true") || inputValue.equals("1")) {
                                                        updateQuery.append("1");
                                                    } else if (inputValue.equals("false") || inputValue.equals("0")) {
                                                        updateQuery.append("0");
                                                    } else {
                                                        JOptionPane.showMessageDialog(null, "잘못된 Boolean 값입니다: " + textFields[i].getText());
                                                        return;
                                                    }
                                                } else {
                                                    updateQuery.append("'").append(textFields[i].getText()).append("'");
                                                }
                                                isFirstColumn = false;
                                            }

                                            // WHERE 절 추가 (수정할 조건)
                                            updateQuery.append(" WHERE ").append(columnNames.get(0)).append(" = '").append(rowData.get(0)).append("'");

                                            // 완성된 쿼리문 출력 (테스트용)
                                            // System.out.println("UPDATE Query: " + updateQuery.toString());

                                            // 쿼리 실행
                                            PreparedStatement pstmt = conn.prepareStatement(updateQuery.toString());
                                            pstmt.executeUpdate();
                                            JOptionPane.showMessageDialog(null, "행이 성공적으로 수정되었습니다.");
                                            tableFrame.dispose(); // 창 닫기
                                        } catch (SQLException ex) {
                                            ex.printStackTrace();
                                            JOptionPane.showMessageDialog(null, "행 수정 중 오류가 발생했습니다.");
                                        }
                                    }
                                }
                            });

                            tableFrame.setVisible(true);

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "테이블 데이터를 가져오는 중 오류가 발생했습니다.");
                        }
                    }
                });
            }

            updateFrame.add(panel);
            updateFrame.setVisible(true);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "테이블 데이터를 가져오는 중 오류가 발생했습니다.");
        }
    }


    
	private void resetDB() {
    	try(Connection conn=DriverManager.getConnection(URL,adminUser,adminPassword);
    		Statement stmt=conn.createStatement()){
    		    		
    		for(String commands:query.dropTables) {
    			stmt.executeUpdate(commands);
    		}
               
    		//table 생성
    		stmt.executeUpdate(query.createMoviesTable);
    		stmt.executeUpdate(query.createScreenTable);
    		stmt.executeUpdate(query.createScreeningInfoTable);
    		stmt.executeUpdate(query.createSeatsTable);
    		stmt.executeUpdate(query.createCustomersInfoTable);
    		stmt.executeUpdate(query.createBookInfoTable);
    		stmt.executeUpdate(query.createTicketsTable);
    		
            //샘플 데이터 삽입
    		stmt.executeUpdate(query.insertMovies);
    		stmt.executeUpdate(query.insertScreen);
    		stmt.executeUpdate(query.insertScreeningInfo);
    		stmt.executeUpdate(query.insertCustomerInfo);
    		stmt.executeUpdate(query.insertBookInfo);
    		
    		PreparedStatement pstmt = conn.prepareStatement(query.insertScreen);
            

            // 삽입된 Screen 데이터의 ScreenID, ScreenRows, ScreenColumns 값을 가져오기
            String selectScreens = "SELECT ScreenID, ScreenRows, ScreenColumns FROM Screen";
            pstmt = conn.prepareStatement(selectScreens);
            ResultSet rs = pstmt.executeQuery();
            
            // Seats 테이블에 데이터 삽입
            while (rs.next()) {
                int screenID = rs.getInt("ScreenID");
                int screenRows = rs.getInt("ScreenRows");
                int screenColumns = rs.getInt("ScreenColumns");

                for (int row = 1; row <= screenRows; row++) {
                    for (int col = 1; col <= screenColumns; col++) {
                    	
                        String insertSeat = "INSERT INTO Seats (ScreenID, SeatRow, SeatColumn) VALUES (?, ?, ?)";
                        System.out.println(insertSeat);
                        pstmt = conn.prepareStatement(insertSeat);
                        pstmt.setInt(1, screenID);
                        pstmt.setInt(2, row);
                        pstmt.setInt(3, col);
                        pstmt.executeUpdate();
                    }
                }
            }
            stmt.executeUpdate(query.insertTickets);
            
            stmt.close();
            conn.close();
    		JOptionPane.showMessageDialog(null, "DB 초기화 성공");
    		
    	}catch(SQLException e) {
    		JOptionPane.showMessageDialog(null, "DB 초기화 실패");
    		e.printStackTrace();
    	}
    	
    }
    
    private void insertDB() {
        JFrame frame = new JFrame("입력할 테이블 선택");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.setLayout(new FlowLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));
        
        

        try {
            // 테이블 이름들을 가져오는 쿼리
            Connection conn = DriverManager.getConnection(URL, adminUser, adminPassword);
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet resultSet = metaData.getTables("db1", null, "%", new String[]{"TABLE"});

            // 테이블 이름들을 버튼으로 생성
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME"); // 테이블 이름은 세 번째 열에 위치함
                JButton Button=new JButton(tableName);
                panel.add(Button); // 패널에 버튼 추가
                Button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    	
                        // 클릭된 버튼의 텍스트(테이블 이름) 가져오기
                        String tableClicked = ((JButton) e.getSource()).getText();

                        // 새로운 패널 생성
                        JPanel inputPanel = new JPanel(new GridLayout(0, 1)); // 1열의 그리드 레이아웃

                        // 테이블명과 속성값을 표시하는 라벨 생성 및 패널에 추가
                        JLabel tableLabel = new JLabel(tableClicked + "의 속성:");
                        inputPanel.add(tableLabel);

                        // 테이블의 속성들을 가져와서 텍스트 필드에 표시
                        StringBuilder propertyText = new StringBuilder();
                        try {
                        	attributeCnt = 0;
                            ResultSet tableColumns = metaData.getColumns(null, null, tableClicked, null);
                            while (tableColumns.next()) {
                                String isAutoIncrement = tableColumns.getString("IS_AUTOINCREMENT");
                                if (!"YES".equals(isAutoIncrement)) {
                                    String columnName = tableColumns.getString("COLUMN_NAME");
                                    propertyText.append(columnName).append("/");
                                    attributeCnt++;
                                }
}
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "테이블의 속성 값을 가져오는데 실패했습니다.");
                        }

                        // 속성값을 표시하는 라벨 생성 및 패널에 추가
                        JLabel propertyLabel = new JLabel(propertyText.toString());
                        inputPanel.add(propertyLabel);

                        // 입력을 받을 텍스트 필드 생성 및 패널에 추가
                        JTextField textField = new JTextField(30); // 입력 받을 텍스트 필드
                        inputPanel.add(textField);

                        // 입력과 취소 버튼 생성
                        JButton submitButton = new JButton("입력");
                        JButton cancelButton = new JButton("취소");
                        JPanel buttonPanel = new JPanel();
                        buttonPanel.add(submitButton);
                        buttonPanel.add(cancelButton);

                        // 버튼 패널을 패널에 추가
                        inputPanel.add(buttonPanel);

                        // 프레임에 패널 추가
                        frame.getContentPane().removeAll(); // 기존의 내용을 지우고
                        frame.getContentPane().add(inputPanel); // 패널을 추가

                        // 프레임의 크기 재설정
                        frame.setSize(600, 400);

                        // 프레임을 다시 그리기
                        frame.revalidate();
                        frame.repaint();

                        // 입력 버튼의 액션 리스너 등록
                        submitButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String inputText = textField.getText();
                                String[] attributes = inputText.split("/");

                                if (attributes.length != attributeCnt) {
                                    JOptionPane.showMessageDialog(null, "입력한 값의 개수가 테이블의 속성 개수와 일치하지 않습니다.");
                                    return;
                                }

                                // 데이터 삽입 쿼리 실행
                                StringBuilder queryBuilder = new StringBuilder("INSERT INTO ");
                                queryBuilder.append(tableClicked).append(" (");

                                try {
                                    // 테이블의 열 정보 가져오기
                                    ResultSet tableColumns = metaData.getColumns(null, null, tableClicked, null);
                                    boolean isFirstColumn = true; // 첫 번째 열 여부 확인을 위한 변수

                                    // 열 정보를 바탕으로 INSERT 쿼리의 열 부분 작성
                                    while (tableColumns.next()) {
                                        String isAutoIncrement = tableColumns.getString("IS_AUTOINCREMENT");
                                        if (!"YES".equals(isAutoIncrement)) { // Auto Increment가 아닌 경우에만 처리
                                            if (!isFirstColumn) {
                                                queryBuilder.append(", "); // 첫 번째 열이 아닌 경우 쉼표 추가
                                            }
                                            
                                            String columnName = tableColumns.getString("COLUMN_NAME");
                                            queryBuilder.append(columnName);
                                            isFirstColumn = false; // 첫 번째 열 여부 갱신
                                        }
                                    }
                                    queryBuilder.append(") VALUES (");

                                    // 입력된 값들을 쿼리에 추가
                                    String[] attributes1 = inputText.split("/");
                                    tableColumns.beforeFirst(); // ResultSet을 다시 처음부터 순회하기 위해 초기화
                                    int i = 0;
                                    while (tableColumns.next()) {
                                        String isAutoIncrement = tableColumns.getString("IS_AUTOINCREMENT");
                                        if (!"YES".equals(isAutoIncrement)) { // Auto Increment가 아닌 경우에만 처리
                                            if (i > 0) {
                                                queryBuilder.append(", "); // 첫 번째 값 이후에는 쉼표 추가
                                            }
                                            
                                            // 컬럼의 데이터 타입을 가져와서 Boolean인지 아닌지 확인
                                            int dataType = tableColumns.getInt("DATA_TYPE");
                                            boolean isBoolean = (dataType == Types.BOOLEAN || dataType == Types.BIT);
                                            
                                            // Boolean 타입인 경우 처리
                                            if (isBoolean) {
                                                String inputValue = attributes1[i].trim().toLowerCase();
                                                if (inputValue.equals("true") || inputValue.equals("1")) {
                                                    queryBuilder.append("1");
                                                } else if (inputValue.equals("false") || inputValue.equals("0")) {
                                                    queryBuilder.append("0");
                                                } else {
                                                    JOptionPane.showMessageDialog(null, "잘못된 Boolean 값입니다: " + attributes1[i]);
                                                    return;
                                                }
                                            } else {
                                                // Boolean이 아닌 경우의 처리
                                                queryBuilder.append("'").append(attributes1[i]).append("'");
                                            }
                                            
                                            i++;
                                        }
                                    }
                                    queryBuilder.append(")");

                                    // 완성된 쿼리문 출력 (테스트용)
                                    //System.out.println("INSERT Query: " + queryBuilder.toString());

                                    // 쿼리 실행
                                    try (PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString())) {
                                        pstmt.executeUpdate();
                                        JOptionPane.showMessageDialog(null, "데이터 삽입 성공");
                                    } catch (SQLException ex) {
                                        ex.printStackTrace();
                                        JOptionPane.showMessageDialog(null, "데이터 삽입 실패");
                                    }
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                    JOptionPane.showMessageDialog(null, "테이블의 열 정보를 가져오는데 실패했습니다.");
                                }
                            }
                        });


                        // 취소 버튼의 액션 리스너 등록
                        cancelButton.addActionListener(new ActionListener() {
                        	@Override
                            public void actionPerformed(ActionEvent e) {
                                // 입력 필드 초기화
                                textField.setText("");
                            }
                        });
                    }
                });


                  
            }

            frame.getContentPane().add(panel); // 프레임에 패널 추가
            JOptionPane.showMessageDialog(null, "어떤 테이블을 입력할지 골라주세요!");
            frame.setVisible(true);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "테이블 정보를 가져오는데 실패했습니다.");
        }
    }


    private void showAllTable() {
        //전체 테이블을 조회하는 기능
        try(Connection conn=DriverManager.getConnection(URL,adminUser,adminPassword)) {
            Statement stmt=conn.createStatement();
            JFrame viewFrame=new JFrame();
            
            viewFrame.setTitle("Database Viewer");
            viewFrame.setSize(800, 600);
            viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            viewFrame.setLocationRelativeTo(null);
            viewFrame.setLayout(new BorderLayout());
            
            JTabbedPane tabbedPane = new JTabbedPane();
            for(String tableName:query.tableNames) {
                String tmpQuery="Select * From "+tableName+";";
                
                //db에서 데이터 읽어온후 테이블별 출력
                ResultSet rs = stmt.executeQuery(tmpQuery);
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                
                // 테이블 데이터 읽기
                Vector<String> columnNames = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    columnNames.add(metaData.getColumnName(i));
                }

                Vector<Vector<Object>> data = new Vector<>();
                
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.add(rs.getObject(i));
                    }
                    data.add(row);
                }

                // JTable 생성
                JTable table = new JTable(data, columnNames);
                JScrollPane scrollPane = new JScrollPane(table);

                // 탭에 추가
                tabbedPane.addTab(tableName, scrollPane);
                
                viewFrame.add(tabbedPane, BorderLayout.CENTER);
                viewFrame.setVisible(true);
            }
            stmt.close();
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteTable() {
        JFrame deleteFrame = new JFrame("테이블 선택");
        deleteFrame.setSize(300, 400);
        deleteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        deleteFrame.setLayout(new FlowLayout());

        try {
            Connection conn = DriverManager.getConnection(URL, adminUser, adminPassword);
            Statement stmt = conn.createStatement();
            // 테이블 이름들을 가져오는 쿼리
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet resultSet = metaData.getTables("db1", null, "%", new String[]{"TABLE"});

            // 테이블 이름들을 버튼으로 생성
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(0, 1));
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                JButton button = new JButton(tableName);
                panel.add(button);

                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // 새로운 창 생성
                        JFrame tableFrame = new JFrame(tableName);
                        tableFrame.setSize(600, 400);
                        tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        tableFrame.setLayout(new BorderLayout());

                        try {
                            // 선택한 테이블의 데이터를 조회하는 쿼리
                            String selectQuery = "SELECT * FROM " + tableName;
                            ResultSet rs = stmt.executeQuery(selectQuery);

                            // 테이블 데이터를 저장할 Vector
                            Vector<Vector<Object>> data = new Vector<>();
                            Vector<String> columnNames = new Vector<>();

                            // 테이블의 컬럼 이름 가져오기
                            ResultSetMetaData metaData = rs.getMetaData();
                            int columnCount = metaData.getColumnCount();
                            for (int i = 1; i <= columnCount; i++) {
                                columnNames.add(metaData.getColumnName(i));
                            }

                            // 테이블의 데이터를 Vector에 저장
                            while (rs.next()) {
                                Vector<Object> row = new Vector<>();
                                for (int i = 1; i <= columnCount; i++) {
                                    row.add(rs.getObject(i));
                                }
                                data.add(row);
                            }

                            // 테이블 생성 및 데이터 추가
                            JTable table = new JTable(data, columnNames);
                            JScrollPane scrollPane = new JScrollPane(table);

                            // 삭제 버튼 추가
                            JButton deleteButton = new JButton("삭제");
                            tableFrame.add(scrollPane, BorderLayout.CENTER);
                            tableFrame.add(deleteButton, BorderLayout.SOUTH);

                            // 삭제 버튼 클릭 이벤트 처리
                            deleteButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    // 선택된 행의 primary key 값 가져오기
                                    int selectedRow = table.getSelectedRow();
                                    if (selectedRow == -1) {
                                        JOptionPane.showMessageDialog(null, "삭제할 행을 선택하세요.");
                                        return;
                                    }

                                    try {
                                        // 선택된 행의 primary key 값 가져오기
                                        DatabaseMetaData metaData = conn.getMetaData();
                                        ResultSet primaryKeyResultSet = metaData.getPrimaryKeys(null, null, tableName);
                                        String primaryKeyColumnName = null;

                                        // 선택된 테이블의 primary key 정보 가져오기
                                        while (primaryKeyResultSet.next()) {
                                            primaryKeyColumnName = primaryKeyResultSet.getString("COLUMN_NAME");
                                        }
                                        Object primaryKeyValue = table.getValueAt(selectedRow, 0);
                                        // primary key를 이용하여 DELETE 쿼리 실행
                                        String deleteQuery = "DELETE FROM " + tableName + " WHERE " + primaryKeyColumnName + " = ?";
                                        PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
                                        pstmt.setObject(1, primaryKeyValue);
                                        pstmt.executeUpdate();

                                        JOptionPane.showMessageDialog(null, "선택하신 행이 삭제되었습니다.");
                                        // 삭제 후에 기존 창을 다시 그려주기
                                        tableFrame.dispose(); // 기존 창 닫기
                                        
                                    } catch (SQLException ex) {
                                        ex.printStackTrace();
                                        JOptionPane.showMessageDialog(null, "행 삭제 중 오류가 발생했습니다.");
                                    }
                                }
                            });


                            tableFrame.setVisible(true);

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "테이블 데이터를 가져오는 중 오류가 발생했습니다.");
                        }
                    }
                });
            }

            deleteFrame.add(panel);
            deleteFrame.setVisible(true);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "테이블 데이터를 가져오는 중 오류가 발생했습니다.");
        }
    }



}




