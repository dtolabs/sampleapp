package com.dtolabs.root.tools.repository;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class HttpDownload {

    private static final Log LOG = LogFactory.getLog(HttpDownload.class);
    
    private static HttpURLConnection getConnection(String location) throws IOException {

        // LOG.info("Getting " + location);
        URL url = new URL(location);
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        huc.setRequestMethod("GET");
        huc.setInstanceFollowRedirects(true);
        return huc;

    }

    public static InputStream getDownloadInputStream(String location, String username, String password) throws IOException {

        InputStream is = null;
        HttpURLConnection huc = getConnection(location);

        huc.connect();

        try {
            is = huc.getInputStream();
        } catch (FileNotFoundException fe) {
            throw new IOException("Could not find file: " + location);
        }
        int code = huc.getResponseCode();
        // LOG.info("Reponse code is " + code);
        // The server agreed to send us the file
        if (code != HttpURLConnection.HTTP_OK) {
            throw new IOException();
        }

        return is;

    }

    public static InputStream getDownloadInputStream(String location) throws IOException {
        return getDownloadInputStream(location, null, null);
    }

    public static InputStream getDownloadInputStream(URL url) throws IOException {
        return getDownloadInputStream(url.toString(), null, null);
    }

    public static String getFileNameToDownload(String location) {
        int idx = location.lastIndexOf("/");
        return location = location.substring(idx + 1);
    }

    public static long getContentLength(String location) {

        long contentLength = 0;
        try {

            HttpURLConnection huc = getConnection(location);
            contentLength = huc.getContentLength();
            huc.disconnect();

        } catch (IOException ioe) {
            LOG.info("\nCould not open connection to: " + location);
            ioe.printStackTrace();
            System.exit(1);
        }
        return contentLength;
    }

    public static File downloadFile(String location, String destinationDir) throws IOException {

        String fileNameToDownload = getFileNameToDownload(location);

        InputStream is = getDownloadInputStream(location);

        File output = new File(destinationDir, fileNameToDownload);
        OutputStream outputStream = null;

        if (is != null) {
            outputStream = new BufferedOutputStream(new FileOutputStream(output));
            LOG.info("Downloading...");
            long fileSize = getContentLength(location);
            LOG.info("File Size: " + fileSize / (1024) + " KB");
            // The server agreed to send us the file
            copyInputStream(is, outputStream);
        } else {
            LOG.info("Download NOT ALLOWED!");
        }

        return output;

    }

    public static void copyInputStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[4096];
        int len;
        int transferredBytes = 0;

        while ((len = in.read(buffer)) >= 0) {
            out.write(buffer, 0, len);
            transferredBytes++;
            if (transferredBytes == 128) {
                System.out.print(".");
                transferredBytes = 0;
            }
        }
        LOG.info("\n");

        out.flush();
        in.close();
        out.close();
    }

    public static void downloadFile(String[] locations, String destinationDir) throws IOException {

        for (int i = 0; i < locations.length; i++) {
            downloadFile(locations[i], destinationDir);
        }
    }

    public static String getURLContents(String location) throws IOException {

        InputStream is = getDownloadInputStream(location);
        StringBuilder htmlString = new StringBuilder();

        // The server agreed to send us the file
        if (is != null) {

            String inputLine;
            BufferedReader in;

            in = new BufferedReader(new InputStreamReader(is));
            while ((inputLine = in.readLine()) != null) {
                htmlString.append(inputLine);
            }

        } else {
            LOG.info("Download NOT ALLOWED!");

        }
        is.close();
        return htmlString.toString();

    }

    public static void main(String[] args) throws IOException {
        getDownloadInputStream("https://dummy.test.com/trunk/pom.xml", "ServiceAccount", "welcome123");
    }
}
