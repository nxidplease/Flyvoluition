package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManger;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.MyObjLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import entities.Farmer;

public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManger.createDisplay();

		Loader loader = new Loader();

		//RawModel model = HisObjLoader.loadOBJ("MyGayzerModel", loader);
		RawModel model = MyObjLoader.loadObjModel("dragon", loader);
		//model = HisOldObjLoader.loadObjModel("MyGayzerModel", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("image"));
		TexturedModel staticModel = new TexturedModel(model, texture);
		texture.setShineDamper(10);
		texture.setReflectivity(1);

		ModelTexture grass = new ModelTexture(loader.loadTexture("grassTexture"));
		grass.setHasTransparency(true);
		grass.setUseFakeLighting(true);
		ModelTexture fern = new ModelTexture(loader.loadTexture("fern"));
		fern.setHasTransparency(true);
		RawModel grassModel = MyObjLoader.loadObjModel("grassModel", loader);
		RawModel fernModel = MyObjLoader.loadObjModel("fern", loader);
		TexturedModel grassTexturedModel = new TexturedModel(grassModel, grass);
		TexturedModel fernTexturedModel = new TexturedModel(fernModel, fern);

		Entity entity = new Entity(staticModel, new Vector3f(0, 0, -5), 0, 0, 0, 1);
		Light light = new Light(new Vector3f(1, 1, 1), new Vector3f(400, 600, 400));

		// Terrain Texture Pack Creation
		TerrainTexture backTexture = new TerrainTexture(loader.loadTexture("Dirt"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("grassBack"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("Snow"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("Ice"));

		TerrainTexturePack textPack = new TerrainTexturePack(backTexture, rTexture, gTexture, bTexture);

		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));



		Terrain terrain = new Terrain(0, 0, loader, textPack, blendMap);
		Terrain terrain1 = new Terrain(1, 0, loader, textPack, blendMap);
		RawModel bunny = MyObjLoader.loadObjModel("stanfordBunny", loader);
		TexturedModel bunnyModel = new TexturedModel(bunny, new ModelTexture(loader.loadTexture("Snow")));

		Player player = new Player(bunnyModel, new Vector3f(0, 0, 0),0 ,0 , 0, 1);
		Camera camera = new Camera(player);



		List<Entity> entities = new ArrayList<>();
		Random rnd = new Random();

		for(int i = 0; i < 200; i++){

			entities.add(new Entity(grassTexturedModel, new Vector3f(rnd.nextFloat() * 400,0, rnd.nextFloat() * 400), 0, 0, 0, 3));
			entities.add(new Entity(fernTexturedModel, new Vector3f(rnd.nextFloat() * 400,0,rnd.nextFloat() * 400), 0, 0, 0, 1));
		}

		MasterRenderer renderer = new MasterRenderer();



		//*********FARMERS**************
		RawModel personModel = MyObjLoader.loadObjModel("Fly", loader);
		TexturedModel texturedPersonModel = new TexturedModel(personModel, new ModelTexture(loader.loadTexture("Fly")));


		List<Farmer> farmers = new ArrayList<>();
		for(int i = 0; i < 200; i++)
			farmers.add(new Farmer(texturedPersonModel, new Vector3f(), 0, 0, 0, 5));

		//*********END FARMERS************************


		//**********TREES****************************			
		RawModel treeModel = MyObjLoader.loadObjModel("treeModel", loader);
		ModelTexture tree = new ModelTexture(loader.loadTexture("tree"));
		TexturedModel treeTexturedModel = new TexturedModel(treeModel, tree);

		List<Entity> trees = new ArrayList<>();

		for(int i = 0; i < 200; i++){
			trees.add(  new Entity(treeTexturedModel, new Vector3f(rnd.nextFloat() * 800,0,rnd.nextFloat() * 800), 0, 0, 0, (rnd.nextInt(4)+10))    );
		}	



		//**********END TREES****************************					




		while(!Display.isCloseRequested()) {
			//entity.increasePosition(0, 0, -0.1f);
			entity.increaseRotation(0.5f, 0.5f, 0);
			//entity1.increaseRotation(0, 1, 0);
			camera.move();
			player.move();
			renderer.proccessEntity(player);
			renderer.proccessTerrain(terrain);
			renderer.proccessTerrain(terrain1);


			for (Entity entity2 : entities) {
				renderer.proccessEntity(entity2);
			}
			renderer.render(light, camera);

			for (Farmer farmer : farmers) {
				renderer.proccessEntity(farmer);
				farmer.move();
			}

			for (Entity aTree : trees) {
				renderer.proccessEntity(aTree);
			}


			// game logic
			DisplayManger.updateDisplay();


		}

		renderer.cleanUp();
		loader.cleanUp();
		DisplayManger.closeDisplay();


	}

}
