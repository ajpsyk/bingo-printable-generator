package org.bingo.model;

import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;

public record GridLayout(
        PdfFormXObject grid,
        float cellWidth,
        float cellHeight,
        float cellPaddingX,
        float cellPaddingY,
        float cellGap,
        float labelHeight,
        float usableWidth
) { }
