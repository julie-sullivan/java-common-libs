package org.opencb.commons.bioformats.commons.core.connectors.ped.readers;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.opencb.commons.bioformats.commons.core.connectors.ped.writers.PedDataWriter;
import org.opencb.commons.bioformats.commons.core.connectors.ped.writers.PedSqliteDataWriter;
import org.opencb.commons.bioformats.commons.core.feature.Pedigree;
import org.opencb.commons.bioformats.commons.core.variant.io.Vcf4Reader;
import org.opencb.commons.bioformats.commons.exception.FileFormatException;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: aleman
 * Date: 8/31/13
 * Time: 8:40 PM
 * To change this template use File | Settings | File Templates.
 */

public class PedFileDataReaderTest {
    private Long start, end;
    private Vcf4Reader vcf;
    private String path = "/opt/data/";
    private String pedFile;
    private String dbFilename;
    private String pathStats;




    @Rule
    public TestName name = new TestName();


    @Before
    public void setUp() throws Exception {


        pedFile = path + "big.ped";
        pathStats = path + "jstats/";
        dbFilename = pathStats + "stats.db";
        start = System.currentTimeMillis();


    }

    @After
    public void tearDown() throws Exception {




    }

    @Test
    public void test() throws IOException, FileFormatException {
        PedDataReader pedReader = new PedFileDataReader(pedFile);
        PedDataWriter pedWriter = new PedSqliteDataWriter(dbFilename);
        Pedigree ped;

        pedReader.open();
        pedReader.pre();
        ped = pedReader.read();
        pedReader.post();
        pedReader.close();

        pedWriter.open();
        pedWriter.pre();
        pedWriter.write(ped);
        pedWriter.post();
        pedWriter.close();

    }
}
