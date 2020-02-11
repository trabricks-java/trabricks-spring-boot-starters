package io.trabricks.boot.web.vcard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import org.junit.Ignore;
import org.junit.Test;

public class VCardTest {

  @Test
  @Ignore
  public void createVCard() throws IOException {
    File f = new File("contact.vcf");
    FileOutputStream fop = new FileOutputStream(f);

    if (f.exists()) {
      String str = "BEGIN:VCARD\n" +
          "VERSION:4.0\n" +
          "N:Gump;Forrest;;;\n" +
          "FN:Forrest Gump\n" +
          "ORG:Bubba Gump Shrimp Co.\n" +
          "TITLE:Shrimp Man\n" +
          "TEL;TYPE=work,voice;VALUE=uri:tel:+1-111-555-1212\n" +
          "TEL;TYPE=home,voice;VALUE=uri:tel:+1-404-555-1212\n" +
          "EMAIL:forrestgump@example.com\n" +
          "REV:20080424T195243Z\n" +
          "END:VCARD";
      fop.write(str.getBytes());
      //Now read the content of the vCard after writing data into it
      BufferedReader br = null;
      String sCurrentLine;
      br = new BufferedReader(new FileReader("contact.vcf"));
      while ((sCurrentLine = br.readLine()) != null) {
        System.out.println(sCurrentLine);
      }
      //close the output stream and buffer reader
      fop.flush();
      fop.close();
      System.out.println("The data has been written");
    } else {
      System.out.println("This file does not exist");
    }
  }

}
