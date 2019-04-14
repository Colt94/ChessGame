package chessGame;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class Server {

	public ServerSocket srvSock = null;
	private Socket sock1, sock2 = null;
	
	private int port;
	int client1, client2;
	
	private boolean twoClients;
	
	public DataInputStream in1, in2 = null;
	public DataOutputStream out1, out2 = null;

	String move1, move2;
	
	
	public Server(int port, int PlayerCount) {
		
		this.port = port;
		
		if (PlayerCount == 2) {
			twoClients = true;
		}
		else {
			twoClients = false;
		}
	}
	
	public void run() throws IOException {
		
		srvSock = new ServerSocket(port);
		
		if (twoClients) {
			System.out.print("Waiting for two connections...\n");
			Socket sock1 = srvSock.accept();
			System.out.print("Client 1 connected\n");
			
			
			in1 = new DataInputStream(new BufferedInputStream(sock1.getInputStream()));
			out1 = new DataOutputStream(new BufferedOutputStream(sock1.getOutputStream()));
			
			//write player color to connected client1
			out1.writeUTF("0");
			out1.flush();
			
			Socket sock2 = srvSock.accept();
			System.out.print("Client 2 connected\n");
		
			in2 = new DataInputStream(new BufferedInputStream(sock2.getInputStream()));
			out2 = new DataOutputStream(new BufferedOutputStream(sock2.getOutputStream()));
			
			//write player color to connected client2
			out2.writeUTF("1");
			out2.flush();
			
		}
		else {
			System.out.print("Waiting for client connection...\n");
			Socket sock1 = srvSock.accept();
			System.out.print("Client connected\n");
			
			in1 = new DataInputStream(new BufferedInputStream(sock1.getInputStream()));
			out1 = new DataOutputStream(new BufferedOutputStream(sock1.getOutputStream()));
		}
		
		out1.writeUTF("WELCOM");
		out2.writeUTF("WELCOME");
		out1.flush();
		out2.flush();
		System.out.print("WELCOME\n");
		
	}
	/*
	public void ServerHandler() throws IOException {
		
		String comfirm = "";
		while(true) {
		
			int data1 = in1.available();
			int data2 = in2.available();
			if(data1 != 0) {
				//String[] values = CSV.split(",");
				System.out.print("Data read from client1\n");
				String move1 = in1.readUTF();
				out2.writeUTF(move1); //send move to other client
				out2.flush();
				System.out.print("Client1 move sent to client2\n");
				
				String c = in2.readUTF();
			
				System.out.print("Client2 comfirmed\n");
				
				out1.writeUTF("ok"); //send ok back to calling client
				out1.flush();
				System.out.print("Sent ok to client1\n");
			}
			else if (data2 != 0) {
				//String[] values = CSV.split(",");
				String move2 = in2.readUTF();
				System.out.printf("Data read %s from client2\n", move2);
				out1.writeUTF(move2);
				out1.flush();
				System.out.printf("%s sent to client1\n", move2);
				
				
				String c = in1.readUTF();
				
				System.out.print("Client1 comfirmed\n");
				
				out2.writeUTF("ok");
				out2.flush();
				System.out.print("Sent ok to Client2");
			}
		}
	}
	*/
	
	public void ServerRead(int client) throws IOException {
	
		if(client == 1) {
		
			move1 = in1.readUTF();
			
		}
		else {
			move2 = in2.readUTF();
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
