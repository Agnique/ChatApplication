import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;

public class ChatClient {
	
	static JFrame chatWindow = new JFrame("Chat Application");
	static JTextArea chatArea = new JTextArea(22, 40);
	static JTextField textField = new JTextField(40);
	static JLabel blankLabel = new JLabel("       ");
	static JButton sendButton = new JButton("Send");
	static BufferedReader in;
	static PrintWriter out;
	
	
    public ChatClient() {
    	
    	chatWindow.setLayout(new FlowLayout());
    	chatWindow.add(new JScrollPane(chatArea));
    	chatWindow.add(blankLabel);
    	chatWindow.add(textField);
    	chatWindow.add(sendButton);
    	
    	chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	chatWindow.setSize(475, 500);
    	chatWindow.setVisible(true);
    	textField.setEditable(false);
    	chatArea.setEditable(false);
    	
    	sendButton.addActionListener(new Listener());
    	textField.addActionListener(new Listener());
    	
    }
    
    void startChat() throws Exception {
    	String ipAddress = JOptionPane.showInputDialog(
    			chatWindow,
    			"Enter IP Address",
    			"IP Address Required!",
    			JOptionPane.PLAIN_MESSAGE);
    	Socket soc = new Socket(ipAddress, 9806);
    	in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
    	out = new PrintWriter(soc.getOutputStream(), true);
    	
    	while (true) {
    		String str = in.readLine();
    		if (str.equals("Name required")) {
    			String name = JOptionPane.showInputDialog(
    					chatWindow,
    					"Enter a unique name:",
    					"Name required!",
    					JOptionPane.PLAIN_MESSAGE);
    			out.println(name);
    			
    		} else if (str.equals("Name already exists!")) {
    			String name = JOptionPane.showInputDialog(
    					chatWindow,
    					"Enter another name:",
    					"Name already exists!",
    					JOptionPane.WARNING_MESSAGE);
    			out.println(name);
    		} else if (str.equals("Name Accepted!")) {
    			textField.setEditable(true);
    			break;
    		} 
    	}
    	
    	while (true) {
    		String str = in.readLine();
    		chatArea.append(str + "\n");
    	}
    }
    
   
   
	public static void main(String[] args) throws Exception{
    	ChatClient client = new ChatClient();
    	client.startChat();
    }
}

class Listener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		ChatClient.out.println(ChatClient.textField.getText());
		ChatClient.textField.setText("");
	}
}


