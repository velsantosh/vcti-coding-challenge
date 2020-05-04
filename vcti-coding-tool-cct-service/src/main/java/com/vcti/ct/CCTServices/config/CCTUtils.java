package com.vcti.ct.CCTServices.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CCTUtils {

	public static String writeProgInFile(String prog, String fileName, String userDir) {

		String path = "C:\\takeTest\\" + userDir + "\\";
		Path pathDir = Paths.get(path);

		try {
			if (!Files.exists(pathDir)) {
				Files.createDirectories(pathDir);
				System.out.println("Directory created");
			} else {
				System.out.println("Directory already exists");
			}
			path = path + fileName + ".java";
			// Java 7
			Files.write(Paths.get(path), prog.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;

	}

	public static Map<String, String> compileJavaProgram(String path) {

		Map<String, String> compilationStatus = null;
		System.out.println("***JAVA COMPILE PROGRAM*******");
		try {
			compilationStatus = runProcess(
					"javac -cp \".;C:\\takeTest\\lib\\hamcrest-core-1.3.jar;C:\\takeTest\\lib\\junit.jar;C:\\takeTest;\" "
							+ path);
			System.out.println("***Compilation Completed*******");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return compilationStatus;
	}

	public static Map<String, String> compileJavaProgram(String path, String cp) {

		Map<String, String> compilationStatus = null;
		System.out.println("***JAVA COMPILE PROGRAM*******");
		try {
			compilationStatus = runProcess(
					"javac -cp \".;C:\\takeTest\\lib\\hamcrest-core-1.3.jar;C:\\takeTest\\lib\\junit.jar;C:\\takeTest\\"
							+ cp + ";\" " + path);
			System.out.println("***Compilation Completed*******");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return compilationStatus;
	}

	public static Map<String, String> runJavaProgram(String fileName, String path) {

		System.out.println("***JAVA RUN PROGRAM*******");
		Map<String, String> runStatusMap = null;
		try {
			runStatusMap = runProcess(
					"java -cp \".;C:\\takeTest\\lib\\hamcrest-core-1.3.jar;C:\\takeTest\\lib\\junit.jar;C:\\takeTest\\"
							+ path + ";\" " + fileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("***Run Java Completed*******");
		return runStatusMap;

	}

	private static String printLines(String cmd, InputStream ins) throws Exception {
		String line = null;
		String response = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
		while ((line = reader.readLine()) != null) {
			System.out.println(cmd + " " + line);
			response += line + "\r\n";
		}
		reader.close();
		return response;
	}

	private static Map<String, String> runProcess(String command) throws Exception {
		Process pro = Runtime.getRuntime().exec(command);
		String stdoutResponse = "", stderrResponse = "";
		stdoutResponse = printLines(command + " stdout:", pro.getInputStream());
		System.out.println("Input Stream Response is :" + stdoutResponse);
		stderrResponse = printLines(command + " stderr:", pro.getErrorStream());
		System.out.println("Error Stream Response is :" + stderrResponse);
		pro.waitFor();

		Map<String, String> processStatusMap = new HashMap<String, String>();
		if (pro.exitValue() == 0) {
			processStatusMap.put(CCTConstants.status.SUCCESS.name(), stdoutResponse);
		} else {
			processStatusMap.put(CCTConstants.status.FAIL.name(), stderrResponse);
		}
		return processStatusMap;
	}

}
