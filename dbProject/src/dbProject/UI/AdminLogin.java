package dbProject.UI;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminLogin extends JFrame {
	private static final String URL = "jdbc:mysql://localhost:3306/db1";
	
	//관리자 ID/PW
	private static final String adminUser="root";
	private static final String adminPassword="1234";
	Connection conn=null;
	Statement stmt = null;
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
        		
        		try {   conn = DriverManager.getConnection(URL,adminUser,adminPassword);//root 계정으로 접속
        				stmt = conn.createStatement();
        				openControllWindow();//관리자 매소드 실행
        			    
        			}catch(SQLException e1) {
        				JOptionPane.showMessageDialog(null,"DB 연결 실패");
        				throw new RuntimeException("DB connection failed",e1);
        			}
        		
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
    	initDbButton.addActionListener(e->{
    		
    	});
    }
}
