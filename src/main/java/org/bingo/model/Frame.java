package org.bingo.model;

import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * Frame Element of the PDF Document
 */
@Data
@Builder(toBuilder = true)
public class Frame {
    @NonNull PdfFormXObject object;
    @Builder.Default Transform transform = Transform.builder().build();
}
