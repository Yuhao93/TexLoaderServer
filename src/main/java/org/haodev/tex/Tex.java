package org.haodev.tex;

import be.ugent.caagt.jmathtex.TeXConstants;
import be.ugent.caagt.jmathtex.TeXFormula;
import be.ugent.caagt.jmathtex.TeXIcon;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class Tex {
  public static byte[] generateTexImage(String formula, float size) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      TeXFormula texFormula = new TeXFormula(formula);
      TeXIcon icon = texFormula.createTeXIcon(TeXConstants.STYLE_DISPLAY, size);
      JComponent component = new JPanel();
      component.setForeground(Color.BLACK);
      BufferedImage image = new BufferedImage(icon.getIconWidth() + 10,
          icon.getIconHeight() + 10, BufferedImage.TYPE_INT_ARGB);
      Graphics graphics = image.getGraphics();
      icon.paintIcon(component, graphics, 5, 5);

      ImageIO.write(image, "PNG", baos);
      return baos.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
