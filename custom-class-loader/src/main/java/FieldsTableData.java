import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class FieldsTableData extends AbstractTableModel{
	
	private static final String[] COLUMN_NAMES = {"Modifier", "Type", "Name", "Value"};
	private List<Field> fieldsList;
	private Class<?> dataClass;
	private Object relatedClassObject;
	
	
	public Class<?> getDataClass() {
		return dataClass;
	}
	
	public void createObject() throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException {
		if (dataClass != null) {
			Constructor<?> constructor = dataClass.getConstructor();
			relatedClassObject = dataClass.newInstance();
			
			Field[] allClassFields = dataClass.getDeclaredFields();			
			fieldsList.addAll(Arrays.asList(allClassFields));
		}
		fireTableDataChanged();
	}
	
	public void setValue(String fieldName, String fieldValue) throws IllegalArgumentException, IllegalAccessException {
		if (fieldName == null) {
			throw new IllegalArgumentException("Argument \"fieldName\" cannot be null!");
		}
		
		boolean fieldSet = false;
		for (Field field : fieldsList) {
			if (field.getName().equals(fieldName)) {
				
				if (field.getType().equals(int.class)) {
					try {
						int value = Integer.parseInt(fieldValue);
						field.set(relatedClassObject, value);
						fieldSet = true;
						break;
					} catch (NumberFormatException e) {
						throw new IllegalArgumentException("Wrong field value - should be of type \"int\"!");
					}
				}
				
				if (field.getType().equals(int[].class)) {
					try {
						String[] items = fieldValue.replaceAll("\\[","").replaceAll("\\]", "").replaceAll("\\s", "").split(",");
						int[] values = new int[items.length];
						for (int i=0; i<values.length; i++) {
							values[i] = Integer.parseInt(items[i]);
						}
						field.set(relatedClassObject, values);
						fieldSet=true;
						break;
					} catch (NumberFormatException e) {
						throw new IllegalArgumentException("Wrong field value - should be of type \"int[]\"!");
					}
				}
				
				if (field.getType().equals(String.class)) {
					field.set(relatedClassObject, fieldValue);
					fieldSet = true;
					break;
				}
				
				if (field.getType().equals(String[].class)) {
					String[] items = fieldValue.replaceAll("\\[","").replaceAll("\\]", "").replaceAll("\\s", "").split(",");
					field.set(relatedClassObject, items);
					fieldSet = true;
					break;
				}
			}
		}
		
		if (!fieldSet) {
			throw new IllegalArgumentException("Unknown field type! Unable to set the field's value!");
		}
		fireTableDataChanged();
	}
	
	public void setDataClass(Class<?> dataClass) {
		this.dataClass = dataClass;
	}

	public FieldsTableData() {
		super();
		this.fieldsList = new ArrayList<>();
	}
	
	public String getClassName() {
		String name = "no class loaded";
		if (dataClass != null) {
			name = dataClass.getName();
		}
		return name;
	}
	
	public List<String> getSuperclasses() {
		List<String> superclassesList = new LinkedList<>();
		if (dataClass != null) {
			Class<?> currentClass = dataClass;
			while (currentClass.getSuperclass() != null) {
				Class<?> superclass = currentClass.getSuperclass();
				superclassesList.add(superclass.getSimpleName());
				currentClass = superclass;
			}
		}
		return superclassesList;
	}
	
	@Override
	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}

	@Override
	public int getRowCount() {
		return fieldsList.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		Object result = null;
		
		if (row >= 0 && row < fieldsList.size()) {
			Field field = fieldsList.get(row);
			field.setAccessible(true);
			switch(column) {
				case 0: {
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
					result = modifierString;
					break;
				}
				case 1: {
					result = field.getType().getSimpleName();
					break;
				}
				case 2: {
					result = field.getName();
					break;
				}
				case 3: {
					if (relatedClassObject != null) {
						result = null;
						try {
							if (field.getType().isArray()) {
								Object array = field.get(relatedClassObject);
								if (array != null) {
									Object[] values = unpackArray(array);
									result = "[";
									for (int i= 0; i<values.length; i++) {
										result += String.valueOf(values[i]);
										if (i < values.length - 1) {
											result += ",";
										}
									}
									result += "]";
								}
							} else {
								result = field.get(relatedClassObject);
							}
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					} 
					break;
				}
			}
		}
		
		return result;
	}
	
	private Object[] unpackArray(Object array) {
		Object[] resultArray = new Object[Array.getLength(array)];
	    for(int i=0;i<resultArray.length;i++)
	        resultArray[i] = Array.get(array, i);
	    return resultArray;
	}
	
	@Override
	public String getColumnName(int col) {
		String result = null;
		if (col >= 0 && col < COLUMN_NAMES.length) {
			result = COLUMN_NAMES[col];
		}
		return result;
	}
	
	public void unloadClass() {
		dataClass = null;
		relatedClassObject = null;
		fieldsList = new ArrayList<>();
		fireTableDataChanged();
	}
}
