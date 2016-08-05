package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.lwjgl.util.vector.Vector3f;

import entities.PlayerMP;

/**
 * This class will send update regarding the local SelfPlayer, and receive
 * update from server about PlayerMP.
 * 
 *
 */
public class GameClient implements Runnable{

	private DatagramSocket socket;
	private byte[] receiveData = new byte[1024];
	private byte[] sendData = new byte[1024];
	private InetAddress serverAddress;
	private int serverPort;
	private PlayerMP remote;
	private boolean running = false;
	private String username;


	public GameClient(String host, int port, PlayerMP remotePlayer, String localUserName) throws SocketException, UnknownHostException{
		socket = new DatagramSocket();
		serverAddress = InetAddress.getByName(host);
		serverPort = port;
		remote = remotePlayer;
		username = localUserName;
	}

	@Override
	public void run() {

		running = true;

		while(running){
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try {
				socket.receive(receivePacket);
				String msg = new String(receivePacket.getData());
				String[] data = msg.trim().split(",");
				
				System.out.println("msg: " + msg.trim() + " currUser: " + username);
				
				if (!data[0].equals(username)) {
					remote.setPosition(new Vector3f(Float.parseFloat(data[1]),
							Float.parseFloat(data[2]), Float
									.parseFloat(data[3])));
					remote.setRotX(Float.parseFloat(data[4]));
					remote.setRotY(Float.parseFloat(data[5]));
					remote.setRotZ(Float.parseFloat(data[6]));
				}
				Arrays.fill(receiveData, (byte)0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void sendUpdateToServer(Vector3f position, float rotX, float rotY, float rotZ){
		String message = username + "," + position.getX() + "," + position.getY() + "," + position.getZ() + "," + rotX + "," +
				rotY + "," + rotZ;
		
		sendData = message.getBytes();

		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
		try {
			socket.send(sendPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stop(){
		running = false;
	}
}
