package dbProject.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainInterface extends JFrame {
	private static boolean loggedIn = false;
	private static boolean userLoggedIn = false;
	
    public MainInterface() {
   
        setSize(300,200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("초기 선택");

        // 버튼 생성
        JButton adminButton = new JButton("관리자");
        JButton customerButton = new JButton("사용자");

        setLayout(new FlowLayout()); //배치 관리자
        

        // 생성한 버튼을 프레임에 추가
        this.add(adminButton);
        this.add(customerButton);

        adminButton.addActionListener(new ActionListener(){
        	@Override
        	
        	public void actionPerformed(ActionEvent e) {
        		new AdminLogin().setVisible(!loggedIn);//한번 로그인했을경우 다시 로그인하지 않고 진행하게끔
        		loggedIn=true;
        	}
        });//관리자 버튼 클릭시 로그인 창 띄우기
      /*
        customerButton.addActionListener(new ActionListener() {
        	@Override
        	
        	public void actionPerformed(ActionEvent e) {
        		new UserLogin().setVisible(!userLoggedIn);
        		userLoggedIn=true;
        	}
        });*/
        // 창을 보이게 함
        setVisible(true);
    }

    public static void main(String[] args) {
        new MainInterface(); // MainInterface 객체 생성
    	//dbQuery q=new dbQuery();
    	
    	//System.out.println(q.createBookInfoTable);
    }
    static boolean isLoggedIn() {
    	return loggedIn;
    }
    
    static boolean isUserLoggedIn() {
    	return userLoggedIn;
    }
}
