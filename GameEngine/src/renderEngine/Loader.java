package renderEngine;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import models.RawModel;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Loader {
	
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();
	private float[] prevPos = null;
	private float[] prevTex = null;
	private float[] prevNorm = null;
	private int[] prevIndex = null;
	
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		//initAndComparePrevData(positions, textureCoords, normals, indices);
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		
		return new RawModel(vaoID, indices.length);
		
	}
	
	private void initAndComparePrevData(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		if(prevPos == null){
			prevPos = new float[positions.length];
			prevTex = new float[textureCoords.length];
			prevNorm = new float[normals.length];
			prevIndex = new int[indices.length];
			System.arraycopy(positions, 0, prevPos, 0, positions.length);
			System.arraycopy(textureCoords, 0, prevTex, 0, textureCoords.length);
			System.arraycopy(normals, 0, prevNorm, 0, normals.length);
			System.arraycopy(indices, 0, prevIndex, 0, indices.length);
		}
		else{
			if(!Arrays.equals(prevPos, positions)){
				System.out.println("Positions aren't equal");
				compareArrays(prevPos, positions);
			}
				
			
			if(!Arrays.equals(prevTex, textureCoords)){
				System.out.println("textCoords aren't equal");
				compareArrays(prevTex, textureCoords);
			}
				
			if(!Arrays.equals(prevNorm, normals)){
				System.out.println("normals aren't equal");
				compareArrays(prevNorm, normals);
			}
				
			if(!Arrays.equals(prevIndex, indices)){
				System.out.println("indices aren't equal");
				compareArrays(prevIndex, indices);
			}
				
		}
		
		
	}
	
	private void compareArrays(float[] array1, float[] array2){
		for (int i = 0; i < array1.length; i++) {
			if(array1[i] != array2[i]){
				System.out.println("Arrayas aren't equal at " + i);
				break;
			}
		}
	}
	
	private void compareArrays(int[] array1, int[] array2){
		for (int i = 0; i < array1.length; i++) {
			if(array1[i] != array2[i]){
				System.out.println("Arrayas aren't equal at " + i);
				break;
			}
		}
	}
	
	public int loadTexture(String fileName) {
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/" + fileName +".png"));
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.4f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int textureID = texture.getTextureID();
		textures.add(textureID);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		return textureID;
	}
	
	public void cleanUp() {
		
		for(int vao:vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		
		for (int vbo : vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		
		for (Integer textID : textures) {
			GL11.glDeleteTextures(textID);
		}
	}
	
	private int createVAO() {
		
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		
		
		return vaoID;
		
	}
	
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
	}
	
	private void unbindVAO() {
		
		GL30.glBindVertexArray(0);
		
	}
	
	private void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	} 
	
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		return buffer;
		
	} 
}
