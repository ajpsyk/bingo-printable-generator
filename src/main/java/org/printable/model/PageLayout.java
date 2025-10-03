package org.printable.model;


import lombok.Builder;
import lombok.Data;

/**
 * Container for page dimensions and print-safe boundaries.
 */
@Data
@Builder(toBuilder=true)
public class PageLayout {
    float width;
    float height;
    float printSafeWidth;
    float printSafeHeight;
    float marginTop;
    float marginRight;
    float marginBottom;
    float marginLeft;
}
