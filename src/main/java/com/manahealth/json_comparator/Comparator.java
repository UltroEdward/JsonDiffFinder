package com.manahealth.json_comparator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.JsonDiff;

public class Comparator {

	public static void main(String[] args) throws JsonProcessingException, IOException {

		String beforeFilePath = args[0];
		String afterFilePath = args[1];
		String diffPath = args[2];

		Comparator comparator = new Comparator();

		JsonNode diff = comparator.getComparedJsons(beforeFilePath, afterFilePath, diffPath);
		comparator.writeStringToFile(diffPath, diff.toString());
	}

	private JsonNode getComparedJsons(String beforeFilePath, String afterFilePath, String diffPath) {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode beforeNode = null;
		JsonNode afterNode = null;

		try {
			beforeNode = objectMapper.readTree(new String(Files.readAllBytes(Paths.get(beforeFilePath))));
			afterNode = objectMapper.readTree(new String(Files.readAllBytes(Paths.get(afterFilePath))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		JsonNode diff = JsonDiff.asJson(beforeNode, afterNode);
		return diff;

	}

	private void writeStringToFile(String filePath, String content) {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(filePath));
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}