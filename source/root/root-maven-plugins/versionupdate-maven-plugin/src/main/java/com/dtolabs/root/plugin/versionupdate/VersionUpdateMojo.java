package com.dtolabs.root.plugin.versionupdate;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoParameter;

@MojoGoal("versionUpdate")
public class VersionUpdateMojo extends VersionUpdateHelper {

    @MojoParameter(required = true, expression = "${changeToVersion}")
    private String changeToVersion = null;

    @Override
    protected void updateVersion(File pomFile) throws MojoExecutionException {

        updatePomVersion(pomFile, changeToVersion);

    }
}
