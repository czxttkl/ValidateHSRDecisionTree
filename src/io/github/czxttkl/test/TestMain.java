package io.github.czxttkl.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.github.czxttkl.jsontree.HSRTree;
import io.github.czxttkl.jsontree.InterfaceDeserializer;
import io.github.czxttkl.jsontree.Leaf;
import io.github.czxttkl.jsontree.Node;

public class TestMain {
	public static boolean leftSubTreeValidated = false;
	public static boolean rightSubTreeValidated = false;
	public static boolean qEdgesValidated = false;
	public static boolean kYesValidated = false;
	public static boolean internalNodeValidated = false;
	public static boolean leafValidated = false;
	
	public static void main(String... args) {
		HSRTree hsrTreeRoot = new HSRTree(4);
		HSRTree hsrTreeLeft = new HSRTree(2);
		HSRTree hsrTreeRight = new HSRTree(6);
		
		HSRTree hsrTreeLeftLeft = new HSRTree(1);
		hsrTreeLeftLeft.setLeftNode(new Leaf(0));
		hsrTreeLeftLeft.setRightNode(new Leaf(1));
		
		HSRTree hsrTreeLeftRight = new HSRTree(3);
		hsrTreeLeftRight.setLeftNode(new Leaf(2));
		hsrTreeLeftRight.setRightNode(new Leaf(3));
		
		hsrTreeLeft.setLeftNode(hsrTreeLeftLeft);
		hsrTreeLeft.setRightNode(hsrTreeLeftRight);
		
		hsrTreeRoot.setLeftNode(hsrTreeLeft);
		
		HSRTree hsrTreeRightLeft = new HSRTree(5);
		hsrTreeRightLeft.setLeftNode(new Leaf(4));
		hsrTreeRightLeft.setRightNode(new Leaf(5));
		
		HSRTree hsrTreeRightRight = new HSRTree(7);		
		hsrTreeRightRight.setLeftNode(new Leaf(6));
		hsrTreeRightRight.setRightNode(new Leaf(7));
		
		hsrTreeRight.setLeftNode(hsrTreeRightLeft);
		hsrTreeRight.setRightNode(hsrTreeRightRight);
		
		hsrTreeRoot.setRightNode(hsrTreeRight);
		
		// Create a Gson instance to convert TernaryDecisionTree Java Object into JSON String
		Gson gson = new GsonBuilder().registerTypeAdapter(HSRTree.class, new InterfaceDeserializer()).create();
		String jsonString = gson.toJson(hsrTreeRoot);
		System.out.println(jsonString);
		
		HSRTree root1 = gson.fromJson(jsonString, HSRTree.class);
		
		checkLeftSubtree(root1.getLeftNode(), root1.getN());
	}
	
	public static void checkLeftSubtree(Node object, int n) {
		int max = findLargest(object);
		System.out.println("");
		leftSubTreeValidated = n - max == 1? true : false;
		System.out.println("Check if the root is one larger than the largest node in the left subtree: " + (leftSubTreeValidated?"yes":"no") );
		System.out.println("The root is " + n + ". The largest node in the left subtree is " + max);
	}
	
	public static int findLargest(Node o) {
		if (o instanceof Leaf)
			return ((Leaf) o).getH();
		else {
			return Math.max(findLargest(((HSRTree)o).getLeftNode()), findLargest(((HSRTree)o).getRightNode()));
		}
	}
	
	
}