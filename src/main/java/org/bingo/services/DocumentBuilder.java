package org.bingo.services;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DocumentBuilder {

    public static final Path OUTPUT_DIR = Paths.get("output");
    public static final Path ONE_PER_PAGE_OUTPUT_FILE = OUTPUT_DIR.resolve("FallNature_1PerPage.pdf");
    public static final Path TWO_PER_PAGE_OUTPUT_FILE = OUTPUT_DIR.resolve("FallNature_2PerPage.pdf");
    public static final Path CALLING_CARDS_TOKES_RULES_OUTPUT_FILE = OUTPUT_DIR.resolve("FallNature_CallingCards, Tokens, & Rules.pdf");

    public static void buildOnePerPageBingoCards (Path outputPath) throws IOException {
        PdfDocument onePerPageBingoCards = new PdfDocument(new PdfWriter(outputPath.toString()));
    }

    public static void buildTwoPerPageBingoCards () throws IOException {
        PdfDocument onePerPageBingoCards = new PdfDocument(new PdfWriter(TWO_PER_PAGE_OUTPUT_FILE.toString()));
    }

    public static void buildInstructionsTokensCallingCards () throws IOException {
        PdfDocument onePerPageBingoCards = new PdfDocument(new PdfWriter(CALLING_CARDS_TOKES_RULES_OUTPUT_FILE.toString()));
    }
}
