package org.bingo.ui;

import javafx.beans.property.Property;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Predicate;

@AllArgsConstructor
@Getter
public class FieldSpec<T> {
    String label;
    Property<T> property;
    FieldType type;
    //Predicate<T> validator;
}
