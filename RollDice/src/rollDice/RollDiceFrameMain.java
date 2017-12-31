package rollDice;

import java.util.Random;

import javax.swing.UIManager;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;

public class RollDiceFrameMain extends JFrame {
	private static final long serialVersionUID = 1L;

	private static final int DICENUM = 5; // 화면에 나오는 주사위의 개수
	
	private JPanel contentPane;
	private JPanel dicePanel; // 주사위가 보여질 Panel
	private JPanel controlPanel; // checkBoxPanel, buttonPanel이 포함될 Panel
	private JPanel checkBoxPanel; // reset 체크버튼들이 포함될 Panel
	private JPanel buttonPanel; // roll, re-roll, reset버튼이 포함될 Panel
	private JLabel[] diceLabel = new JLabel [DICENUM]; // 주사위 그림이 나타날 Label
	private JCheckBox[] diceSelectCheckBox = new JCheckBox [DICENUM]; // 주사위별 reset(check)버튼
	private JButton rollDiceButton; // 주사위 전체 굴리기 버튼
	private JButton reRollDiceButton; // 선택한(체크박스 체크) 주사위들을 다시 굴리는 버튼
	private JButton resetDiceButton; // 모든 주사위를 초기화 하는 버튼

	private Random rand = new Random(); // 난수 발생을 위한 Random변수
	private int[] randomNumber = new int [DICENUM]; // n번째 주사위의 난수 값이 저장될 배열
	
