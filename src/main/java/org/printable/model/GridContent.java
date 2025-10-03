package org.printable.model;

import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * Container for labels and icons that populate grid cells
 */
@Data
@Builder
public class GridContent {
        PdfFormXObject label;
        @NonNull PdfFormXObject icon;
}
