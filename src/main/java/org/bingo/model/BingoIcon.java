package org.bingo.model;

import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;

public record BingoIcon(PdfFormXObject label, PdfFormXObject image) {}
