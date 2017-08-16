/*
Copyright (c) 2008, Roberto Milev
All rights reserved.

Redistribution and use in source and binary forms, with or 
without modification, are permitted provided that the following 
conditions are met:

    * Redistributions of source code must retain the above 
      copyright notice, this list of conditions and the following 
      disclaimer.
    * Redistributions in binary form must reproduce the above 
      copyright notice, this list of conditions and the following 
      disclaimer in the documentation and/or other materials 
      provided with the distribution.
    * Neither the name of the Carleton University nor the names 
      of its contributors may be used to endorse or promote 
      products derived from this software without specific prior 
      written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND 
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, 
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
*/
package edu.carleton.tim.jdsm.dependency.analysis;

import edu.carleton.tim.jdsm.DesignStructureMatrix;
import edu.carleton.tim.jdsm.dependency.Dependency;
import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.io.PrintStream;


/**
 * The Class SVGOutput. Prints the DSM to a Scalable Vector Graphic (SVG)
 * format.
 *
 * @author Roberto Milev
 */
public class SVGOutput {

    /**
     * The logger.
     */
    private static Logger logger = Logger.getLogger(SVGOutput.class);

    /**
     * Prints the DSM to a Scalable Vector Graphic (SVG) format.
     *
     * @param dsm          the DSM
     * @param outputStream the output stream to print to
     */
    public static void printDsm(DesignStructureMatrix<Dependency> dsm, OutputStream outputStream) {
        PrintStream outStream = new PrintStream(outputStream);
        printHeader(outStream, dsm.getMap().length, dsm.getMap().length);

        for (String currentPackage : dsm.getClusterStartPositionMappings().keySet()) {

            printRectangle(outStream,
                    dsm.getClusterStartPositionMappings().get(currentPackage),
                    dsm.getClusterStartPositionMappings().get(currentPackage),
                    dsm.getClusterEndPositionMappings().get(currentPackage)
                            - dsm.getClusterStartPositionMappings().get(currentPackage),
                    dsm.getClusterEndPositionMappings().get(currentPackage)
                            - dsm.getClusterStartPositionMappings().get(currentPackage));
        }

        for (int i = 0; i < dsm.getMap().length; i++) {
            for (int j = 0; j < dsm.getMap().length; j++) {
                if (dsm.getMap()[i][j].booleanValue()) {
                    printRectangle(outStream, i, j, 1, 1);
                }
            }
        }

        printFooter(outStream);
        outStream.close();
        logger.info("Finshed printing DSM to SVG.");
    }

    /**
     * Prints the footer.
     *
     * @param printStream the print stream
     */
    private static void printFooter(PrintStream printStream) {
        printStream.println("</g>");
        printStream.println("</svg>");
    }

    /**
     * Prints the header.
     *
     * @param printStream the print stream
     * @param width       the width
     * @param height      the height
     */
    private static void printHeader(PrintStream printStream, int width, int height) {
        printStream.println("<?xml version=\"1.0\" standalone=\"no\"?>");
        printStream.println("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" " +
                "\"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">");
        printStream.println("<svg " +
                "width=\"" + width + "px\" " +
                "height=\"" + height + "px\" " +
                "version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">");
        printStream.println("<g fill=\"none\" stroke=\"black\" stroke-width=\"0.25\" >");
    }

    /**
     * Prints the rectangle.
     *
     * @param printStream the print stream
     * @param x           the x
     * @param y           the y
     * @param width       the width
     * @param height      the height
     */
    private static void printRectangle(PrintStream printStream, int x, int y,
                                       int width, int height) {
        // coordinates in SVG are inverse from the matrix coordinates
        printStream.println("    <rect width=\"" + width + "\"  " +
                "height=\"" + height + "\" " +
                "x=\"" + y + "\" " +
                "y=\"" + x + "\" " +
                (width == 1 && height == 1 ? " fill=\"black\" " : "") + "/>");

    }
}
