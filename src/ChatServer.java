import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ChatServer {
	
	static HashSet<String> userNames = new HashSet<String>();
	static ArrayList<PrintWriter> printWriters = new ArrayList<PrintWriter>();
	
	public static void main(String[] args) throws Exception {
		System.out.println("Waiting for clients ...");
		ServerSocket ss = new ServerSocket(9806);
		while (true) {
			Socket soc = ss.accept();
			System.out.println("Connection established");
			ConversationHandler handler = new ConversationHandler(soc);
			handler.start();
		} 
	}
}


class ConversationHandler extends Thread {
	Socket socket;
	BufferedReader in;
	PrintWriter out;
    String name;
    
	public ConversationHandler(Socket socket) throws IOException {
		this.socket = socket;
	}
	
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			int count = 0;
			while (true) {
				if (count > 0) {
					out.println("Name already exists!");
				} else {
					out.println("Name required");
				}
				name = in.readLine();
				if (name == null) {
					return;
				}
				if (!ChatServer.userNames.contains(name)) {
					ChatServer.userNames.add(name);
					break;
				}
				count++;
			}
			out.println("Name Accepted!");
			ChatServer.printWriters.add(out);
			
			while (true) {
				String message = in.readLine();
				if (message == null) {
					return;
				}
				
				for(PrintWriter writer : ChatServer.printWriters) {
					writer.println(name + ": " + message);
				}
			}
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}