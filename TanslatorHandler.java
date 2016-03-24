package com.b.jbehavetest.util.translate;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;

public class TanslatorHandler {
	public static final String GIVEN = "Given";
	public static final String WHEN = "When";
	public static final String THEN = "Then";

	
	public static String handleFile(String name, File file, boolean isWithSN ) throws IOException{
		StringBuilder content = new StringBuilder();
		content.append(header(name));
		Iterator<String> lines = FileUtils.lineIterator(file);
		while(lines.hasNext()){
			content.append(handleLine(lines.next(), isWithSN));
		}
		content.append(footer());
		return content.toString();
	}
	
	public static String handleLine(String input, boolean isWithSN) {

		if (input == null) {
			return "";
		}
		if (input.startsWith(GIVEN)) {
			return givenHandle(input, isWithSN);
		} else if (input.startsWith(WHEN)) {
			return whenHandle(input, isWithSN);
		} else if (input.startsWith(THEN)) {
			return thenHandle(input);
		} else {
			return otherHandle(input);
		}
	}

	public static String header(String fileName) {

		return "import org.jbehave.core.annotations.Given;\r\n" + "import org.jbehave.core.annotations.Named;\r\n"
				+ "import org.jbehave.core.annotations.Then;\r\n" + "import org.jbehave.core.annotations.When;\r\n"
				+ "import java.math.BigDecimal;\r\n" +

		"public class " + upperFirstChar(fileName) + "Steps {\r\n";
	}

	public static String footer() {
		return "}\r\n";
	}

	public static String givenHandle(String input, boolean isWithSN) {

		StringBuilder content = new StringBuilder();

		content.append("    " + generateField(input, isWithSN));
		content.append("    @Given(\"");
		content.append(addDollerInFrontOfDecimal(input.substring(5), isWithSN));
		content.append("\")");
		content.append("\r\n");
		content.append("    public void set" + generateMethodName(input) + "(");
		content.append("        " + generateMethodParam(input, isWithSN));
		content.append("        ){");
		content.append("        " + generateMethodParamToField(input, isWithSN));
		content.append("    }");
		content.append("\r\n");

		return content.toString();
	}

	public static String whenHandle(String input, boolean isWithSN) {
		StringBuilder content = new StringBuilder();

		content.append("    " + generateField(input, isWithSN));
		content.append("    @When(\"");
		content.append(addDollerInFrontOfDecimal(input.substring(4), isWithSN));
		content.append("\")");
		content.append("\r\n");
		content.append("    public void set" + generateMethodName(input) + "(");
		content.append("        " + generateMethodParam(input, isWithSN));
		content.append("        ){");
		content.append("        " + generateMethodParamToField(input, isWithSN));
		content.append("    }");
		content.append("\r\n");

		return content.toString();
	}

	public static String thenHandle(String input) {
		StringBuilder content = new StringBuilder();

		content.append("    @Then(\"");
		content.append(addDollerInFrontOfDecimal(input.substring(4), false));
		content.append("\")");
		content.append("\r\n");
		content.append("    public void test" + generateMethodName(input) + "(");
		content.append("        " + generateMethodParam(input, false));
		content.append("        ){");
		content.append("\r\n");
		content.append("    }");
		content.append("\r\n");

		return content.toString();
	}

	public static String otherHandle(String input) {
		return "";
	}

