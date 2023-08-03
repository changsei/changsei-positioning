package send_manager;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class SendManager {

	private JFrame frame;
	private JPanel sendLogPanel;
	private JLabel lbSvrState;
	
	private JTextField textRecvrName;
	private JTextField textRecvrCoordinateX;
	private JTextField txtRecvrCoordinateY;	
	
	private JTextArea textSendLog;
	
	private JButton btnClickSvrConnect;
	private JButton btnClickSvrDisConnect;
	private JButton btnClickOpenLog;
	private JButton btnClickRecvrName;
	private JButton btnClickCloseLog;
	private JButton btnClickRecvrCoodinateX;
	private JButton btnClickRecvrCoodinateY;
	
	private boolean isOpenSendLogPanel = false;
	private boolean isOpenRecvrSettingsBtn = false;
	private long delay = 100;
	private long period = 1000;
	private Socket socket;
	private OutputStreamWriter toServer;
	private Timer timer;
	private TimerTask task;
	
	/**
	 * Create the application.
	 */
	public SendManager() {
		initialize();
		addAction();
		createTimer();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("장치 관리자");
		frame.setBounds(100, 100, 361, 195);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(null);
		
		JPanel recvrSettingsPanel = new JPanel();
		recvrSettingsPanel.setLocation(0, 0);
		mainPanel.add(recvrSettingsPanel);
		recvrSettingsPanel.setSize(347, 158);
		recvrSettingsPanel.setLayout(new GridLayout(5, 4, 10, 5));
		
		JLabel lbSvr = new JLabel("서버 상태   ");
		lbSvr.setHorizontalAlignment(SwingConstants.CENTER);
		lbSvr.setFont(new Font("나눔고딕", Font.BOLD, 12));
		lbSvr.setMinimumSize(new Dimension(90, -1));
		recvrSettingsPanel.add(lbSvr);
		
		lbSvrState = new JLabel("연결 없음");
		lbSvrState.setHorizontalAlignment(SwingConstants.CENTER);
		lbSvrState.setFont(new Font("나눔고딕", Font.BOLD, 12));
		recvrSettingsPanel.add(lbSvrState);
		
		btnClickSvrConnect = new JButton("연결");
		btnClickSvrConnect.setFont(new Font("나눔고딕", Font.BOLD, 12));
		recvrSettingsPanel.add(btnClickSvrConnect);
		
		btnClickSvrDisConnect = new JButton("연결해제");
		btnClickSvrDisConnect.setFont(new Font("나눔고딕", Font.BOLD, 12));
		btnClickSvrDisConnect.setEnabled(false);
		recvrSettingsPanel.add(btnClickSvrDisConnect);
		
		JLabel lbRecvr = new JLabel("리시버 설정");
		lbRecvr.setHorizontalAlignment(SwingConstants.CENTER);
		lbRecvr.setFont(new Font("나눔고딕", Font.BOLD, 12));
		recvrSettingsPanel.add(lbRecvr);
		
		JLabel lblLog = new JLabel("로그        ");
		lblLog.setHorizontalAlignment(SwingConstants.CENTER);
		lblLog.setFont(new Font("나눔고딕", Font.BOLD, 12));
		recvrSettingsPanel.add(lblLog);
		
		btnClickOpenLog = new JButton("열기");
		btnClickOpenLog.setFont(new Font("나눔고딕", Font.BOLD, 12));
		recvrSettingsPanel.add(btnClickOpenLog);
		
		btnClickCloseLog = new JButton("닫기");
		btnClickCloseLog.setFont(new Font("나눔고딕", Font.BOLD, 12));
		recvrSettingsPanel.add(btnClickCloseLog);
		
		Panel panel = new Panel();
		panel.setEnabled(false);
		recvrSettingsPanel.add(panel);
		
		JLabel lbRecvrName = new JLabel("이름        ");
		lbRecvrName.setHorizontalAlignment(SwingConstants.CENTER);
		lbRecvrName.setFont(new Font("나눔고딕", Font.BOLD, 12));
		recvrSettingsPanel.add(lbRecvrName);
		
		textRecvrName = new JTextField();
		textRecvrName.setFont(new Font("나눔고딕", Font.PLAIN, 12));
		textRecvrName.setText("이름 입력");
		recvrSettingsPanel.add(textRecvrName);
		textRecvrName.setColumns(10);
		
		btnClickRecvrName = new JButton("설정");
		btnClickRecvrName.setFont(new Font("나눔고딕", Font.BOLD, 12));
		btnClickRecvrName.setEnabled(false);
		recvrSettingsPanel.add(btnClickRecvrName);
		
		Panel panel_1 = new Panel();
		panel_1.setEnabled(false);
		recvrSettingsPanel.add(panel_1);
		
		JLabel lbRecvrCoordinateX = new JLabel("X좌표      ");
		lbRecvrCoordinateX.setHorizontalAlignment(SwingConstants.CENTER);
		lbRecvrCoordinateX.setFont(new Font("나눔고딕", Font.BOLD, 12));
		recvrSettingsPanel.add(lbRecvrCoordinateX);
		
		textRecvrCoordinateX = new JTextField();
		textRecvrCoordinateX.setFont(new Font("나눔고딕", Font.PLAIN, 12));
		textRecvrCoordinateX.setText("X 좌표 입력");
		recvrSettingsPanel.add(textRecvrCoordinateX);
		textRecvrCoordinateX.setColumns(10);
		
		btnClickRecvrCoodinateX = new JButton("설정");
		btnClickRecvrCoodinateX.setFont(new Font("나눔고딕", Font.BOLD, 12));
		btnClickRecvrCoodinateX.setEnabled(false);
		recvrSettingsPanel.add(btnClickRecvrCoodinateX);
		
		Panel panel_2 = new Panel();
		panel_2.setEnabled(false);
		recvrSettingsPanel.add(panel_2);
		
		JLabel lbRecvrCoordinateY = new JLabel("Y좌표      ");
		lbRecvrCoordinateY.setHorizontalAlignment(SwingConstants.CENTER);
		lbRecvrCoordinateY.setFont(new Font("나눔고딕", Font.BOLD, 12));
		recvrSettingsPanel.add(lbRecvrCoordinateY);
		
		txtRecvrCoordinateY = new JTextField();
		txtRecvrCoordinateY.setFont(new Font("나눔고딕", Font.PLAIN, 12));
		txtRecvrCoordinateY.setText("Y 좌표 입력");
		recvrSettingsPanel.add(txtRecvrCoordinateY);
		txtRecvrCoordinateY.setColumns(10);
		
		btnClickRecvrCoodinateY = new JButton("설정");
		btnClickRecvrCoodinateY.setFont(new Font("나눔고딕", Font.BOLD, 12));
		btnClickRecvrCoodinateY.setEnabled(false);
		recvrSettingsPanel.add(btnClickRecvrCoodinateY);
		
		sendLogPanel = new JPanel();
		sendLogPanel.setBounds(351, 0, 372, 151);
		mainPanel.add(sendLogPanel);
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		sendLogPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JScrollPane sendLogScrollPane = new JScrollPane();
		sendLogPanel.add(sendLogScrollPane);
		
		JLabel lbSendLog = new JLabel("송신 데이터");
		sendLogScrollPane.setColumnHeaderView(lbSendLog);
		lbSendLog.setFont(new Font("나눔고딕", Font.BOLD, 12));
		
		textSendLog = new JTextArea();
		sendLogScrollPane.setViewportView(textSendLog);
		textSendLog.setText("송신 데이터 없음");
		textSendLog.setFont(new Font("나눔고딕", Font.PLAIN, 13));
		frame.getContentPane().add(mainPanel);
		
		// 메인 프레임 갱신  
		frame.revalidate();
		frame.repaint();
	}
	
	private void addAction() {
		btnClickSvrConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					socket = new Socket(InetAddress.getLocalHost(), 8080);
					toServer = new OutputStreamWriter(socket.getOutputStream());
					toServer.write("Client: SendManager");
					toServer.flush();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		
		btnClickSvrDisConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!socket.isClosed()) {
					try {
						socket.close();
						toServer.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		
		btnClickOpenLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				repaintSendLogPanel();
			}
		});
		
		btnClickCloseLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				repaintSendLogPanel();
			}
		});
		
		btnClickRecvrName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendData(textRecvrName);
				writeLog(textRecvrName);
			}
		});
		
		btnClickRecvrCoodinateX.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendData(textRecvrCoordinateX);
				writeLog(textRecvrCoordinateX);
			}
		});
		
		btnClickRecvrCoodinateY.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendData(txtRecvrCoordinateY);
				writeLog(txtRecvrCoordinateY);
			}
		});
		
	}
	
	private void createTimer() {
		timer = new Timer();
		setUIState();
		timer.schedule(task, delay, period);
	}
	
	private void setUIState() {
		task = new TimerTask() {
			 @Override
			 public void run() {
				 // 프로그램 첫 시작시 발생하는 예외 처리 
				 if (socket == null) return;
				 
				 if (!socket.isClosed()) {
					 if (!isOpenRecvrSettingsBtn) {
						 isOpenRecvrSettingsBtn = true;
						 // UI 스레드 사용, EvenQueue에 추가
						 SwingUtilities.invokeLater(() -> {
							 btnClickSvrConnect.setEnabled(false);
							 btnClickSvrDisConnect.setEnabled(true);
							 btnClickRecvrName.setEnabled(true);
							 btnClickRecvrCoodinateX.setEnabled(true);
							 btnClickRecvrCoodinateY.setEnabled(true);
							 lbSvrState.setText("연결중     ");
						 });
					 }
				 } else {
					 if (isOpenRecvrSettingsBtn) {
						 isOpenRecvrSettingsBtn = false;
						 SwingUtilities.invokeLater(() -> {
							 btnClickSvrConnect.setEnabled(true);
							 btnClickSvrDisConnect.setEnabled(false);
							 btnClickRecvrName.setEnabled(false);
							 btnClickRecvrCoodinateX.setEnabled(false);
							 btnClickRecvrCoodinateY.setEnabled(false);
							 lbSvrState.setText("연결 없음");
						 });
					 }
				 }
			 }
		};
	}
	
	
	private void repaintSendLogPanel() {
		if (!isOpenSendLogPanel) {
			isOpenSendLogPanel = true;
			frame.setSize(751, 195);
			SwingUtilities.invokeLater(() -> {
				sendLogPanel.setVisible(true);
				frame.paint(frame.getGraphics());
				btnClickCloseLog.setEnabled(true);
				btnClickOpenLog.setEnabled(false);
			});
		} else {
			isOpenSendLogPanel = false;
			frame.setSize(361, 195);
			SwingUtilities.invokeLater(() -> {
				sendLogPanel.setVisible(false);
				frame.paint(frame.getGraphics());
				btnClickCloseLog.setEnabled(false);
				btnClickOpenLog.setEnabled(true);
			});
		}
	}
	
	private void sendData(JTextField txt) {
		try {
			if (socket.isConnected()) {
				String data = txt.getText();
				toServer.write(data);
				toServer.flush();
			}	
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private void writeLog(JTextField txt) {
		String log = txt.getText();
		textSendLog.append(log + "\n");
		textSendLog.setCaretPosition(textSendLog.getDocument().getLength());
	}
}
