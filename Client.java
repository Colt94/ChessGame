package chessGame;

import java.io.*;
import java.net.*;



public class Client {

	private String userName;
	private String ipAddress;
	private int port;
	public Socket sock = null;
	private DataInputStream in;
	private DataOutputStream out;
	int color;
	
	public RemoteBoard board;
	
	public void setColor(int color) {
		this.color = color;
	}
	
	public Client(String userName, String ipAddress, int port) {
		this.userName = userName;
		this.ipAddress = ipAddress;
		this.port = port;
		//board = new RemoteBoard(color);
	}
	
	public void run() throws IOException {
		
		sock = new Socket(ipAddress, port);
		
		in = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
		out = new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));
			
	}
	
	public int dataAvailable() throws IOException{
		
		return in.available();
	}
	
	public String ClientRead() {
		try {
			return in.readUTF();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	public void ClientWrite(String move) {
		try {
			out.writeUTF(move);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

public static void main(String[] args) {
		
		String test = "";
		Client c1 = new Client("Client1", "127.0.0.1", 111);
		//Client c2 = new Client("Client2", "127.0.0.1", 111);
		try {
			c1.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(!test.equals("quit")) {
			System.out.print("Client reading..\n");
			test = c1.ClientRead();

			System.out.print(test);
			System.out.print("\n");
		}
		c1.close();
		
	}
	
}
