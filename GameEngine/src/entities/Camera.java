package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	private Vector3f position = new Vector3f(0, 14, 50);
	private float pitch = 20;
	private float yaw;
	private float roll;
	private float speedMulti = 1;
	
	private Player player;

	public Camera(Player player) {
		this.player = player;
		Mouse.setGrabbed(true);
	}
	
	public void move() {
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
		/*if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
			speedMulti = 5;
		}
		else{
			speedMulti = 1;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			//position.z-=0.02f;
			position.z -= Math.cos(Math.toRadians(yaw)) * speedMulti;
			position.x += Math.sin(Math.toRadians(yaw)) * speedMulti;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			position.z += Math.cos(Math.toRadians(yaw)) * speedMulti;
			position.x -= Math.sin(Math.toRadians(yaw)) * speedMulti;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			position.z += Math.cos(Math.toRadians(90 - yaw)) * speedMulti;
			position.x += Math.sin(Math.toRadians(90 - yaw)) * speedMulti;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			//position.x-=0.02f;
			position.z -= Math.cos(Math.toRadians(90 - yaw)) * speedMulti;
			position.x -= Math.sin(Math.toRadians(90 - yaw)) * speedMulti;
			
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
			yaw -= 1;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
			yaw += 1;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			position.y += 0.5f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
			position.y -= 0.5f;
		}
		
		int mouseDy = Mouse.getDY();
		
		if(mouseDy != 0){
			System.out.println("Moved by " + mouseDy);
			pitch -= mouseDy * 0.1;
		}
		
		int mouseDx = Mouse.getDX();
		
		if(mouseDx != 0){
			System.out.println("Moved by " + mouseDx);
			yaw += mouseDx * 0.1;
			System.out.println("Curr Yaw: " + yaw + " Yaw Vector: z: " + Math.cos(Math.toRadians(yaw)) + " x: " + Math.sin(Math.toRadians(yaw)));
		}*/
		
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	private void calculateCameraPosition(float horizDistance, float verticalDistance){
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float)(horizDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float)(horizDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + verticalDistance;
				
	}
	
	private float calculateHorizontalDistance(){
		return (float)(distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance(){
		return (float)(distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
	}
	
	private void calculatePitch() {
		if(Mouse.isButtonDown(1)){
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
		}
	}
	
	private void calculateAngleAroundPlayer() {
		if(Mouse.isButtonDown(0)){
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}
	
	
}
