package chessGame;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class Server {

	private ServerSocket srvSock;
	private Socket sock1, sock2;
	
	private int port;
	int client1, client2;
	
	private boolean twoClients;
	
	private DataInputStream in1, in2 = null;
	private DataOutputStream out1, out2 = null;

	String move1, move2;
	Board board;
	
	
	public Server(int port, int PlayerCount) {
		
		this.port = port;
		
		if (PlayerCount == 2) {
			twoClients = true;
		}
		else {
			twoClients = false;
		}
		
		board = new Board();
	}
	
	public void run() throws IOException {
		
		srvSock = new ServerSocket(port);
		
		if (twoClients) {
			System.out.print("Waiting for two connections...\n");
			Socket sock1 = srvSock.accept();
			System.out.print("Client 1 connected\n");
			Socket sock2 = srvSock.accept();
			System.out.print("Client 2 connected\n");
			
			in1 = new DataInputStream(new BufferedInputStream(sock1.getInputStream()));
			out1 = new DataOutputStream(new BufferedOutputStream(sock1.getOutputStream()));
			
			in2 = new DataInputStream(new BufferedInputStream(sock2.getInputStream()));
			out2 = new DataOutputStream(new BufferedOutputStream(sock2.getOutputStream()));
			
		}
		else {
			System.out.print("Waiting for client connection...\n");
			Socket sock1 = srvSock.accept();
			System.out.print("Client connected\n");
			
			in1 = new DataInputStream(new BufferedInputStream(sock1.getInputStream()));
			out1 = new DataOutputStream(new BufferedOutputStream(sock1.getOutputStream()));
		}
		
		System.out.print("WELCOME");
		
		//board = new Board();
		
	}
	
	
	public void ServerRead(int client) {
		
		if(client == 1) {
			try {
				move1 = in1.readUTF();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			try {
				move2 = in2.readUTF();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void ServerWrite(int client, String move) {
		
		if(client == 1) {
			try {
				out1.writeUTF(move);
				out1.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			try {
				out2.writeUTF(move);
				out2.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void close() {
		try {
			srvSock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		
		Server srv = new Server(111, 2);
		try {
			srv.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		srv.ServerWrite(1, "Test1");
		srv.ServerWrite(2, "Test2");
		
		srv.ServerWrite(1,  "More tests1");
		srv.ServerWrite(2,  "More tests2");
		
		srv.ServerWrite(1,  "quit");
		srv.ServerWrite(2,  "quit");
	
		
	}
	
}
