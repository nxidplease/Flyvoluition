package shaders;

public class TerrainShader extends SimpleShader {

	private static final String VERTEX_FILE = "src/shaders/terrainVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/shaders/terrainFragmentShader.txt";
	
	private int location_backgroundTexture;
	private int location_rTexture;
	private int location_gTexture;
	private int location_bTexture;
	private int location_blendMap;
	
	public TerrainShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();
		location_backgroundTexture = getUniformLocation("backgroundTexture");
		location_rTexture = getUniformLocation("rTexture");
		location_gTexture = getUniformLocation("gTexture");
		location_bTexture = getUniformLocation("bTexture");
		location_blendMap = getUniformLocation("blendMap");
	}
	
	public void connectTextureUnits(){
		loadInt(location_backgroundTexture, 0);
		loadInt(location_rTexture, 1);
		loadInt(location_gTexture, 2);
		loadInt(location_bTexture, 3);
		loadInt(location_blendMap, 4);
	}
	
}
