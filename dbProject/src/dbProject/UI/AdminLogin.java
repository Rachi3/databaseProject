package dbProject.UI;

import dbProject.UI.*;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminLogin extends JFrame {
	private static final String URL = "jdbc:mysql://localhost:3306/db1";
	
	dbQuery query=new dbQuery();
	//관리자 ID/PW
	private static final String adminUser="root";
	private static final String adminPassword="1234";

	static private String ID="root";
	static private String PW="1234";
	
	private JTextField adminIdField=null;
    private JPasswordField adminPasswordField=null;
    
    public AdminLogin() {
        setTitle("관리자 로그인");
        setSize(300,170);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        	}
        	else {
        		JOptionPane.showMessageDialog(null,"로그인 실패");
        	}
        
        }
        });
        
             
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
    	viewTableButton.addActionListener(e->showAllTable());
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
    		
          
            stmt.close();
            conn.close();
    		JOptionPane.showMessageDialog(null, "DB 초기화 성공");
    		
    	}catch(SQLException e) {
    		JOptionPane.showMessageDialog(null, "DB 초기화 실패");
    		e.printStackTrace();
    	}
    	
    }
    
    private void insertDB() {
    	//원하는 조건식 입력받기
    	
    	//삽입 쿼리 실행
    }
    
    private void showAllTable() {
    	//전체 테이블을 조회하는 기능
    	
    	try(Connection conn=DriverManager.getConnection(URL,adminUser,adminPassword);
    			Statement stmt=conn.createStatement()){
    				
    			for(String tableName:query.tableNames) {
    				String tmpQuery="Select * From "+tableName+";";
    				
    				System.out.println(tmpQuery);
    				//db에서 데이터 읽어온후 테이블별 출력
    				//ResultSet rs = stmt.executeQuery(tmpQuery);
    				//ResultSetMetaData metaData = rs.getMetaData();
    				
    				JOptionPane.showMessageDialog(null, tmpQuery);
    			}
    			}catch(SQLException e) {
    				e.printStackTrace();
    			}
    }
}
