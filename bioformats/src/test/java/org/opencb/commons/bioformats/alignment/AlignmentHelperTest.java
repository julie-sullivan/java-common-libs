package org.opencb.commons.bioformats.alignment;

import static org.junit.Assert.*;
import java.util.LinkedList;
import java.util.List;
import net.sf.samtools.*;
import org.junit.*;

/**
 *
 * @author Cristina Yenyxe Gonzalez Garcia <cgonzalez@cipf.es>
 */
public class AlignmentHelperTest {
    
    public AlignmentHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getDifferencesFromCigar method, of class AlignmentHelper.
     */
    @Test
    public void testGetDifferencesFromCigarInsertions() {
        SAMRecord record = new SAMRecord(new SAMFileHeader());
        List<CigarElement> elements = null;
        List expResult = null, result = null;
        String referenceSequence = "AAAACCCCGGGGTTTTAAAACCCCGGGGTTTTAAAACCCCGGGGTTTTAAAACCCCGGGGTTTTAAAACCCCGGGGTTTT"; // 80 nt
        
        // 20M1I20M - middle
        System.out.println("20M1I20M");
        elements = new LinkedList<>();
        elements.add(new CigarElement(20, CigarOperator.M));
        elements.add(new CigarElement(1, CigarOperator.I));
        elements.add(new CigarElement(20, CigarOperator.M));
        record.setCigar(new Cigar(elements));
        record.setReadString("AAAACCCCGGGGTTTTAAAANCCCCGGGGTTTTAAAACCCC");
        
        expResult = new LinkedList<>();
        expResult.add(new Alignment.AlignmentDifference(20, Alignment.AlignmentDifference.INSERTION, "N"));
        result = AlignmentHelper.getDifferencesFromCigar(record, referenceSequence);
        assertEquals(expResult.size(), result.size());
        
        for (int i = 0; i < result.size(); i++) {
            assertTrue("Expected " + expResult.get(i).toString() + " but got " + result.get(i).toString(),
                        expResult.get(i).equals(result.get(i)));
        }
        
        // 3I10M2I10M5I30M1I - beginning, middle and end
        System.out.println("3I10M2I10M5I30M1I");
        elements = new LinkedList<>();
        elements.add(new CigarElement(3, CigarOperator.I));
        elements.add(new CigarElement(10, CigarOperator.M));
        elements.add(new CigarElement(2, CigarOperator.I));
        elements.add(new CigarElement(10, CigarOperator.M));
        elements.add(new CigarElement(5, CigarOperator.I));
        elements.add(new CigarElement(30, CigarOperator.M));
        elements.add(new CigarElement(1, CigarOperator.I));
        record.setCigar(new Cigar(elements));
        record.setReadString("GGGAAAACCCCGGACGGTTTTAAAAGTGTGCCCCGGGGTTTTAAAACCCCGGGGTTTTAAN");
        
        expResult = new LinkedList<>();
        expResult.add(new Alignment.AlignmentDifference(0, Alignment.AlignmentDifference.INSERTION, "GGG"));
        expResult.add(new Alignment.AlignmentDifference(10, Alignment.AlignmentDifference.INSERTION, "AC"));
        expResult.add(new Alignment.AlignmentDifference(20, Alignment.AlignmentDifference.INSERTION, "GTGTG"));
        expResult.add(new Alignment.AlignmentDifference(50, Alignment.AlignmentDifference.INSERTION, "N"));
        result = AlignmentHelper.getDifferencesFromCigar(record, referenceSequence);
        assertEquals(expResult.size(), result.size());
        
        for (int i = 0; i < result.size(); i++) {
            assertTrue("Expected " + expResult.get(i).toString() + " but got " + result.get(i).toString(),
                        expResult.get(i).equals(result.get(i)));
        }
    }
    
    
    /**
     * Test of getDifferencesFromCigar method, of class AlignmentHelper.
     */
    @Test
    public void testGetDifferencesFromCigarDeletions() {
        SAMRecord record = new SAMRecord(new SAMFileHeader());
        List<CigarElement> elements = null;
        List expResult = null, result = null;
        String referenceSequence = "AAAACCCCGGGGTTTTAAAACCCCGGGGTTTTAAAACCCCGGGGTTTTAAAACCCCGGGGTTTTAAAACCCCGGGGTTTT"; // 80 nt
        
        // 20M2D18M - middle
        System.out.println("20M2D18M");
        elements = new LinkedList<>();
        elements.add(new CigarElement(20, CigarOperator.M));
        elements.add(new CigarElement(2, CigarOperator.D));
        elements.add(new CigarElement(18, CigarOperator.M));
        record.setCigar(new Cigar(elements));
        record.setReadString("AAAACCCCGGGGTTTTAAAACCGGGGTTTTAAAACCCC");
        
        expResult = new LinkedList<>();
        expResult.add(new Alignment.AlignmentDifference(20, Alignment.AlignmentDifference.DELETION, "CC"));
        result = AlignmentHelper.getDifferencesFromCigar(record, referenceSequence);
        assertEquals(expResult.size(), result.size());
        
        for (int i = 0; i < result.size(); i++) {
            assertTrue("Expected " + expResult.get(i).toString() + " but got " + result.get(i).toString(),
                        expResult.get(i).equals(result.get(i)));
        }
        
        // 3D10M2D10M5D19M - beginning and middle 
        System.out.println("3D10M2D10M5D19M");
        elements = new LinkedList<>();
        elements.add(new CigarElement(3, CigarOperator.D));
        elements.add(new CigarElement(10, CigarOperator.M));
        elements.add(new CigarElement(2, CigarOperator.D));
        elements.add(new CigarElement(10, CigarOperator.M));
        elements.add(new CigarElement(5, CigarOperator.D));
        elements.add(new CigarElement(20, CigarOperator.M));
        record.setCigar(new Cigar(elements));
        record.setReadString("ACCCCGGGGTTAAAACCCCGTTAAAACCCCGGGGTTTTAA");
        
        expResult = new LinkedList<>();
        expResult.add(new Alignment.AlignmentDifference(0, Alignment.AlignmentDifference.DELETION, "AAA"));
        expResult.add(new Alignment.AlignmentDifference(13, Alignment.AlignmentDifference.DELETION, "TT"));
        expResult.add(new Alignment.AlignmentDifference(25, Alignment.AlignmentDifference.DELETION, "GGGTT"));
        result = AlignmentHelper.getDifferencesFromCigar(record, referenceSequence);
//        for (int i = 0; i < result.size(); i++) {
//            System.out.println(result.get(i).toString());
//        }
        assertEquals(expResult.size(), result.size());
        
        for (int i = 0; i < result.size(); i++) {
            assertTrue("Expected " + expResult.get(i).toString() + " but got " + result.get(i).toString(),
                        expResult.get(i).equals(result.get(i)));
        }
        
        // TODO Test deletion at the end? Does it make sense?
    }
}
