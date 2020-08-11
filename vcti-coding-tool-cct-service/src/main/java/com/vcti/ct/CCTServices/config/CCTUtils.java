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

	private static String userHome = System.getProperty("user.home");
	private static String osName = System.getProperty("os.name");
	private static String userDirectory = System.getProperty("user.dir");

	public static String writeProgInFile(String prog, String fileName, String userDir, String fileExtension) {

		System.out.println("userDirectory :" + userDirectory);
		System.out.println("userHome : " + userHome);
		System.out.println("os osName :" + osName);

		String path = "C:\\takeTest\\" + userDir + "\\";

		if ("Linux".equals(osName)) {
			path = "/home/velankani/test" + "/" + userDir + "/";
		}
		
		System.out.println("path :" + path);

		Path pathDir = Paths.get(path);

		try {
			if (!Files.exists(pathDir)) {
				Files.createDirectories(pathDir);
				System.out.println("Directory created");
			} else {
				System.out.println("Directory already exists");
			}
			path = path + fileName + fileExtension;
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

			if ("Linux".equals(osName)) {
				compilationStatus = runProcess("javac -cp .:" + "/home/velankani/lib/hamcrest-core-1.3.jar:"
						+ "/home/velankani/lib/junit.jar:/home/velankani/test: " + path);
			} else {

				compilationStatus = runProcess(
						"javac -cp \".;C:\\takeTest\\lib\\hamcrest-core-1.3.jar;C:\\takeTest\\lib\\junit.jar;C:\\takeTest;\" "
								+ path);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return compilationStatus;
	}

	public static Map<String, String> compileJavaProgram(String path, String cp) {

		Map<String, String> compilationStatus = null;
		System.out.println("***JAVA COMPILE PROGRAM******* with path and cp");
		try {

			if ("Linux".equals(osName)) {
				
				cp = cp.substring(0, cp.length() - 1);
				compilationStatus = runProcess("javac -cp .:" + "/home/velankani/lib/hamcrest-core-1.3.jar:"
						+ "/home/velankani/lib/junit.jar:/home/velankani/test/"+cp+ ": " + path);	
				
			} else {

				compilationStatus = runProcess(
						"javac -cp \".;C:\\takeTest\\lib\\hamcrest-core-1.3.jar;C:\\takeTest\\lib\\junit.jar;C:\\takeTest\\"
								+ cp + ";\" " + path);
			}

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
			if ("Linux".equals(osName)) {
				runStatusMap = runProcess("java -cp \".:/home/velankani/lib/hamcrest-core-1.3.jar:"
						+ "/home/velankani/lib/junit.jar:/home/velankani/test/" + path + ":\" " + fileName);

			} else {
				runStatusMap = runProcess(
						"java -cp \".;C:\\takeTest\\lib\\hamcrest-core-1.3.jar;C:\\takeTest\\lib\\junit.jar;C:\\takeTest\\"
								+ path + ";\" " + fileName);
			}
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
			stderrResponse = stderrResponse.substring(stderrResponse.lastIndexOf("\\") + 1, stderrResponse.length());
			processStatusMap.put(CCTConstants.status.FAIL.name(), stderrResponse);
		}
		return processStatusMap;
	}

}
