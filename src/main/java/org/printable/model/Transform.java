package org.printable.model;

import lombok.Builder;
import lombok.Data;

/**
 * Holds layout data for PDF objects.
 */
@Data
@Builder(toBuilder = true)
public class Transform {
    @Builder.Default float scaleX = 1f;
    @Builder.Default float skewY = 0f;
    @Builder.Default float skewX = 0f;
    @Builder.Default float scaleY = 1f;
    @Builder.Default float positionX = 0f;
    @Builder.Default float positionY = 0f;
}
