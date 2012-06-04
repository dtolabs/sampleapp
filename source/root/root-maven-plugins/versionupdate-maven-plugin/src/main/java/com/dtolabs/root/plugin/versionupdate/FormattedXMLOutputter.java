package com.dtolabs.root.plugin.versionupdate;

import java.io.IOException;
import java.io.Writer;

import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * 
 * Override some methods in XMLOutputter since the comments outside root element don't get tracked This causes all comments outside to be
 *         in one single line.
 */

public class FormattedXMLOutputter extends XMLOutputter {

    FormattedXMLOutputter(Format f) {
        super(f);
    }

    @Override
    protected void printComment(Writer out, Comment comment) throws IOException {
        super.printComment(out, comment);
        if (comment.getParent() instanceof Document) {
            out.write(getFormat().getLineSeparator());
        }
    }

}
