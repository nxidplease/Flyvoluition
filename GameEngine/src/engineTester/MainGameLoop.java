package engineTester;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import models.RawModel;
import models.TexturedModel;
import net.GameClient;
import net.GameServer;

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
import entities.PlayerMP;
import entities.SelfPlayer;

public class MainGameLoop {

	public static GameClient client;
	
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

		Player player = new SelfPlayer(bunnyModel, new Vector3f(0, 0, 0),0 ,0 , 0, 1);
		PlayerMP mpPlayer = new PlayerMP(bunnyModel, new Vector3f(2, 0, 4),0 ,0 , 0, 1);
		Camera camera = new Camera(player);
		
		GameServer server = null;
		
		String username = JOptionPane.showInputDialog("Enter usernanme");
		
		int ans = JOptionPane.showOptionDialog(null, "Would you like to run the server?", "Serer", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, 
									 null, null, null);
		
		if(ans == JOptionPane.YES_OPTION){
			try {
				server = new GameServer();
				Thread serverThread = new Thread(server);
				serverThread.start();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			client = new GameClient("localhost", 1422, mpPlayer, username);
			Thread clientThread = new Thread(client);
			clientThread.start();
		} catch (SocketException | UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 


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
			renderer.proccessEntity(mpPlayer);
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
		client.stop();
		
		if (server != null) {
			server.stop();
		}
		
		DisplayManger.closeDisplay();


	}

}
