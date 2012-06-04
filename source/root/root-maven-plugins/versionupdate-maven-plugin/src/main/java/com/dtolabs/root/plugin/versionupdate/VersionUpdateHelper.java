package com.dtolabs.root.plugin.versionupdate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.IOUtil;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jfrog.maven.annomojo.annotations.MojoParameter;


public abstract class VersionUpdateHelper extends AbstractMojo {

    @MojoParameter(readonly = true, required = true, expression = "${project}")
    private MavenProject project;

 
    public MavenProject getProject() {
        return project;
    }

    private SAXBuilder builder = new SAXBuilder();

    protected Document readPOM(File pomFile) throws IOException, JDOMException {

        InputStream stream = new FileInputStream(pomFile);
        return builder.build(stream);

    }

    protected void writePom(File pomFile, Document document) throws IOException {

        Writer writer = null;
        try {

            writer = new BufferedWriter(new FileWriter(pomFile));

            // ALL Poms should look the same. Format em
            Format format = Format.getPrettyFormat();
            format.setLineSeparator("\n");
            FormattedXMLOutputter out = new FormattedXMLOutputter(format);
            out.output(document, writer);

        } finally {
            writer.flush();
            IOUtil.close(writer);
        }
    }



    @Override
    public void execute() throws org.apache.maven.plugin.MojoExecutionException{
        File pomFile = new File(getProject().getBasedir(), "pom.xml");
        getLog().debug("Project is " + getProject());
       
            updateVersion(pomFile);
       
    }

    protected abstract void updateVersion(File pomFile) throws MojoExecutionException;

    protected void updatePomVersion(File pomFile, String version) throws MojoExecutionException {

        getLog().info("Updating " + pomFile + " with version " + version);

        try {
            Document doc = readPOM(pomFile);
            getLog().debug("Document is " + doc);
            Namespace ns = doc.getRootElement().getNamespace();
            getLog().debug("Namespace is " + ns);

            Element element = doc.getRootElement().getChild("version", ns);
            if (element == null) {
                element = doc.getRootElement().getChild("parent", ns).getChild("version", ns);
            }
            element.setText(version);

            writePom(pomFile, doc);

        } catch (IOException ioe) {
            throw new MojoExecutionException("Error in updating version ", ioe);
        } catch (JDOMException e) {
            throw new MojoExecutionException("Error in updating version ", e);
        }

    }

}
