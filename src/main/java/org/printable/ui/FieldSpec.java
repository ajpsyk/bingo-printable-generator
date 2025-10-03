package org.printable.ui;

import javafx.beans.property.Property;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FieldSpec<T> {
    String label;
    Property<T> property;
    FieldType type;
    //Predicate<T> validator;
}
