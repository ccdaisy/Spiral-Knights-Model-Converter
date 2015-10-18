package xan_code.dathandler;

public class ClassToName {
	public static String toName(Object o) {
		return o.getClass().getTypeName();
	}
}
