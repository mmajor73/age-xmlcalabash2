package com.xmlcalabash.drivers;

import com.xmlcalabash.core.XProcConfiguration;
import com.xmlcalabash.core.XProcRuntime;
import com.xmlcalabash.runtime.XOutput;
import com.xmlcalabash.runtime.XPipeline;
import net.sf.saxon.Configuration;
import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;
import org.xml.sax.InputSource;

import javax.xml.transform.sax.SAXSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

/**
 * Created by ndw on 5/14/15.
 *
 * This class attempts to test how XML Calabash performs when it's embedded in other applications
 */
public class EmbeddedTest {
    String pipeline_xml = "<p:declare-step xmlns:p=\"http://www.w3.org/ns/xproc\" version=\"1.0\"\n" +
            "                name=\"main\">\n" +
            "<p:input port=\"source\">\n" +
            "    <p:inline><doc/></p:inline>\n" +
            "</p:input>\n" +
            "<p:output port=\"result\"/>\n" +
            "\n" +
            "<p:identity>\n" +
            "  <p:input port=\"source\">\n" +
            "    <p:pipe step=\"main\" port=\"source\" />\n" +
            "  </p:input>\n" +
            "</p:identity>\n" +
            "\n" +
            "</p:declare-step>\n";
    
    String pipeline2_xml = "<p:declare-step xmlns:p=\"http://www.w3.org/ns/xproc\" version=\"1.0\"\n" +
            "                name=\"main\">\n" +
            "<p:input port=\"source\">\n" +
            "    <p:inline><doc/></p:inline>\n" +
            "</p:input>\n" +
            "<p:output port=\"result\" primary=\"true\"/>\n" +
            "<p:output port=\"status\" primary=\"false\">\n" +
            "    <p:pipe step=\"step1\" port=\"status\" />\n" +
            "</p:output>" + 
            "<p:majorTest name=\"step1\" configurationfile=\"test1.xml\">\n" +
            "  <p:input port=\"source\">\n" +
            "    <p:pipe step=\"main\" port=\"source\" />\n" +
            "  </p:input>\n" +
            "  <p:input port=\"parameters\">\n" +
            "    <p:empty/>\n" +
            "  </p:input>\n" +
            "</p:majorTest>\n" +
            "</p:declare-step>\n";

    public static void main(String[] args) throws SaxonApiException, IOException, URISyntaxException {
        EmbeddedTest test = new EmbeddedTest();
        test.run();
    }

    public void run() throws SaxonApiException {
        Processor saxon = new Processor(false);
        XProcConfiguration config = new XProcConfiguration(saxon);
        XProcRuntime runtime = new XProcRuntime(config);

        InputStream stream = new ByteArrayInputStream(pipeline2_xml.getBytes());
        DocumentBuilder builder = saxon.newDocumentBuilder();
        XdmNode pipeline_doc = builder.build(new SAXSource(new InputSource(stream)));

        XPipeline pipeline = runtime.use(pipeline_doc);
        pipeline.run();
        
        XOutput pipeline_output_result = pipeline.getOutput("result");
        XOutput pipeline_output_status = pipeline.getOutput("status");
        System.out.println(pipeline_output_result.getReader().documents().get(0).toString());
        System.out.println(pipeline_output_status.getReader().documents().get(0).toString());  
    }

}
