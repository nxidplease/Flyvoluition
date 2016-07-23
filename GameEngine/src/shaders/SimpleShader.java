package shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import toolbox.Maths;
import entities.Camera;
import entities.Light;

public class SimpleShader extends ShaderProgram {

	protected int location_transformationMatrix;
	protected int location_projectionMatrix;
	protected int location_viewMatrix;
	protected int location_lightPosition;
	protected int location_lightColor;
	protected int location_shineDamper;
	protected int location_reflectivity;
	protected int location_skyColor;
	
	public SimpleShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
		bindAttribute(1, "textureCoords");
		bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = getUniformLocation("transformationMatrix");
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_lightPosition = getUniformLocation("lightPosition");
		location_lightColor = getUniformLocation("lightColor");
		location_shineDamper = getUniformLocation("shineDamper");
		location_reflectivity = getUniformLocation("reflectivity");
		location_skyColor = getUniformLocation("skyColor");
	}
	
	public void loadSkyColor(float r, float b, float g){
		loadVector(location_skyColor, new Vector3f(r, g, b));
	}
	
	public void loadShineVariables(float damper, float reflectivity){
		loadFloat(location_shineDamper, damper);
		loadFloat(location_reflectivity, reflectivity);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadLight(Light light){
		loadVector(location_lightColor, light.getColor());
		loadVector(location_lightPosition, light.getPosition());
	}
	
	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(location_projectionMatrix, projection);
	}

	public void loadViewMatrix(Camera camera) {
		loadMatrix(location_viewMatrix, Maths.createViewMatrix(camera));
	}
}
