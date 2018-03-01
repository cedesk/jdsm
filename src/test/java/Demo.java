import edu.carleton.tim.jdsm.DesignStructureMatrix;
import edu.carleton.tim.jdsm.dependency.Dependency;
import edu.carleton.tim.jdsm.dependency.DependencyDSM;
import edu.carleton.tim.jdsm.dependency.analysis.ClusteredCost;
import edu.carleton.tim.jdsm.dependency.analysis.PropagationCost;
import edu.carleton.tim.jdsm.dependency.analysis.SVGOutput;
import edu.carleton.tim.jdsm.dependency.provider.DependencyFinderDSMProvider;
import javolution.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Demo {

    public static void main(String args[]) throws SAXException, ParserConfigurationException, FileNotFoundException, JAXBException, XMLStreamException {
        //load from Dependency Finder XML
        String inputFileName = args[0];
        String filterExpression = args[1];
        DesignStructureMatrix<Dependency> dsm = DependencyFinderDSMProvider.loadDesignStructureMatrix(inputFileName, filterExpression);

        //save the DSM to SVG
        SVGOutput.printDsm(dsm, new FileOutputStream("dsm_original.svg"));

        //Save to a proprietary XML format
        dsm.saveToXml(new FileOutputStream("dsm_original.xml"));
        //Load the proprietary XML
        DependencyDSM newDsmInstance = new DependencyDSM();
        newDsmInstance.loadFromXml(new FileInputStream("dsm_original.xml"));

        //Compute propagation cost
        double propagationCost = PropagationCost.computePropagationCost(dsm);

        //Compute clustered  cost
        ClusteredCost.ClusteredCostResult clusteredCostResult =
                ClusteredCost.computeClusteredCost(dsm, 0.1d);
        int nrVerticalBusses = clusteredCostResult.getVerticalBusses().size();
        long clusteredCost = clusteredCostResult.getClusteredCost();
        double relativeClusteredCost = clusteredCostResult.getRelativeClusteredCost();

        DesignStructureMatrix costResultDsm = clusteredCostResult.getDsm();
        costResultDsm.saveToXml(new FileOutputStream("dsm_clustered.xml"));

        //save the DSM to SVG
        SVGOutput.printDsm(costResultDsm, new FileOutputStream("dsm_clustered.svg"));

    }
}
