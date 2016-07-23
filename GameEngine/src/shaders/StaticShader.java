package shaders;

public class StaticShader extends SimpleShader {

	private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";
	
	private int location_useFakeLighting;
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();
		location_useFakeLighting = getUniformLocation("useFakeLighting");
	}
	
	public void loadFakeLighting(boolean useFake){
		loadBoolean(location_useFakeLighting, useFake);
	}
}
