package dummyclasses;

public class DummyClassTwo extends DummyClassOne {
	
	private String anotherDummyPrivateString;
	private int anotherDummyPrivateInt;
	private String[] anotherDummyPrivateStringArray;
	private int[] anotherDummyPrivateIntArray;
	
	public String anotherDummyPublicString;
	public int anotherDummyPublicInt;
	public String[] anotherDummyPublicStringArray;
	public int[] anotherDummyPublicIntArray;
	
	public DummyClassTwo() {
		super();
	}

	public String getAnotherDummyPrivateString() {
		return anotherDummyPrivateString;
	}

	public void setAnotherDummyPrivateString(String anotherDummyPrivateString) {
		this.anotherDummyPrivateString = anotherDummyPrivateString;
	}

	public int getAnotherDummyPrivateInt() {
		return anotherDummyPrivateInt;
	}

	public void setAnotherDummyPrivateInt(int anotherDummyPrivateInt) {
		this.anotherDummyPrivateInt = anotherDummyPrivateInt;
	}

	public String[] getAnotherDummyPrivateStringArray() {
		return anotherDummyPrivateStringArray;
	}

	public void setAnotherDummyPrivateStringArray(String[] anotherDummyPrivateStringArray) {
		this.anotherDummyPrivateStringArray = anotherDummyPrivateStringArray;
	}

	public int[] getAnotherDummyPrivateIntArray() {
		return anotherDummyPrivateIntArray;
	}

	public void setAnotherDummyPrivateIntArray(int[] anotherDummyPrivateIntArray) {
		this.anotherDummyPrivateIntArray = anotherDummyPrivateIntArray;
	}
}
