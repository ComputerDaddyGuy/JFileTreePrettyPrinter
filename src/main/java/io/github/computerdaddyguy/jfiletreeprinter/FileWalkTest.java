package io.github.computerdaddyguy.jfiletreeprinter;

import java.io.IOException;

import io.github.computerdaddyguy.jfiletreeprinter.core.FileTreePrettyPrinter;

public class FileWalkTest {

	public static void main(String[] args) throws IOException {
		test("C:\\Users\\samue\\eclipse-workspace\\jfiletreeprinter\\src\\test\\resources");
//		test("C:\\Users\\samue\\eclipse-workspace\\jfiletreeprinter\\src\\test\\resources\\empty_folder");
//		test("C:\\Users\\samue\\eclipse-workspace\\jfiletreeprinter\\src\\test\\resources\\folder_with_1_file");
//		test("C:\\Users\\samue\\eclipse-workspace\\jfiletreeprinter\\src\\test\\resources\\folder_with_3_files");
//		test("C:\\Users\\samue\\eclipse-workspace\\jfiletreeprinter\\src\\test\\resources\\folder_with_3_files_and_subfolders");
//		test("C:\\Users\\samue\\eclipse-workspace\\jfiletreeprinter\\src\\test\\resources\\skipped");
//		test("C:\\Users\\samue\\eclipse-workspace\\jfiletreeprinter");
	}

	private static void test(String path) throws IOException {
		System.out.println("-------------------------------------------------------");
		System.out.println(">>> " + path);
		var tree = FileTreePrettyPrinter.createDefault().prettyPrint(path);
		System.out.println(tree);
		System.out.println("-------------------------------------------------------");
	}

}
