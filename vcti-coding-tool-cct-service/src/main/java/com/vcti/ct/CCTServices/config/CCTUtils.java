package com.vcti.ct.CCTServices.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CCTUtils {

	public static String writeProgInFile(String prog, String fileName) {

		String currentDirectory = System.getProperty("user.dir");
		System.out.println("Current Directory is :" + currentDirectory);
		String path = "C:\\takeTest\\" + fileName + ".java";
		try {
			// Java 7
			Files.write(Paths.get(path), prog.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;

	}

	public static Map<String, String> compileJavaProgram(String fileName) {

		String separator = File.separator;
		separator = System.getProperty("file.separator");
		Map<String, String> compilationStatus = null;
		String currentDirectory = System.getProperty("user.dir");
		System.out.println("Current Directory is :" + currentDirectory);
		System.out.println("***JAVA COMPILE PROGRAM*******");
		try {
			compilationStatus = runProcess(
					"javac -cp \".;C:\\takeTest\\lib\\hamcrest-core-1.3.jar;C:\\takeTest\\lib\\junit.jar;C:\\takeTest;\" C:\\takeTest\\"
							+ fileName + ".java");
			System.out.println("***Compilation Completed*******");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return compilationStatus;
	}

	public static Map<String, String> runJavaProgram(String fileName) {

		System.out.println("***JAVA RUN PROGRAM*******");
		String currentDir = System.getProperty("user.dir");
		System.out.println("Current Directory is :" + currentDir);
		Map<String, String> runStatusMap = null;
		try {
			runStatusMap = runProcess(
					"java -cp \".;C:\\takeTest\\lib\\hamcrest-core-1.3.jar;C:\\takeTest\\lib\\junit.jar;C:\\takeTest;\" "
							+ fileName);
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
