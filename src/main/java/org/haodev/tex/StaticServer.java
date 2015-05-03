package org.haodev.tex;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.net.URLDecoder;
import java.util.Map;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StaticServer extends HttpServlet {
  private final Path path;
  private final String responseType;

  public StaticServer(String file, String responseType) {
    this.path = Paths.get(file);
    this.responseType = responseType;
  }
  
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      byte[] data = Files.readAllBytes(path);
      resp.setStatus(HttpServletResponse.SC_OK);
      resp.setContentType(responseType);
      resp.setContentLength(data.length);
      OutputStream os = resp.getOutputStream();
      os.write(data);
      os.flush();
    } catch (Exception e) {
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
  }
}
