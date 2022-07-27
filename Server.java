package socket_programming;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;	
import java.util.concurrent.Executors;
public class Server {
	int port;
	ServerSocket server= null;
	Socket client = null;
	ExecutorService pool = null;
	int clientcount=0;
	public static void main(String[] args) throws IOException {
		Server serverobj = new Server(5000);
		serverobj.startServer();
	}
	
	Server(int port)
	{
		this.port=port;
		pool = Executors.newFixedThreadPool(5);
	}
	
	public void startServer() throws IOException
	{
		server = new ServerSocket(5000);
		System.out.println("Server is Started");
		System.out.println("");
		while(true)
		{
			client = server.accept();
			clientcount++;
			ServerThread runobj = new ServerThread(client,clientcount,this);
			pool.execute(runobj);
		}
	}
	
	public static class ServerThread implements Runnable 
	{
		
		Server server = null;
		Socket client;
		BufferedReader cin;
		PrintStream cout;
		Scanner sc = new Scanner(System.in);
		int id;
		String s;
		
		ServerThread(Socket client,int count,Server server) throws IOException
		{
			this.client = client;
			this.server = server;
			this.id = count;
			System.out.println("Connection " +id+"established with client " +client);
			
			cin = new BufferedReader(new InputStreamReader(client.getInputStream()));
			cout = new PrintStream(client.getOutputStream());
			
		}
		
		@Override
		public void run()
		{
			int x=1;
			try {
				while(true)
				{
					s=cin.readLine();
					System.out.print("Client("+id+") :"+s+"\n");
					System.out.print("Server : ");
					s= sc.nextLine();
					if(s.equalsIgnoreCase("bye"))
					{
						cout.println("BYE");
						x=0;
						System.out.println("Connection breaked by the Server");
						break;
					}
					cout.println(s);
					
					
				}
				cin.close();
				client.close();
				cout.close();
				if(x==0)
				{
					System.out.println("Server cleaning up.");
					System.exit(0);
				}
			}
			catch(IOException ex)
			{
				System.out.println("Error : "+ex);
			}
		}		
	}
}