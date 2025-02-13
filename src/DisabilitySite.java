import java.time.LocalDate;

public class DisabilitySite {

	private final Reading[] _readings = new Reading[1000];
	private static final Dollars FUEL_TAX_CAP = new Dollars (0.10);
	private static final double TAX_RATE = 0.05;
	private final Zone _zone;
	private static final int CAP = 200;
	
	public DisabilitySite (Zone zone) {
		_zone = zone;
	}

	public void addReading(Reading newReading) {
		int i;
		for (i = 0; _readings[i] != null; i++);
		_readings[i] = newReading;
	}

	public Dollars charge() {
		int i;
		for (i = 0; _readings[i] != null; i++);
		int usage = _readings[i-1].amount() - _readings[i-2].amount();
		LocalDate end = LocalDate.parse(_readings[i-1].toString());
		LocalDate start = LocalDate.parse(_readings[i-2].toString());
		start = start.plusDays(1); //set to begining of period
		return charge(usage, start, end);
	}

	private Dollars charge(int fullUsage, LocalDate start, LocalDate end) {
		Dollars result;
		double summerFraction;
		int usage = Math.min(fullUsage, CAP);
		if (start.isAfter(_zone.summerEnd()) || end.isBefore(_zone.summerStart()))
			summerFraction = 0;
		else if (!start.isBefore(_zone.summerStart()) && !start.isAfter(_zone.summerEnd()) &&
				!end.isBefore(_zone.summerStart()) && !end.isAfter(_zone.summerEnd()))
			summerFraction = 1;
		else {
			double summerDays;
			if (start.isBefore(_zone.summerStart()) || start.isAfter(_zone.summerEnd())) {
				// end is in the summer
				summerDays = dayOfYear(end) - dayOfYear (_zone.summerStart()) + 1;
			} else {
				// start is in summer
				summerDays = dayOfYear(_zone.summerEnd()) - dayOfYear (start) + 1;
			};
			summerFraction = summerDays / (dayOfYear(end) - dayOfYear(start) + 1);
		};
		result = new Dollars ((usage * _zone.summerRate() * summerFraction) +
				(usage * _zone.winterRate() * (1 - summerFraction)));
		result = result.plus(new Dollars (Math.max(fullUsage - usage, 0) * 0.062));
		result = result.plus(new Dollars (result.times(TAX_RATE)));
		Dollars fuel = new Dollars(fullUsage * 0.0175);
		result = result.plus(fuel);
		result = new Dollars (result.plus(fuel.times(TAX_RATE).min(FUEL_TAX_CAP)));
		return result;
	}

	private int dayOfYear(LocalDate arg) {
		int result = switch (arg.getMonthValue()) {
            case 1 -> 0;
            case 2 -> 31;
            case 3 -> 59;
            case 4 -> 90;
            case 5 -> 120;
            case 6 -> 151;
            case 7 -> 181;
            case 8 -> 212;
            case 9 -> 243;
            case 10 -> 273;
            case 11 -> 304;
            case 12 -> 334;
            default -> throw new IllegalArgumentException();
        };

        result += Integer.parseInt(arg.toString());
		//check leap year
		if ((arg.getYear()%4 == 0) && ((arg.getYear() % 100 != 0) ||
				((arg.getYear() + 1900) % 400 == 0))) {
			result++;
		}
		return result;
	}


}
