import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LifelineTest { 
	
	LifelineSite _subject;

	LocalDate dateDebutZone1 = LocalDate.parse("2023-03-15");
	LocalDate dateFinZone1 = LocalDate.parse("2023-09-10");
	LocalDate dateDebutZone2 = LocalDate.parse("2023-06-05");
	LocalDate dateFinZone2 = LocalDate.parse("2023-08-31");
	LocalDate date1 = LocalDate.parse("2023-01-01");
	LocalDate date2 = LocalDate.parse("2023-02-01");

	@BeforeEach
	public void setUp() {
		new Zone ("A", 0.06, 0.07, dateDebutZone1, dateFinZone1).register();
		new Zone ("B", 0.07, 0.06, dateDebutZone2,  dateFinZone2).register();
		new Zone ("C", 0.065, 0.065, dateDebutZone2,  dateFinZone2).register();
		_subject = new LifelineSite();
	}

	@Test
	public void testZero() {
		_subject.addReading(new Reading (10, date1));
		_subject.addReading(new Reading (10, date2));
		assertEquals(0d, _subject.charge().amount());
	}

	@Test
	public void test100() {
		_subject.addReading(new Reading (10,date1));
		_subject.addReading(new Reading (110, date2));
		assertEquals(4.84d, _subject.charge().amount());
	}

	@Test
	public void test99() {
		_subject.addReading(new Reading (100,date1));
		_subject.addReading(new Reading (199, date2));
		assertEquals(4.79d, _subject.charge().amount());
	}

	@Test
	public void test101() {
		_subject.addReading(new Reading (1000,date1));
		_subject.addReading(new Reading (1101, date2));
		assertEquals(4.91d, _subject.charge().amount());
	}

	@Test
	public void test199() {
		_subject.addReading(new Reading (10000,date1));
		_subject.addReading(new Reading (10199, date2));
		assertEquals(11.6d, _subject.charge().amount());
	}

	@Test
	public void test200() {
		_subject.addReading(new Reading (0,date1));
		_subject.addReading(new Reading (200, date2));
		assertEquals(11.68d, _subject.charge().amount());
	}

	@Test
	public void test201() {
		_subject.addReading(new Reading (50,date1));
		_subject.addReading(new Reading (251, date2));
		assertEquals(11.77d, _subject.charge().amount());
	}

	@Test
	public void testMax() {
		_subject.addReading(new Reading (0, date1));
		_subject.addReading(new Reading (Integer.MAX_VALUE, date2));
		assertEquals (1.9730005336E8, _subject.charge().amount());
	}

	@Test
	public void testNoReadings() {
		try {
			_subject.charge();
			assert(false);
		} catch (NullPointerException ignored) {}
	}
}
