package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.HashMap;

public class GameServer implements Runnable{

	private DatagramSocket socket;
	private byte[] receiveData = new byte[1024];
	private byte[] sendData = new byte[1024];
	private boolean running = false;

	private HashMap<String, SocketAddress> userToSocketMap = new HashMap<>();

	public GameServer() throws SocketException{
		socket = new DatagramSocket(1422);
	}

	@Override
	public void run() {

		running = true;

		while(running){
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try {
				socket.receive(receivePacket);
				SocketAddress remoteAddress = receivePacket.getSocketAddress();
				byte[] data = receivePacket.getData();
				String msg = new String(data);
				String[] dataArr = msg.split(",");
				String username = dataArr[0];

				if(!userToSocketMap.containsKey(username)){
					userToSocketMap.put(username, remoteAddress);
				}

				updateClients(data, username);
				Arrays.fill(receiveData, (byte)0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void updateClients(byte[] data, String username){
		
			Arrays.fill(sendData, (byte) 0);
			sendData = Arrays.copyOf(data, data.length);
			
			SocketAddress dest = null;
			
			for (String user : userToSocketMap.keySet()) {
				// If the current user is not the user that sent the packet
				// we need to notidy him and so the dest address is his.
				if(!user.equals(username)){
					dest = userToSocketMap.get(user);
					
					DatagramPacket sendPacket = new DatagramPacket(sendData,
							sendData.length, dest);
					try {
						socket.send(sendPacket);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
	}

	public void stop(){
		running = false;
	}
}