	private int checkBoxCheckedCount = 0; // diceSelectCheckBox[]에 check된 개수

	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // 현재 화면의 크기(해상도)를 Dimension으로 받아옴
	private static final int FRAME_WIDTH = 200 * DICENUM; // 프로그램(Frame)의 너비(Width)
	private static final int FRAME_HEIGHT = 270; // 프로그램(Frame)의 높이(Height)

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); // Nimbus Look and Feel 적용
					RollDiceFrameMain frame = new RollDiceFrameMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initialize.
	 */
	public RollDiceFrameMain() { // default Constructor
		initialize();
	}

	/**
	 * Create the frame.
	 */
	void initialize() {
		setTitle("Roll Dice"); // 프로그램 제목 설정
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 프로그램 상단 제목의 닫기(X)버튼 눌렀을 때의 작

		setResizable(false); // 프로그램 화면 크기는 조절 불가능 하도록 함
		setSize(FRAME_WIDTH, FRAME_HEIGHT); // 프로그램의 화면 크기 지정
		setLocation((screenSize.width - FRAME_WIDTH) / 2, (screenSize.height - FRAME_HEIGHT) / 4); // 프로그램 첫 동작 시 나타날 위치 지정

		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		dicePanel = new JPanel();
		dicePanel.setLayout(new GridLayout(1, DICENUM, 0, 0)); // row = 1, column = DICENUM
		contentPane.add(dicePanel);

		controlPanel = new JPanel();
		controlPanel.setLayout(new BorderLayout(0, 0));
		contentPane.add(controlPanel, BorderLayout.SOUTH);

		checkBoxPanel = new JPanel();
		checkBoxPanel.setBackground(Color.WHITE);
		checkBoxPanel.setLayout(new GridLayout(1, DICENUM, 0, 0)); // row = 1, column = DICENUM
		controlPanel.add(checkBoxPanel, BorderLayout.NORTH);
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 3, 0, 0)); // row = 1, column = 3
		controlPanel.add(buttonPanel, BorderLayout.CENTER);

		for(int i = 0; i < DICENUM; i++) {
			diceLabel[i] = new JLabel("Click Roll Dice"); // 초기 실행 시 주사위 그림 영역에 보일 화면
			diceLabel[i].setHorizontalAlignment(SwingConstants.CENTER); // 위의 텍스트 가운데 정렬
			diceLabel[i].setOpaque(true);
			diceLabel[i].setBackground(Color.WHITE); // Label Background 설정
			dicePanel.add(diceLabel[i]);
			
			diceSelectCheckBox[i] = new JCheckBox();
			diceSelectCheckBox[i].setHorizontalAlignment(SwingConstants.CENTER);
			diceSelectCheckBox[i].setEnabled(false); // 주사위를 던지지 않은 첫 실행시에는 선택이 불가능하도록 함
			checkBoxPanel.add(diceSelectCheckBox[i]);
		}

		rollDiceButton = new JButton("Roll Dice"); // 주사위 던지기
		rollDiceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rollDiceAction();
			}
		});
		buttonPanel.add(rollDiceButton);

		reRollDiceButton = new JButton("Re-Roll Dice"); // 선택한 주사위들 다시 던지기
		reRollDiceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reRollDiceAction();
			}
		});
		reRollDiceButton.setEnabled(false);
		buttonPanel.add(reRollDiceButton);

		resetDiceButton = new JButton("Reset Dice"); // 모든 주사위 초기화
		resetDiceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetAction();
			}
		});
		resetDiceButton.setEnabled(false);
		buttonPanel.add(resetDiceButton);
		
		setEnableReRollDiceButton(); // 현재 상태에 따라 re-roll 버튼 활성화 하기

	}

	/**
	 * Roll Dice.
	 */
	void rollDiceAction() { // 주사위 던지기
		for(int i = 0; i < DICENUM; i++) {
			randomNumber[i] = Math.abs(rand.nextInt()) % 6 + 1; // 1~6까지의 난수들을 배열에 저장
		}

		for(int i = 0; i < DICENUM; i++) {
			diceLabel[i].setText(null);
			diceLabel[i].setIcon( // Label의 아이콘 지정(주사위 그림)
				new ImageIcon(
					RollDiceFrameMain.class.getResource(
							"/images/dice" + randomNumber[i] + "_200x200.png"
					)
				)
			);
			diceSelectCheckBox[i].setEnabled(true); // 주사위를 던졌으므로, checkBox선택 가능하게 해야함
		}
		
		for(int i = 0; i < DICENUM; i++) {
			diceSelectCheckBox[i].setSelected(false); // 초기 checkBox선택 상태는 false
		}
		
		resetDiceButton.setEnabled(true); // 주사위를 던졌으므로, reset버튼 선택 가능하게 해야함

		System.out.print("Roll ----- "); // 현재 주사위 상태(n번째 주사위의 눈 값) 출력
		for(int i = 0; i < DICENUM; i++) {
			System.out.print("Dice[" + (i + 1) + "] = " + randomNumber[i]);
			if(i != DICENUM - 1) {
				System.out.print(",   ");
			}
		}System.out.println();
	}

	void reRollDiceAction() { // 선택한 주사위들 다시 던지기
		for(int i = 0; i < DICENUM; i++) {
			if(diceSelectCheckBox[i].isSelected()) {
				randomNumber[i] = Math.abs(rand.nextInt()) % 6 + 1; // 1~6까지의 난수들을 배열에 저장
			}
		}

		for(int i = 0; i < DICENUM; i++) {
			if(diceSelectCheckBox[i].isSelected()) {
				diceLabel[i].setText(null);
				diceLabel[i].setIcon( // Label의 아이콘 지정(주사위 그림)
					new ImageIcon(
						RollDiceFrameMain.class.getResource(
							"/images/dice" + randomNumber[i] + "_200x200.png"
						)
					)
				);
			}
		}
		
		resetDiceButton.setEnabled(true); // 주사위를 던졌으므로, reset버튼 선택 가능하게 해야함

		System.out.print("Re-Roll -- "); // 현재 주사위 상태(n번째 주사위의 눈 값) 출력
		for(int i = 0; i < DICENUM; i++) {
			System.out.print("Dice[" + (i + 1) + "] = " + randomNumber[i]);
			if(i != DICENUM - 1) {
				System.out.print(",   ");
			}
		}System.out.println();
	}

	void setEnableReRollDiceButton() { // checkBox에 체크가 하나라도 있을 때, re-roll 버튼 활성화 하는 메소드
		for(JCheckBox dSelChkBox : diceSelectCheckBox) {
			dSelChkBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(dSelChkBox.isSelected()) {
						checkBoxCheckedCount++;
					}
					else {
						checkBoxCheckedCount--;
					}

					if(checkBoxCheckedCount != 0) { // checkBox에 체크가 하나라도 있는 경우
						reRollDiceButton.setEnabled(true); // re-roll 버튼 활성화
					}
					else {
						reRollDiceButton.setEnabled(false);
					}
				}
			});
		}
	}
	
	void resetAction() { // 모든 주사위 reset 하기
		for(int i = 0; i < DICENUM; i++) {
			diceLabel[i].setText("Click Roll Dice");
			diceLabel[i].setIcon(null);
			
			diceSelectCheckBox[i].setSelected(false); // checkBox의 모든 체크 해제
			diceSelectCheckBox[i].setEnabled(false); // checkBox 비활성화
		}
		
		checkBoxCheckedCount = 0; // checkBox 체크 개수 초기화
		reRollDiceButton.setEnabled(false); // re-roll 버튼 비활성화
		resetDiceButton.setEnabled(false); // reset 버튼 비활성화
		
		System.out.println("Reset Dice");
	}
	
}
