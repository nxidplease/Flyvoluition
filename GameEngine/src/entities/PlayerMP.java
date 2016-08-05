package entities;

import models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

public class PlayerMP extends Player {

	private String username;
	
	public PlayerMP(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
