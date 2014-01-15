package io.github.czxttkl.jsontree;

import java.lang.reflect.Type;
import java.util.Iterator;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class InterfaceDeserializer implements JsonDeserializer<Node> {

	@Override
	public Node deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		if (json.isJsonObject()) {
			JsonObject jsonObject = json.getAsJsonObject();

			if (jsonObject.get("rung") != null) {
				int rung = jsonObject.get("rung").getAsInt();
				HSRTree tree = new HSRTree(rung);
				Node leftTree;
				Node rightTree;
				leftTree = context.deserialize(jsonObject.get("breakNode"), HSRTree.class);
				rightTree = context.deserialize(jsonObject.get("surviveNode"), HSRTree.class);
				tree.setLeftNode(leftTree);
				tree.setRightNode(rightTree);
				return tree;
			} else {
				if (jsonObject.get("h") != null) {
					int h = jsonObject.get("h").getAsInt();
					Leaf lf = new Leaf(h);
					return lf;
				}
			}
		}

		return null;
	}

	private int[] jsonArrayToIntArray(JsonElement je) {
		JsonArray ja = je.getAsJsonArray();
		Iterator<JsonElement> iterator = ja.iterator();
		int[] balls = new int[ja.size()];
		for (int i = 0; i < ja.size(); i++) {
			balls[i] = iterator.next().getAsInt();
		}
		return balls;
	}
}
