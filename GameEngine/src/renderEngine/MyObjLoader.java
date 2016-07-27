package renderEngine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import models.RawModel;

import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class MyObjLoader {
	public static RawModel loadObjModel(String filename, Loader loader) {
		FileReader fr = null;
		
		try {
			fr = new FileReader("res/" + filename + ".obj");
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't load file " + filename + ".obj");
			e.printStackTrace();
		}
		
		BufferedReader reader = new BufferedReader(fr);
		String line = "";
		
		// Stores data read from obj file in the order it appears
		List<Vector3f> vertices = new ArrayList<>();
		List<Vector2f> uvCoords = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<Integer> indices = new ArrayList<>();
		
		// Stores the data ordered by indices
		// data of same index in all arrays belongs to the same vertex
		float[] verticesArray = null;
		float[] normalsArray = null;
		float[] uvCoordsArray = null;
		int[] indicesArray = null;
		
		try {
			
			boolean done = false;
			
			// Read All data until got to indexing data
			while(!done){
				line = reader.readLine();
				
				// Split line by space to get every component of the vector
				// separately
				String[] currLine = line.split(" ");
				
				// If we read a vertex/uv coord/normal we add it to their
				// corresponding lists
				if(line.startsWith("v ")) {
					Vector3f vertex = new Vector3f(Float.parseFloat(currLine[1]), 
												   Float.parseFloat(currLine[2]), 
												   Float.parseFloat(currLine[3]));
					vertices.add(vertex);
				} else if(line.startsWith("vt ")){
					Vector2f uvCoord = new Vector2f(Float.parseFloat(currLine[1]), 
							   Float.parseFloat(currLine[2]));
					uvCoords.add(uvCoord);
					
				} else if(line.startsWith("vn ")){
					Vector3f normal = new Vector3f(Float.parseFloat(currLine[1]), 
							   Float.parseFloat(currLine[2]), 
							   Float.parseFloat(currLine[3]));
					normals.add(normal);
					
				// If we read a "face" we organize
				// the uvCoord and the normal in their correct position
				// in the corresponding arrays
				} else if(line.startsWith("f ")){
					
					// If arrays are yet to be initialized
					if(normalsArray == null){
						normalsArray = new float[vertices.size() * 3];
						uvCoordsArray = new float[vertices.size() * 2];
					}
					
					// Going through all the vertices in the current face
					for(int i = 1; i <= 3; i++){
						String[] currVertexData = currLine[i].split("/");
						proccessVertex(currVertexData, indices, uvCoords, 
									   normals, uvCoordsArray, normalsArray);
					}
					
					
					
				}
			}
		} catch (Exception e) {
			System.out.println("gayzer");
		}
		
		// Converting vertices vectors to float arrays, and indices list to array 
		verticesArray = new float[vertices.size() * 3];
		indicesArray = new int[indices.size()];
		
		for (int i=0; i < vertices.size(); i++) {
			putVectorfInArray(verticesArray, i, vertices.get(i));
		}
		
		for (int i=0; i < indices.size(); i++) {
			indicesArray[i] = indices.get(i);
		}
		
		return loader.loadToVAO(verticesArray, uvCoordsArray, normalsArray, indicesArray);
	}
	
	private static <T extends Vector> void putVectorfInArray(float[] array, int index,  T vec){
		int vecDimensions = 0;
		float[] vecComponents = null;
		
		if (vec instanceof Vector3f){
			// 3 Dimensional
			vecDimensions = 3;
			vecComponents = new float[vecDimensions];
			vecComponents[0] = ((Vector3f)vec).x;
			vecComponents[1] = ((Vector3f)vec).y;
			vecComponents[2] = ((Vector3f)vec).z;
		} else {
			// 2 Dimensional assumed
			vecDimensions = 2;
			vecComponents = new float[vecDimensions];
			vecComponents[0] = ((Vector2f)vec).x;
			vecComponents[1] = 1 - ((Vector2f)vec).y;
		}
		
		for(int i = 0; i < vecDimensions; i++){
			array[index * vecDimensions + i] = vecComponents[i];
		}
	}
	
	/**
	 * Takes in the pointers to vertex, uvCoord and normal indexes
	 * and puts the data of uvCoords and normals in their correct positions
	 * so they are aligned with the order of appearance of vertices
	 * @param vertexData pointers to vertex, uvCoord, and normal indices
	 * @param indices A list that keeps the order of appearance of the vertices
	 * @param uvCoords A list of uvCoords in the order of appearance in the obj file
	 * @param normals A list of normals in the order of appearance in the obj file
	 * @param uvCoordArray The array of uvCoords ordered by the appearance of vertices
	 * @param normalsArray The array of normals ordered by the appearance of vertices
	 */
	private static void proccessVertex(String[] vertexData, List<Integer> indices,
									   List<Vector2f> uvCoords, List<Vector3f> normals, 
									   float[] uvCoordArray, float[] normalsArray){
		int currentVertexIndex = Integer.parseInt(vertexData[0]) - 1;
		int uvCoordIndex = Integer.parseInt(vertexData[1]) - 1;
		int normalIndex = Integer.parseInt(vertexData[2]) - 1;
		
		indices.add(currentVertexIndex);
		Vector2f currUvCoord = uvCoords.get(uvCoordIndex);
		Vector3f currNorm = normals.get(normalIndex);
		
		putVectorfInArray(uvCoordArray, currentVertexIndex, currUvCoord);
		putVectorfInArray(normalsArray, currentVertexIndex, currNorm);
	}
}
