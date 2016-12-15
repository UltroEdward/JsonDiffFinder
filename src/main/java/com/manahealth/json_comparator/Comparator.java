package com.manahealth.json_comparator;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.JsonDiff;
import com.github.opendevl.JFlat;

public class Comparator {

	public enum OutputType {
		JSON, CSV
	}

	/***
	 * 
	 * @param args:  args[0] - path of beforeJson, args[1] - after, args[2] - diff file destination, args[3] - output type JSON or CSV
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public static void main(String[] args) throws JsonProcessingException, IOException {

		String beforeFilePath = args[0];
		String afterFilePath = args[1];
		String diffPath = args[2];
		OutputType outputType = OutputType.valueOf(args[3].trim().toUpperCase());

		Comparator comparator = new Comparator();

		JsonNode diff = comparator.getComparedJsons(beforeFilePath, afterFilePath, diffPath);

		switch (outputType) {
		case JSON:
			comparator.writeStringToFile(diffPath, diff.toString());
			break;
		case CSV:
			jsonToCsv(diffPath, diff.toString());
			break;
		}

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

	private static void jsonToCsv(String filePath, String content) throws FileNotFoundException, UnsupportedEncodingException {
		new JFlat(content).json2Sheet().write2csv(filePath, ';');

	}

}