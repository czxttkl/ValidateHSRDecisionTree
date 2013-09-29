package io.github.czxttkl.jsontree;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.google.gson.Gson;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.parser.JSONParser;


public class MyJsonParser {

	public static Node parseJsonToTree(String s) {
		Gson gson = new Gson();
		Node treeNode = gson.fromJson(s, Node.class);
		return treeNode;
	}
	
	public static String parseTreeToJson(Root root) {
		Gson gson = new Gson();
		String s = gson.toJson(root);
		return s;
	}
	
	
	
	
}
