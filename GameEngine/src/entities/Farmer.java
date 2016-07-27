
package entities;

import models.TexturedModel;

import java.util.Random;

import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector3f;



public class Farmer extends Entity{

	
	private Random rand = new Random();
	private float startRotY =rand.nextFloat()*360-180f;
	private Vector3f startPosition = new Vector3f(rand.nextFloat()*800,0,rand.nextFloat()*800);
	

	

	public Farmer(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		super.setPosition(startPosition);
		super.setRotY(startRotY);
			
	}




	public void move(){
		
		
	float dx,dz;
	
	if(Math.random()<0.01)
		this.increaseRotation(0, rand.nextFloat()*360-180f, 0);
		
		
	dx = 0.1f*(float) (Math.sin(Math.toRadians(getRotY())));
	dz = 0.1f*(float) (Math.cos(Math.toRadians(getRotY())));
	this.increasePosition(dx, 0, dz);
	
	}

	

	
}
