/**
 * 
 */
package com.xmlcalabash.library;

import java.io.StringReader;
import java.util.Hashtable;

import javax.xml.transform.stream.StreamSource;

import com.xmlcalabash.core.XMLCalabash;
import com.xmlcalabash.core.XProcConstants;
import com.xmlcalabash.core.XProcRuntime;
import com.xmlcalabash.io.ReadablePipe;
import com.xmlcalabash.io.WritablePipe;
import com.xmlcalabash.model.RuntimeValue;
import com.xmlcalabash.runtime.XAtomicStep;
import com.xmlcalabash.util.TreeWriter;

import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XdmNode;

/**
 * @author EVOFORGE\mmajor
 *
 */
@XMLCalabash(
        name = "p:majorTest",
        type = "{http://www.w3.org/ns/xproc}majorTest")
public class majorTest extends DefaultStep {
	private static final QName _configurationfile = new QName("","configurationfile");
    Hashtable<QName,String> parameters = new Hashtable<QName,String> ();
    protected static final String logger = "org.xproc.library.majorTest";
    private ReadablePipe source = null;
    private WritablePipe result = null;
    private WritablePipe status = null;
	/**
	 * @param runtime
	 * @param step
	 */
	public majorTest(XProcRuntime runtime, XAtomicStep step) {
		super(runtime, step);
		// TODO Auto-generated constructor stub
	}
	
	public void setInput(String port, ReadablePipe pipe) {
        source = pipe;
    }
	
	public void setOutput(String port, WritablePipe pipe) {
		if ("result".equals(port))
          result = pipe;
		else
	      status = pipe;
    }
	
	public void setParameter(QName name, RuntimeValue value) {
        parameters.put(name, value.getString());
    }
	
	public void reset() {
		parameters.clear();
		source.resetReader();
        status.resetWriter();
        result.resetWriter();
    }
	
	public void run() throws SaxonApiException {
	        super.run();
	        
	
     
     String guardmessage = source.read().toString();
     
     Processor proc = new Processor(false);
     DocumentBuilder builder = proc.newDocumentBuilder();

     // Load the XML document.
     StringReader reader = new StringReader(guardmessage);
     XdmNode newGuardMessage = builder.build(new StreamSource(reader));

     result.write(newGuardMessage);
     
     String value = getOption(_configurationfile).getString();
 	
	 TreeWriter tree = new TreeWriter(runtime);
     tree.startDocument(step.getNode().getBaseURI());
     tree.addStartElement(_configurationfile);
     tree.startContent();
     tree.addText("" + value);
     tree.addEndElement();
     tree.endDocument();
     status.write(tree.getResult());

	}
}
