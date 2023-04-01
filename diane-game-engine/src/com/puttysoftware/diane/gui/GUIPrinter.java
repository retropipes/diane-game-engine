/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.gui;

import java.awt.image.BufferedImage;
import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.PrintException;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.diane.internal.PrivateErrorString;
import com.puttysoftware.diane.internal.PrivateStrings;

public class GUIPrinter {
    public static void printScreen() {
        try {
            final var board = MainWindow.mainWindow().content();
            final var d = board.getPreferredSize();
            final var bi = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);
            board.paintComponents(bi.createGraphics());
            final var baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "PNG", baos);
            final var data = baos.toByteArray();
            final var bais = new ByteArrayInputStream(data);
            final PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            final DocFlavor flavor = DocFlavor.INPUT_STREAM.PNG;
            final var pj = PrinterJob.getPrinterJob();
            final var okay = pj.printDialog(pras);
            if (okay) {
                final var service = pj.getPrintService();
                final var job = service.createPrintJob();
                final DocAttributeSet das = new HashDocAttributeSet();
                final Doc doc = new SimpleDoc(bais, flavor, das);
                job.print(doc, pras);
            }
        } catch (final IOException | PrintException | NullPointerException npe) {
            CommonDialogs.showErrorDialog(PrivateStrings.error(PrivateErrorString.PRINT_ERROR_MESSAGE),
                    PrivateStrings.error(PrivateErrorString.PRINT_ERROR_TITLE));
        }
    }

    private GUIPrinter() {
        // Do nothing
    }
}
