package org.printable.model;

import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import lombok.Builder;
import lombok.Data;

/**
 * Frame Element of the PDF Document
 */
@Data
@Builder(toBuilder = true)
public class Frame {
    PdfFormXObject object;
    @Builder.Default Transform transform = Transform.builder().build();
}
