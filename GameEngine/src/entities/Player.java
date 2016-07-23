package entities;

import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManger;

public class Player extends Entity{

	private static final float RUN_SPEED = 20;
	private static final float TURN_SPEED = 160;
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 30;
	
	private static final float TERRAIN_HEIGHT = 0;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardSpeed = 0;
	
	private boolean isInAir = false;
	
	public Player(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
	public void move(){
		checkInputs();
		increaseRotation(0, currentTurnSpeed * DisplayManger.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManger.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(getRotY())));
		increasePosition(dx, 0, dz);
		upwardSpeed += GRAVITY * DisplayManger.getFrameTimeSeconds();
		increasePosition(0, upwardSpeed * DisplayManger.getFrameTimeSeconds(), 0);
		
		if(getPosition().y < TERRAIN_HEIGHT){
			upwardSpeed = 0;
			isInAir = false;
			getPosition().y = TERRAIN_HEIGHT;
		}
	}
	
	private void jump(){
		if(!isInAir){
			this.upwardSpeed = JUMP_POWER;
			isInAir = true;
		}
	}

	private void checkInputs(){
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			this.currentSpeed = RUN_SPEED;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			this.currentSpeed = -RUN_SPEED;
		}
		else{
			this.currentSpeed = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			this.currentTurnSpeed = -TURN_SPEED;
		}
		else if(Keyboard.isKeyDown( Keyboard.KEY_A)){
			this.currentTurnSpeed = TURN_SPEED;
		}
		else {
			this.currentTurnSpeed = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			jump();
		}
	}
	
}
