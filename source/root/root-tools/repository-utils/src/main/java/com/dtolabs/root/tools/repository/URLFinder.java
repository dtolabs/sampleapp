package com.dtolabs.root.tools.repository;

import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.XStream;

public class URLFinder {
	
    private static final Log LOG = LogFactory.getLog(URLFinder.class);
    
	public static void main(String[] args) throws Exception {
		
		String baseURL="http://repository-webexdemo.dtolabs.com/nexus/";
		String serviceLocation="service/local/artifact/maven/resolve";
		
	
		String groupId=args[0];
		String artifactId=args[1];
		String classifier=args[2];
		String type=args[3];
		String version=args[4];
		String repo=args[5];
		
		/*
		String groupId="com.dtolabs.webexdemo.sample";
		String artifactId="webApp";
		String classifier="";
		String type="war";
		String version="1.1-SNAPSHOT";
		String repo="inhouse.snapshots";
		*/
		
		String location = baseURL+serviceLocation+"?g="+groupId+"&a="+artifactId+"&c="+classifier+"&e="+type+"&v="+version+"&r="+repo;
		LOG.debug(location);
		
		InputStream in = HttpDownload.getDownloadInputStream(location);
		XStream xstream = new XStream();
		xstream.alias("artifact-resolution", ArtifactResolution.class);

		
        ArtifactResolution resolve = (ArtifactResolution) xstream.fromXML(in);
		
		String path = resolve.data.repositoryPath;
		
		String downloadPath = baseURL+"content/repositories/"+repo+path;
		
		System.out.println(downloadPath);
		
		
	}

}
