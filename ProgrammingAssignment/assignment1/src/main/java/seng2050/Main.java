package seng2050;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class Main {
  public static void main(String[] args) {
    // Create Tomcat instance
    Tomcat tomcat = new Tomcat();
    tomcat.setPort(8080); // Set server port

    // Ensure a base directory for Tomcat
    tomcat.setBaseDir("temp");

    // Create and configure context
    Context ctx = tomcat.addWebapp("", new File("webapps/ROOT").getAbsolutePath());
    System.out.println("Root path (web apps): " + new File("webapps/ROOT").getAbsolutePath());

    File rootDir = new File("webapps/ROOT");
    if (rootDir.exists() && rootDir.isDirectory()) {
      for (String fileName : rootDir.list()) {
        System.out.println("Found: " + fileName);
      }
    } else {
      System.out.println("Directory does not exist or is not a directory.");
    }

    if (ctx == null) {
      throw new RuntimeException("Tomcat context initialization failed!");
    }

    // Add and map the servlet
    // Add a servlet
      tomcat.addServlet("", "LoginServlet", new LoginServlet());
      tomcat.addServlet("", "SemesterServlet", new SemesterServlet()); 
      tomcat.addServlet("", "EnrollServlet", new EnrollServlet()); 
    
    // Map the servlet
      ctx.addServletMappingDecoded("/login", "LoginServlet");
      ctx.addServletMappingDecoded("/SemesterServlet", "SemesterServlet");
      ctx.addServletMappingDecoded("/EnrollServlet", "EnrollServlet");

    // Start Tomcat
    try {
      tomcat.getConnector();
      tomcat.start();
      System.out.println("Tomcat started successfully.");
      tomcat.getServer().await();
    } catch (LifecycleException e) {
      e.printStackTrace();
    }
  }
}