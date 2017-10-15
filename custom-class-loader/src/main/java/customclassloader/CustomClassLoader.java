package customclassloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;

public class CustomClassLoader extends ClassLoader {
	
	protected static final String classesRoot = "../dummy-classes/bin/";
	protected static final String classesFilesSuffix = ".class";
	private Hashtable<String, Class<?>> classesCache;
	
	public CustomClassLoader() {
		this.classesCache = new Hashtable<>();
	}
	
	protected byte[] getClassBytesFromFile(String className) throws IOException {
		
		System.out.println("Fetching the following class implementation: " + className);
		byte[] result = null;
		
		String pathToClassFile = createPathToClassFromClassName(className);
		try {
			FileInputStream fis = new FileInputStream(new File(pathToClassFile));
			result = new byte[fis.available()];
			fis.read(result);
			System.out.println("Successfully fetched the class data from: " + pathToClassFile);
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find the " + className + " class file in" + pathToClassFile);
			throw e;
		} catch (IOException e) {
			System.out.println("Cannot read from the class file: " + pathToClassFile);
			throw e;
		}
		
		return result;
	}
	
	@Override
	public Class<?> loadClass(String className) throws ClassNotFoundException{
		return loadClass(className, true);
	}
	
	@Override
	public synchronized Class<?> loadClass(String className, boolean resolveIt) throws ClassNotFoundException{
		Class<?> resultClass = null;
		byte[] classData;
		
		System.out.println("Loading the class: " + className);
		
		// Check in local cache
		resultClass = classesCache.get(className);
		if (resultClass == null) {
			
			// Check with the premordial class loader
			try {
				resultClass = super.findSystemClass(className);
				System.out.println("Returning the system class.");
			} catch(ClassNotFoundException e) {
				System.out.println("Not a system class: " + className);
				
				try {
					classData = getClassBytesFromFile(className);
				} catch (IOException e1) {
					e1.printStackTrace();
					throw new ClassNotFoundException("Failed on trying to read the class file: ", e1);
				}
				
				if (classData == null) {
					throw new ClassNotFoundException("Reading class data returned null");
				}
				
				resultClass = super.defineClass(className, classData, 0, classData.length);
				
				if (resultClass == null) {
					throw new ClassFormatError("Unable to parse the class");
				}
				
				if (resolveIt) {
					super.resolveClass(resultClass);
				}
				
				classesCache.put(className, resultClass);
				System.out.println("Loaded the " + className + " class successfully.");
			}
		} else {
			System.out.println("Returning the result from cache.");
		}
		return resultClass;
	}
	
	protected String dotsToSlashes(String className) {
		return className.replaceAll("\\.", "/");
	}
	
	protected String createPathToClassFromClassName(String className) {
		String result = classesRoot;
		result += dotsToSlashes(className);
		result += classesFilesSuffix;
		
		return result;
	}
}
