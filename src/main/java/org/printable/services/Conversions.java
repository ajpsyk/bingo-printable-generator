package org.printable.services;

/**
 * Helper methods for converting measurements to PDF standards
 */
public class Conversions {
    public static float inchesToPoints(float inches) {
        return inches * 72f;
    }
}
