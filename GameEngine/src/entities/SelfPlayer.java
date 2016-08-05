package entities;

import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import engineTester.MainGameLoop;

public class SelfPlayer extends Player {

	public SelfPlayer(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}

	public void move(){
		checkInputs();
		super.move();
		UpdateServer();
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
			if(!isInAir){
				this.upwardSpeed = JUMP_POWER;
				isInAir = true;
			}
		}
	}
	
	private void UpdateServer(){
		
		if(this.currentSpeed != 0 || this.currentTurnSpeed != 0 || this.upwardSpeed != 0){
			MainGameLoop.client.sendUpdateToServer(getPosition(), getRotX(), getRotY(), getRotZ());
		}
		
	}
}
