import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DisabilitySiteTest {

    DisabilitySite _subject;

    Date dateDebut = new Date("21 Jun 2023");
    Date dateFin = new Date ("21 Sep 2023");
    Date dateAvant = new Date("21 Jan 2023");
    Date dateAvant2 = new Date("21 May 2023");
    Date dateApres = new Date("21 Oct 2023");
    Date dateApres2 = new Date("21 Nov 2023");
    Date datePendant = new Date("21 Jul 2023");
    Date datePendant2 = new Date("21 Aug 2023");

    @BeforeEach
    public void setUp() {
        Zone zone = new Zone ("A", 0.06, 0.07, dateDebut, dateFin).register();
        _subject = new DisabilitySite(zone);
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