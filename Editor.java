package org.sp.app0706.io;


import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Editor extends JFrame implements ActionListener{

	JMenuBar bar;
	JMenu[] menu;
	JMenuItem[] item;
	JTextArea area;
	JScrollPane scroll;
	JFileChooser chooser;
	
	public Editor() {
		bar = new JMenuBar();
		menu = new JMenu[5];
		item = new JMenuItem[8];
		area = new JTextArea();
		scroll = new JScrollPane(area);
		chooser = new JFileChooser("D:/morning/javase_workspace");
		
		//메뉴5개 생성
		String[] menuName= {"파일","편집","서식","보기","도움말"};
		
		for(int i=0;i<menu.length;i++) {
			menu[i]=new JMenu(menuName[i]);
			//생성된 메뉴를 바에 부착
			bar.add(menu[i]);
		}
		
		//메뉴아이템 생성
		String[] itemName= {"새로만들기","새창","열기","저장","다른이름으로 저장","페이지","인쇄","끝내기"};
		
		//java 5(jdk 1.5)부터는 개선된(improved) for문을 지원
		//반복문의 대사이 컬레션, 배열 등의 집합인 경우 유용
		for(int i=0; i<itemName.length;i++) {
			item[i]=new JMenuItem(itemName[i]);
			menu[0].add(item[i]);
			
			
			
			//메뉴아이템들에 리스너 연결
			item[i].addActionListener(this);
		}
		
		//속성지정
		area.setBackground(Color.black);
		area.setForeground(Color.yellow);
		
		//조립
		setJMenuBar(bar);//프레임에 바 부착
		add(scroll);
		
		setSize(800,700);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);//margin auto
		
		setFont();
	}
	
	public void setFont() {
		Font font=new Font("돋움체", Font.PLAIN,16);
		
		area.setFont(font);
	}
	
	//바이트 기반 스트림으로 파일 열기
	public void openFile() {

		System.out.println("열거야?");
		
		int result=chooser.showOpenDialog(this);
		
		//스트림은 기복적으로 1byte씩 처리되므로 영문의 이외의 문자를 해설할 수 없다.(2byte로 표현되는 문자-영문 제외 전세계 문자)
		FileInputStream fis=null;
		FileReader reader;
		
		if(result==JFileChooser.APPROVE_OPTION) {
			File file=chooser.getSelectedFile();
			
			//FileInputStream생성시 경로도 가능하지만 File 자체도 가능함
			try {
				fis = new FileInputStream(file);//빨대 꽂기
				int data=-1;
				
				byte[] b=new byte[1024];
				
				while(true) {
					//실행중인 프로그램이 스트림으로부터 한  알갱이 즉 1byte 읽기
					
					data=fis.read(b);
					if(data==-1)break;
					//읽어들인 데이터는 b에 담겨져 있다.
					String str=new String(b);
					
					System.out.println(str);
					
					//자바의 모든 기본자료형마다 1:1 대응하는 Wrapper클래스가 지원됨
					//ex)int
					//boolean : Boolean
					//char : Character
					//character -->String 
					
					area.append(str+"\n");
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}

	//문자기반 스트림으로 파일 열기
	public void openFileByReader() {
		int result=chooser.showOpenDialog(this);//파일 탐색기 열기
		if(result==JFileChooser.APPROVE_OPTION) {//열기 선택시
			File file=chooser.getSelectedFile();
			
			//영문뿐만 아니라 전 세계 모든 문자를 해석할 수 있는 능력이 있는 스트림을 이용해보자
			FileReader fr=null;
			try {
				fr=new FileReader(file);
				
				int data=-1;
				
				//영문도 1자 한글도 1자로 인식
				//apple 맛나요 : 9회 읽어들임
				while(true) {
					data=fr.read();//한 문자 읽기
					if(data==-1)break;
					System.out.println((char)data);
	
					area.append(Character.toString((char)data));
				}
				
				
				
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//버퍼까지 처리된 문자기반 스트림으로 파일 열기
	//버퍼가 처리된 스트림은 접두어에 Buffered~~
	public void openFileByBuffer() {
		int result =chooser.showOpenDialog(this);
		
		FileReader reader=null;
		BufferedReader buffr=null;
		
		if(result==JFileChooser.APPROVE_OPTION) {
			File file =chooser.getSelectedFile();
			try {
				reader = new FileReader(file);
				buffr = new BufferedReader(reader);
				String msg=null;
				
				while(true) {
					msg=buffr.readLine();//한줄을 읽어들임(맨끝에 \n줄바꿈 만나면)
					if(msg==null)break;
					area.append(msg+"\n");
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				if(buffr!=null) {//인스턴스가 존재한다면
					try {
						buffr.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(reader!=null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		JMenuItem obj=(JMenuItem)e.getSource();
		
		//열기눌렀을 때
		if(obj==item[2]) {
			openFileByBuffer();
		}
	}
	
	public static void main(String[] args) {
		new Editor();
	}
}
