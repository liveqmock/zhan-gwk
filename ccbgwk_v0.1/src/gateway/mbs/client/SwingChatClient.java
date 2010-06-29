package gateway.mbs.client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class SwingChatClient extends JFrame {

	KFSocketClient client;
	private static final long serialVersionUID = 1538675161745436968L;

	public JTextField inputText;

	public JButton loginButton;

	public JButton quitButton;

	public JTextField portField;

	public JTextField hostField;

	public JTextArea area;

	public JScrollBar scroll;

	public SwingChatClient() {
		super("socket client");

		loginButton = new JButton(new LoginAction());
		loginButton.setText("Connect");
		quitButton = new JButton(new LogoutAction());
		quitButton.setText("Disconnect");

		inputText = new JTextField(30);
		inputText.setAction(new BroadcastAction());
		area = new JTextArea(10, 50);
		area.setLineWrap(true);
		area.setEditable(false);
		scroll = new JScrollBar();
		scroll.add(area);
		hostField = new JTextField(10);
		hostField.setText("localhost");
		// nameField.setEditable(false);
		portField = new JTextField(10);
		portField.setText("8090");
		// serverField.setEditable(false);

		JPanel h = new JPanel();
		h.setLayout(new BoxLayout(h, BoxLayout.LINE_AXIS));
		h.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JLabel nameLabel = new JLabel("host: ");
		JLabel serverLabel = new JLabel("port: ");
		h.add(nameLabel);
		h.add(Box.createRigidArea(new Dimension(10, 0)));
		h.add(hostField);
		h.add(Box.createRigidArea(new Dimension(10, 0)));
		h.add(Box.createHorizontalGlue());
		h.add(Box.createRigidArea(new Dimension(10, 0)));
		h.add(serverLabel);
		h.add(Box.createRigidArea(new Dimension(10, 0)));
		h.add(portField);

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));
		p.setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel left = new JPanel();
		left.setLayout(new BoxLayout(left, BoxLayout.PAGE_AXIS));
		left.add(area);
		left.add(Box.createRigidArea(new Dimension(0, 5)));
		left.add(Box.createHorizontalGlue());
		left.add(inputText);

		JPanel right = new JPanel();
		right.setLayout(new BoxLayout(right, BoxLayout.PAGE_AXIS));
		right.add(loginButton);
		right.add(Box.createRigidArea(new Dimension(0, 5)));
		right.add(quitButton);
		right.add(Box.createHorizontalGlue());
		right.add(Box.createRigidArea(new Dimension(0, 25)));

		p.add(left);
		p.add(Box.createRigidArea(new Dimension(10, 0)));
		p.add(right);

		getContentPane().add(h, BorderLayout.NORTH);
		getContentPane().add(p);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public class LoginAction extends AbstractAction {
		public static final long serialVersionUID = 3596719854773863244L;

		public void actionPerformed(ActionEvent e) {
			try {
				client = new KFSocketClient(hostField.getText(), Integer
						.parseInt(portField.getText()), SwingChatClient.this);
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public class LogoutAction extends AbstractAction {
		public static final long serialVersionUID = 1655297424639924560L;

		public void actionPerformed(ActionEvent e) {
			try {
				client.bc.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public class BroadcastAction extends AbstractAction {
		/**
		 *
		 */
		public static final long serialVersionUID = -6276019615521905411L;

		public void actionPerformed(ActionEvent e) {

			try {
				client.sendData(inputText.getText());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				area.append(e1.getMessage());
			}
		}
	}

	public void append(String text) {
		area.append(text);
	}

	public static void main(String[] args) {
		SwingChatClient client = new SwingChatClient();
		client.pack();
		client.setVisible(true);
	}
}