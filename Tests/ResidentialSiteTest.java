import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResidentialSiteTest {

    ResidentialSite _subject;

    LocalDate dateDebut = LocalDate.parse("2023-06-21");
    LocalDate dateFin = LocalDate.parse("2023-09-21");
    LocalDate dateAvant = LocalDate.parse("2023-01-21");
    LocalDate dateAvant2 = LocalDate.parse("2023-05-21");
    LocalDate dateApres = LocalDate.parse("2023-10-21");
    LocalDate dateApres2 = LocalDate.parse("2023-11-21");
    LocalDate datePendant = LocalDate.parse("2023-07-21");
    LocalDate datePendant2 = LocalDate.parse("2023-08-21");

    @BeforeEach
    public void setUp() {
        Zone zone = new Zone ("A", 0.06, 0.07, dateDebut, dateFin).register();
        _subject = new ResidentialSite(zone);
    }

    @Test
    public void testZero() {
        _subject.addReading(new Reading (10, dateAvant));
        _subject.addReading(new Reading (10, dateAvant2));
        assertEquals(0d, _subject.charge().amount());
    }

    @Test
    public void test1() {
        _subject.addReading(new Reading (10, dateApres));
        _subject.addReading(new Reading (10, dateApres2));
        assertEquals(0d, _subject.charge().amount());
    }

    @Test
    public void test2() {
        _subject.addReading(new Reading (10, dateAvant));
        _subject.addReading(new Reading (10, datePendant));
        assertEquals(0d, _subject.charge().amount());
    }

    @Test
    public void test3() {
        _subject.addReading(new Reading (10, datePendant));
        _subject.addReading(new Reading (10, dateFin));
        assertEquals(0d, _subject.charge().amount());
    }

    @Test
    public void test4() {
        _subject.addReading(new Reading (10, datePendant));
        _subject.addReading(new Reading (10, datePendant2));
        assertEquals(0d, _subject.charge().amount());
    }

    @Test
    public void test5() {
        _subject.addReading(new Reading (10, dateAvant));
        _subject.addReading(new Reading (10, dateApres));
        assertEquals(0d, _subject.charge().amount());
    }

    @Test
    public void testMax() {
        _subject.addReading(new Reading (0, dateDebut));
        _subject.addReading(new Reading (Integer.MAX_VALUE, dateFin));
        assertEquals (1.7738214892E8, _subject.charge().amount());
    }
}