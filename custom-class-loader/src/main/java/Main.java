import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import customclassloader.CustomClassLoader;

public class Main {
	
	private static final String APP_NAME = "Custom class loader";
	private static final String DUMMY_CLASS_ONE_NAME = "dummyclasses.DummyClassOne";
	private static final String DUMMY_CLASS_TWO_NAME = "dummyclasses.DummyClassTwo";
	private static final String DUMMY_CLASSES_ROOT = "../dummy-classes/bin/";
	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		createAndShowGUI();
	}
	
	private static void createAndShowGUI() {
		ClassLoader classLoader = new CustomClassLoader(DUMMY_CLASSES_ROOT);
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame mainFrame = new JFrame(APP_NAME);
		
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// main pane
		JPanel mainPane = new JPanel();
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
		
		// top pane for class loading input
		JPanel loadClassPane = new JPanel();
		loadClassPane.setLayout(new BoxLayout(loadClassPane, BoxLayout.LINE_AXIS));
		
		// fields table
		FieldsTableData fieldsTableData = new FieldsTableData();
		JTable fieldsTable = new JTable(fieldsTableData);
		JScrollPane classFieldsTableScrollPane = new JScrollPane(fieldsTable);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));
		JLabel classNameLabel = new JLabel("No class loaded");
		classNameLabel.setLabelFor(classFieldsTableScrollPane);
		
		centerPanel.add(classNameLabel);
		centerPanel.add(classFieldsTableScrollPane);
		
		JLabel classNameInputFieldLabel = new JLabel("Enter full class name: ");
		JTextField classNameInputField = new JTextField(40);
		classNameInputFieldLabel.setLabelFor(classNameInputField);
			
		JPanel fieldSetterPanel = new JPanel();
		fieldSetterPanel.setLayout(new BoxLayout(fieldSetterPanel, BoxLayout.LINE_AXIS));
		
		JLabel fieldNameInputLabel = new JLabel("Field name: ");
		JLabel fieldValueInputLabel = new JLabel("Field value: ");
		
		JTextField fieldNameInput = new JTextField(20);
		JTextField fieldValueInput = new JTextField(20);
		fieldNameInputLabel.setLabelFor(fieldNameInput);
		fieldValueInputLabel.setLabelFor(fieldValueInput);
		
		JTextArea infoArea = new JTextArea(15, 60);
		infoArea.setText("");
		infoArea.setEditable(false);
		JScrollPane textAreaScrollPane = new JScrollPane(infoArea);
		
		JButton loadClassButton = new JButton("Load class");
		loadClassButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String className = classNameInputField.getText();
				try {
					fieldsTableData.setDataClass(classLoader.loadClass(className));
					fieldsTableData.createObject();
					infoArea.append("Class \"" + className + "\" loaded successfully!");
					String classNameLabelValue = fieldsTableData.getClassName();
					List<String> superClasses = fieldsTableData.getSuperclasses();
					if (!superClasses.isEmpty()) {
						classNameLabelValue += " extends ";
						for(int i=0; i<superClasses.size(); i++) {
							classNameLabelValue += superClasses.get(i);
							if (i < superClasses.size() - 1) {
								classNameLabelValue += " extends ";
							}
						}
					}
					classNameLabel.setText(classNameLabelValue);
					centerPanel.revalidate();
					centerPanel.repaint();
				} catch (Exception e1) {
					infoArea.append("\nCannot load class \"" + className + "\". Exception: " + e1.getMessage());
				}
				
			}
		});
		
		JButton setFieldButton = new JButton("Set field value");
		setFieldButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String fieldName = fieldNameInput.getText();
				String fieldValue = fieldValueInput.getText();
				try {
					fieldsTableData.setValue(fieldName, fieldValue);
				} catch(Exception e2) {
					infoArea.append("\nError: " + e2.getMessage());
				}
			}
		});
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.PAGE_AXIS));
		
		fieldSetterPanel.add(fieldNameInputLabel);
		fieldSetterPanel.add(fieldNameInput);
		fieldSetterPanel.add(fieldValueInputLabel);
		fieldSetterPanel.add(fieldValueInput);
		fieldSetterPanel.add(setFieldButton);
		
		bottomPanel.add(fieldSetterPanel);
		bottomPanel.add(textAreaScrollPane);
		
		loadClassPane.add(classNameInputFieldLabel);
		loadClassPane.add(classNameInputField);
		loadClassPane.add(loadClassButton);
		
		mainPane.add(loadClassPane);
		mainPane.add(centerPanel);
		mainPane.add(bottomPanel);
		
		mainFrame.getContentPane().add(mainPane);
		mainFrame.pack();
		mainFrame.setVisible(true);
	}

}
