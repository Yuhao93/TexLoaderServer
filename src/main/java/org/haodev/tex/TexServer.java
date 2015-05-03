package org.haodev.tex;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Map;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TexServer extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      String qs = req.getQueryString();
      String[] params = qs.split("&");
      Map<String, String> map = new HashMap<String, String>();
      for (int i = 0; i < params.length; i++) {
        String[] parts = params[i].split("=");
        map.put(parts[0], URLDecoder.decode(parts[1], "UTF-8"));
      }
      String size = map.get("size");
      String formula = map.get("formula");
      String color = map.get("color");
      long cl = Long.parseLong(color, 16) & 0xFFFFFFFF;
      int r = (int) ((cl >> 16) & 0xFF);
      int g = (int) ((cl >> 8) & 0xFF);
      int b = (int) (cl & 0xFF);
      int a = (int) ((cl >> 24) & 0xFF);
      byte[] data = Tex.generateTexImage(formula, Float.parseFloat(size),
          r, g, b, a);
      resp.setStatus(HttpServletResponse.SC_OK);
      resp.setContentType("image/png");
      resp.setContentLength(data.length);
      OutputStream os = resp.getOutputStream();
      os.write(data);
      os.flush();
    } catch (Exception e) {
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
  }

  public static void main(String[] args) throws Exception{
    String port = System.getenv("PORT");
    if (port == null) {
      port = "8000";
    }
    Server server = new Server(Integer.valueOf(port));
    ServletContextHandler context =
        new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);
    context.addServlet(new ServletHolder(new TexServer()),"/tex");
    context.addServlet(new ServletHolder(
        new StaticServer("static/index.html", "text/html")), "/");
    context.addServlet(new ServletHolder(
        new StaticServer("static/script.js", "text/javascript")), "/script.js");
    context.addServlet(new ServletHolder(
        new StaticServer("static/texloader/src/texloader.js",
            "text/javascript")), "/tex.js");
    context.addServlet(new ServletHolder(
        new StaticServer("static/texloader/closure/TexLoader.js",
            "text/javascript")), "/tex-closure.js");
    context.addServlet(new ServletHolder(
        new StaticServer("static/style.css", "text/css")), "/style.css");
    server.start();
    server.join();
  }
}
