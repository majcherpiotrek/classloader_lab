import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import customclassloader.CustomClassLoader;

public class Main {

	private static final String dummyClassOneName = "dummyclasses.DummyClassOne";
	private static final String dummyClassTwoName = "dummyclasses.DummyClassTwo";
	private static final String dummyClassesRoot = "../dummy-classes/bin/";
	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		ClassLoader classLoader = new CustomClassLoader(dummyClassesRoot);
		URL mainString = Main.class.getResource("Main.class");
		System.out.println(mainString);
		Class<?> dummyClassOne = classLoader.loadClass(dummyClassOneName);
		
		Constructor<?> dummyConstructor = dummyClassOne.getConstructor();
		Object o = dummyConstructor.newInstance();
		
		Field[] allClassFields = dummyClassOne.getDeclaredFields();
		
		System.out.println("\n-----------------------------------------"
				+ "\nClass: "  + dummyClassOne.getName());
		System.out.println("\nPublic fields:");
		
		
		System.out.println("\nPrivate fields:");
		for(Field field : allClassFields) {
			field.setAccessible(true);
			int modifier = field.getModifiers();
			String modifierString = "";
			if (Modifier.isPublic(modifier)) {
				modifierString += "public";
			} else if (Modifier.isPrivate(modifier)) {
				modifierString += "private";
			} else if (Modifier.isProtected(modifier)) {
				modifierString += "protected";
			} else {
				modifierString += "unknown modifier";
			}
			System.out.println( "modifier: \"" + modifierString + "\", type: \"" + field.getType().getSimpleName() +"\", name: \"" + field.getName() + "\", value: \""+ field.get(o) + "\"");
		}
	}

}
