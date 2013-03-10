package utils;

public class ComponentUtils {
	public static boolean isEmptyOrNull(String value) {
		if (value == null || value.trim().equals("")) {
			return true;
		}
		return false;
	}
}
