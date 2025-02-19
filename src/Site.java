import java.time.LocalDate;

public abstract class Site {
	 public final Zone _zone;
	 public final Reading[] _readings = new Reading[1000];

	public Site(Zone zone) {
		_zone = zone;
	}
	public void addReading(Reading newReading) {
		_readings[firstUnusedReadingsIndex()] = newReading;
	}

	public int firstUnusedReadingsIndex(){
		int i;
		for (i = 0; _readings[i] != null; i++);
		return i;
	}
	public Dollars charge() {
		LocalDate end = lastReading().date();
		LocalDate start = nextDay(previousReading().date());
		return charge(lastUsage(), start, end);
	}

	public  abstract Dollars charge(int usage, LocalDate start, LocalDate end);

	private Reading lastReading() {
		return _readings[firstUnusedReadingsIndex()-1];
	}

	private Reading previousReading() {
		int i = firstUnusedReadingsIndex();
		return _readings[i-2];
	}

	public int lastUsage(){
		return lastReading().amount() -previousReading().amount();
	}

	public LocalDate nextDay(LocalDate date) {
		return date.plusDays(1);
	}
}
