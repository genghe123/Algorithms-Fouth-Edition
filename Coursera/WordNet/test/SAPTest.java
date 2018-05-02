import WordNet.SAP;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SAPTest {

    private SAP sap;
    private int v;
    private int w;
    private int desiredLength;
    private int desiredAncestor;

    public SAPTest(String filepath, int v, int w, int desiredLength, int desiredAncestor) {
        this.v = v;
        this.w = w;
        this.desiredLength = desiredLength;
        this.desiredAncestor = desiredAncestor;
        sap = new SAP(new Digraph(new In(filepath)));
    }

    @Parameterized.Parameters
    @SuppressWarnings("unchecked")
    public static Collection prepareData() {
        Object[][] objects = {
                {"wordnet/digraph1.txt", 3, 3, 0, -100},
                {"wordnet/digraph2.txt", 2, 0, 4, 0},
                {"wordnet/digraph2.txt", 1, 5, 2, -100},
                {"wordnet/digraph3.txt", 10, 3, -1, -100},
                {"wordnet/digraph3.txt", 7, 9, 2, 9},
                {"wordnet/digraph3.txt", 2, 6, 2, -100},
                {"wordnet/digraph4.txt", 1, 4, 3, -100},
                {"wordnet/digraph5.txt", 17, 1, -1, -100},
                {"wordnet/digraph5.txt", 17, 7, 5, -100},
                {"wordnet/digraph5.txt", 17, 21, 5, -100},
                {"wordnet/digraph9.txt", 4, 2, 3, 2},
                {"wordnet/digraph9.txt", 4, 7, 3, -100},
                {"wordnet/digraph6.txt", 0, 5, 5, -100},
                {"wordnet/digraph6.txt", 5, 0, 5, -100},


        };
        return Arrays.asList(objects);
    }

    @Test
    public void test1() {
        if (desiredLength >= -1)
            Assert.assertEquals(desiredLength, sap.length(v, w));
        if (desiredAncestor >= -1)
            Assert.assertEquals(desiredAncestor, sap.ancestor(v, w));
    }

}