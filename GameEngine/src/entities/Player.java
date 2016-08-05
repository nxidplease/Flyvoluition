package entities;

import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManger;

public abstract class Player extends Entity{

	protected static final float RUN_SPEED = 20;
	protected static final float TURN_SPEED = 160;
	protected static final float GRAVITY = -50;
	protected static final float JUMP_POWER = 30;
	
	private static final float TERRAIN_HEIGHT = 0;
	
	protected float currentSpeed = 0;
	protected float currentTurnSpeed = 0;
	protected float upwardSpeed = 0;
	
	protected boolean isInAir = false;
	
	public Player(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
	public void move(){
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
	
}
