package utils;

import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.TickUnits;

public class ComponentUtils {
	public static boolean isEmptyOrNull(String value) {
		if (value == null || value.trim().equals("")) {
			return true;
		}
		return false;
	}

	public static TickUnits prepareChartUnits() {
		final TickUnits standardUnits = new TickUnits();
		standardUnits.add(new NumberTickUnit(1));
		standardUnits.add(new NumberTickUnit(10));
		standardUnits.add(new NumberTickUnit(100));
		standardUnits.add(new NumberTickUnit(1000)); // Kilo
		standardUnits.add(new NumberTickUnit(10000));
		standardUnits.add(new NumberTickUnit(100000));
		standardUnits.add(new NumberTickUnit(1000000)); // Mega
		return standardUnits;
	}
}
