package org.bingo.model;

import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import lombok.Builder;
import lombok.Data;

/**
 * Grid Element of the PDF Document
 */
@Data
@Builder(toBuilder = true)
public class Grid {
    PdfFormXObject object;
    Transform transform;
    float cellWidth;
    float cellHeight;
    float cellPaddingX;
    float cellPaddingY;
    float cellGap;
    float labelHeight;
    float usableWidth;
}