	public static String generateMethodName(String input) {
		String[] inutArray = input.split(" ");
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < inutArray.length; i++) {
			if (upperFirstChar(inutArray[i]) != null) {
				content.append(upperFirstChar(inutArray[i]));
			}
		}
		return content.toString().replaceAll("\\.", "_");
	}
	
	
	
	public static String generateMethodParam(String input, boolean isWithSN) {
		String[] inutArray = input.split(" ");
		StringBuilder content = new StringBuilder("\r\n");
		int count = 0;
		boolean isFirstDecimal = true;
		for (int i = 0; i < inutArray.length; i++) {
			if (isNumeric(inutArray[i]) && isFirstDecimal && isWithSN) {
				isFirstDecimal = false;
				continue;
			}
			if (isNumeric(inutArray[i])) {
				content.append("            @Named(\"param" + count + "\") BigDecimal "+lowerFirstChar(generateMethodName(input))+"_Param" + count + ", //" + inutArray[i]
						+ "\r\n");
				count++;
			}
		}
		if(content.toString().lastIndexOf(",")>-1){
			return content.toString().substring(
						0, content.toString().lastIndexOf(","))
					+ content.toString().substring(
							content.toString().lastIndexOf(",") + 1);
		}else{
			return "\r\n";
		}
	}
	
	
	
	public static String generateMethodParamToField(String input, boolean isWithSN) {
		String[] inputArray = input.split(" ");
		StringBuilder content = new StringBuilder("\r\n");
		int count = 0;
		boolean isFirstDecimal = true;
		for (int i = 0; i < inputArray.length; i++) {
			if (isNumeric(inputArray[i]) && isFirstDecimal && isWithSN) {
				isFirstDecimal = false;
				continue;
			}
			if (isNumeric(inputArray[i])) {
				content.append("        this." + lowerFirstChar(generateMethodName(input))+"_Param" + count + " = " + lowerFirstChar(generateMethodName(input))+"_Param" + count + "; //" + inputArray[i]
						+ "\r\n");
				count++;
			}
		}
		return content.toString();
	}
	
	public static String generateField(String input, boolean isWithSN) {
		String[] inputArray = input.split(" ");
		StringBuilder content = new StringBuilder("\r\n");
		int count = 0;
		boolean isFirstDecimal = true;
		for (int i = 0; i < inputArray.length; i++) {
			if (isNumeric(inputArray[i]) && isFirstDecimal && isWithSN) {
				isFirstDecimal = false;
				continue;
			}
			if (isNumeric(inputArray[i])) {
				content.append("    private BigDecimal "+lowerFirstChar(generateMethodName(input))+"_Param" + count + "; //" + inputArray[i]
						+ "\r\n");
				count++;
			}
		}
		return content.toString();
	}

	public static String upperFirstChar(String input) {
		if (input == null || "".equals(input)) {
			return null;
		}

		return input.substring(0, 1).toUpperCase() + input.substring(1);
	}
	
	public static String lowerFirstChar(String input) {
		if (input == null || "".equals(input)) {
			return null;
		}

		return input.substring(0, 1).toLowerCase() + input.substring(1);
	}

	public static String addDollerInFrontOfDecimal(String input, boolean isWithSN) {
		String[] inutArray = input.split(" ");
		StringBuilder content = new StringBuilder();
		boolean isFirstDecimal = true;
		for (int i = 0; i < inutArray.length; i++) {
			if (isNumeric(inutArray[i]) && isFirstDecimal && isWithSN) {
				isFirstDecimal = false;
				content.append(" " + inutArray[i]);
				continue;
			}

			if (isNumeric(inutArray[i])) {
				content.append(" $" + inutArray[i]);
			} else {
				if (!"".equals(inutArray[i])) {
					content.append(" " + inutArray[i]);
				}
			}
		}
		return content.toString().substring(1);
	}

	public static boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static void main(String args[]) throws IOException {
		System.out.println(header("hole"));
		System.out.println("//" + upperFirstChar("abc"));
		System.out.println("//" + generateMethodName("abc csd hkjl 1"));
		System.out.println(givenHandle("Given 1 abc csd hkjl 2.7 and kads asda 3.0", true));
		System.out.println(whenHandle("When 1 abc csd hkjl 4.40", true));
		System.out.println(thenHandle("Then abc csd hkjl is 2.4"));
		System.out.println(footer());
		System.out.println("============================================================");
		System.out.println(handleFile("hole", new File("C:/UBS/Dev/b/eclipse-jee-mars-1-win32-x86_64/workspace/jbehavetest/src/test/java/com/b/jbehavetest/demo_story.story"), true));
	}

}
