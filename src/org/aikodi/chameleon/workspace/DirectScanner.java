package org.aikodi.chameleon.workspace;

/**
 * A scanner that does nothing by default. All document loades must be added
 * externally.
 * 
 * @author Marko van Dooren
 */
public class DirectScanner extends DocumentScannerImpl {

   @Override
   public String label() {
      return "Direct scanner";
   }

}