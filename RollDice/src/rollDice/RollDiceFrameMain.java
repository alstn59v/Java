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

	private static final int DICENUM = 5; // ȭ�鿡 ������ �ֻ����� ����
	
	private JPanel contentPane;
	private JPanel dicePanel; // �ֻ����� ������ Panel
	private JPanel controlPanel; // checkBoxPanel, buttonPanel�� ���Ե� Panel
	private JPanel checkBoxPanel; // reset üũ��ư���� ���Ե� Panel
	private JPanel buttonPanel; // roll, re-roll, reset��ư�� ���Ե� Panel
	private JLabel[] diceLabel = new JLabel [DICENUM]; // �ֻ��� �׸��� ��Ÿ�� Label
	private JCheckBox[] diceSelectCheckBox = new JCheckBox [DICENUM]; // �ֻ����� reset(check)��ư
	private JButton rollDiceButton; // �ֻ��� ��ü ������ ��ư
	private JButton reRollDiceButton; // ������(üũ�ڽ� üũ) �ֻ������� �ٽ� ������ ��ư
	private JButton resetDiceButton; // ��� �ֻ����� �ʱ�ȭ �ϴ� ��ư

	private Random rand = new Random(); // ���� �߻��� ���� Random����
	private int[] randomNumber = new int [DICENUM]; // n��° �ֻ����� ���� ���� ����� �迭
	
	private int checkBoxCheckedCount = 0; // diceSelectCheckBox[]�� check�� ����

	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // ���� ȭ���� ũ��(�ػ�)�� Dimension���� �޾ƿ�
	private static final int FRAME_WIDTH = 200 * DICENUM; // ���α׷�(Frame)�� �ʺ�(Width)
	private static final int FRAME_HEIGHT = 270; // ���α׷�(Frame)�� ����(Height)

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); // Nimbus Look and Feel ����
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
		setTitle("Roll Dice"); // ���α׷� ���� ����
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ���α׷� ��� ������ �ݱ�(X)��ư ������ ���� ��

		setResizable(false); // ���α׷� ȭ�� ũ��� ���� �Ұ��� �ϵ��� ��
		setSize(FRAME_WIDTH, FRAME_HEIGHT); // ���α׷��� ȭ�� ũ�� ����
		setLocation((screenSize.width - FRAME_WIDTH) / 2, (screenSize.height - FRAME_HEIGHT) / 4); // ���α׷� ù ���� �� ��Ÿ�� ��ġ ����

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
			diceLabel[i] = new JLabel("Click Roll Dice"); // �ʱ� ���� �� �ֻ��� �׸� ������ ���� ȭ��
			diceLabel[i].setHorizontalAlignment(SwingConstants.CENTER); // ���� �ؽ�Ʈ ��� ����
			diceLabel[i].setOpaque(true);
			diceLabel[i].setBackground(Color.WHITE); // Label Background ����
			dicePanel.add(diceLabel[i]);
			
			diceSelectCheckBox[i] = new JCheckBox();
			diceSelectCheckBox[i].setHorizontalAlignment(SwingConstants.CENTER);
			diceSelectCheckBox[i].setEnabled(false); // �ֻ����� ������ ���� ù ����ÿ��� ������ �Ұ����ϵ��� ��
			checkBoxPanel.add(diceSelectCheckBox[i]);
		}

		rollDiceButton = new JButton("Roll Dice"); // �ֻ��� ������
		rollDiceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rollDiceAction();
			}
		});
		buttonPanel.add(rollDiceButton);

		reRollDiceButton = new JButton("Re-Roll Dice"); // ������ �ֻ����� �ٽ� ������
		reRollDiceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reRollDiceAction();
			}
		});
		reRollDiceButton.setEnabled(false);
		buttonPanel.add(reRollDiceButton);

		resetDiceButton = new JButton("Reset Dice"); // ��� �ֻ��� �ʱ�ȭ
		resetDiceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetAction();
			}
		});
		resetDiceButton.setEnabled(false);
		buttonPanel.add(resetDiceButton);
		
		setEnableReRollDiceButton(); // ���� ���¿� ���� re-roll ��ư Ȱ��ȭ �ϱ�

	}

	/**
	 * Roll Dice.
	 */
	void rollDiceAction() { // �ֻ��� ������
		for(int i = 0; i < DICENUM; i++) {
			randomNumber[i] = Math.abs(rand.nextInt()) % 6 + 1; // 1~6������ �������� �迭�� ����
		}

		for(int i = 0; i < DICENUM; i++) {
			diceLabel[i].setText(null);
			diceLabel[i].setIcon( // Label�� ������ ����(�ֻ��� �׸�)
				new ImageIcon(
					RollDiceFrameMain.class.getResource(
							"/images/dice" + randomNumber[i] + "_200x200.png"
					)
				)
			);
			diceSelectCheckBox[i].setEnabled(true); // �ֻ����� �������Ƿ�, checkBox���� �����ϰ� �ؾ���
		}
		
		for(int i = 0; i < DICENUM; i++) {
			diceSelectCheckBox[i].setSelected(false); // �ʱ� checkBox���� ���´� false
		}
		
		resetDiceButton.setEnabled(true); // �ֻ����� �������Ƿ�, reset��ư ���� �����ϰ� �ؾ���

		System.out.print("Roll ----- "); // ���� �ֻ��� ����(n��° �ֻ����� �� ��) ���
		for(int i = 0; i < DICENUM; i++) {
			System.out.print("Dice[" + (i + 1) + "] = " + randomNumber[i]);
			if(i != DICENUM - 1) {
				System.out.print(",   ");
			}
		}System.out.println();
	}

	void reRollDiceAction() { // ������ �ֻ����� �ٽ� ������
		for(int i = 0; i < DICENUM; i++) {
			if(diceSelectCheckBox[i].isSelected()) {
				randomNumber[i] = Math.abs(rand.nextInt()) % 6 + 1; // 1~6������ �������� �迭�� ����
			}
		}

		for(int i = 0; i < DICENUM; i++) {
			if(diceSelectCheckBox[i].isSelected()) {
				diceLabel[i].setText(null);
				diceLabel[i].setIcon( // Label�� ������ ����(�ֻ��� �׸�)
					new ImageIcon(
						RollDiceFrameMain.class.getResource(
							"/images/dice" + randomNumber[i] + "_200x200.png"
						)
					)
				);
			}
		}
		
		resetDiceButton.setEnabled(true); // �ֻ����� �������Ƿ�, reset��ư ���� �����ϰ� �ؾ���

		System.out.print("Re-Roll -- "); // ���� �ֻ��� ����(n��° �ֻ����� �� ��) ���
		for(int i = 0; i < DICENUM; i++) {
			System.out.print("Dice[" + (i + 1) + "] = " + randomNumber[i]);
			if(i != DICENUM - 1) {
				System.out.print(",   ");
			}
		}System.out.println();
	}

	void setEnableReRollDiceButton() { // checkBox�� üũ�� �ϳ��� ���� ��, re-roll ��ư Ȱ��ȭ �ϴ� �޼ҵ�
		for(JCheckBox dSelChkBox : diceSelectCheckBox) {
			dSelChkBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(dSelChkBox.isSelected()) {
						checkBoxCheckedCount++;
					}
					else {
						checkBoxCheckedCount--;
					}

					if(checkBoxCheckedCount != 0) { // checkBox�� üũ�� �ϳ��� �ִ� ���
						reRollDiceButton.setEnabled(true); // re-roll ��ư Ȱ��ȭ
					}
					else {
						reRollDiceButton.setEnabled(false);
					}
				}
			});
		}
	}
	
	void resetAction() { // ��� �ֻ��� reset �ϱ�
		for(int i = 0; i < DICENUM; i++) {
			diceLabel[i].setText("Click Roll Dice");
			diceLabel[i].setIcon(null);
			
			diceSelectCheckBox[i].setSelected(false); // checkBox�� ��� üũ ����
			diceSelectCheckBox[i].setEnabled(false); // checkBox ��Ȱ��ȭ
		}
		
		checkBoxCheckedCount = 0; // checkBox üũ ���� �ʱ�ȭ
		reRollDiceButton.setEnabled(false); // re-roll ��ư ��Ȱ��ȭ
		resetDiceButton.setEnabled(false); // reset ��ư ��Ȱ��ȭ
		
		System.out.println("Reset Dice");
	}
	
}
