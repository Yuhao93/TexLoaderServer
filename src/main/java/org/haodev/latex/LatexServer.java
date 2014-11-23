package org.haodev.latex;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.haodev.latex.Latex;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Map;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LatexServer extends HttpServlet {
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
      byte[] data = Latex.generateLatexImage(formula, Float.parseFloat(size));
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
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);
    context.addServlet(new ServletHolder(new LatexServer()),"/latex");
    server.start();
    server.join();
  }
}
