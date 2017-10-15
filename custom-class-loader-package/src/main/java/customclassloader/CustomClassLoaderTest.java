package customclassloader;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

public class CustomClassLoaderTest {

	private CustomClassLoader customClassLoader;
	
	@Test
	public void dotsToSlashesTest() {
		String className = "dummyclasses.DummyClassOne";
		String expectedPath = "dummyclasses/DummyClassOne";
		
		customClassLoader = new CustomClassLoader("../dummy-classes/bin/");
		
		assertEquals(expectedPath, customClassLoader.dotsToSlashes(className));
	}
	
	@Test
	public void createPathToClassFromClassNameTest() {
		String className = "dummyclasses.DummyClassOne";
		String expectedPath = "../dummy-classes/bin/dummyclasses/DummyClassOne.class";
		
		customClassLoader = new CustomClassLoader("../dummy-classes/bin/");
		
		assertEquals(expectedPath, customClassLoader.createPathToClassFromClassName(className));
	}
	
	@Test
	public void getClassBytesFromFileTestDummyClassOne() throws IOException {
		String className = "dummyclasses.DummyClassOne";
		customClassLoader = new CustomClassLoader("../dummy-classes/bin/");
		byte[] result;
		
		result = customClassLoader.getClassBytesFromFile(className);
		assertNotNull(result);
	}
	
	@Test
	public void getClassBytesFromFileTestDummyClassTwo() throws IOException {
		String className = "dummyclasses.DummyClassTwo";
		customClassLoader = new CustomClassLoader("../dummy-classes/bin/");
		byte[] result;
		
		result = customClassLoader.getClassBytesFromFile(className);
		assertNotNull(result);
	}
	
	@Test(expected = FileNotFoundException.class)
	public void getClassBytesFromFileTestNoSuchFile() throws IOException {
		String className = "dummyclasses.DummyClassThree";
		customClassLoader = new CustomClassLoader("../dummy-classes/bin/");
		byte[] result;
		
		result = customClassLoader.getClassBytesFromFile(className);
	}
	
	@Test
	public void loadClassTestDummyClassOne() throws ClassNotFoundException {
		String className = "dummyclasses.DummyClassOne";
		customClassLoader = new CustomClassLoader("../dummy-classes/bin/");
		Class<?> loadedClass = null;
		loadedClass = customClassLoader.loadClass(className);
		assertNotNull(loadedClass);
	}
	
	@Test
	public void loadClassTestDummyClassTwo() throws ClassNotFoundException {
		String className = "dummyclasses.DummyClassTwo";
		customClassLoader = new CustomClassLoader("../dummy-classes/bin/");
		Class<?> loadedClass = null;
		loadedClass = customClassLoader.loadClass(className);
		assertNotNull(loadedClass);
	}
	
	@Test(expected = ClassNotFoundException.class)
	public void loadClassTestDummyNoSuchClass() throws ClassNotFoundException {
		String className = "dummyclasses.DummyClassThree";
		customClassLoader = new CustomClassLoader("../dummy-classes/bin/");
		Class<?> loadedClass = null;
		loadedClass = customClassLoader.loadClass(className);
	}
	
	@Test
	public void loadClassTestDummyClassOneFromCache() throws ClassNotFoundException {
		String className = "dummyclasses.DummyClassOne";
		customClassLoader = new CustomClassLoader("../dummy-classes/bin/");
		Class<?> loadedClass = null;
		customClassLoader.loadClass(className);
		loadedClass = customClassLoader.loadClass(className);
		assertNotNull(loadedClass);
	}
}
