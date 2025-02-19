import java.time.LocalDate;

public class ResidentialSite extends Site {

	private static final double TAX_RATE = 0.05;

	public ResidentialSite(Zone zone) {
		super(zone);
	}

	@Override
	public Dollars charge(int usage, LocalDate start, LocalDate end) {
		Dollars result;
		double summerFraction;

		// Find out how much of period is in the summer
		if (start.isAfter(_zone.summerEnd()) || end.isBefore(_zone.summerStart()))
			summerFraction = 0;
		else if (!start.isBefore(_zone.summerStart()) && !start.isAfter(_zone.summerEnd()) &&
				!end.isBefore(_zone.summerStart()) && !end.isAfter(_zone.summerEnd()))
			summerFraction = 1;
		else { // part in summer part in winter
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
		result = result.plus(new Dollars (result.times(TAX_RATE)));
		Dollars fuel = new Dollars(usage * 0.0175);
		result = result.plus(fuel);
		result = new Dollars (result.plus(fuel.times(TAX_RATE)));
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

		result += arg.getDayOfYear();
		//check leap year
		if ((arg.getYear()%4 == 0) && ((arg.getYear() % 100 != 0) ||
				((arg.getYear() + 1900) % 400 == 0))) {
			result++;
		}
		return result;
	}

}
