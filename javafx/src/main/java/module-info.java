module at.htl.bibliotheksverwaltung {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // Wenn du FXML-Controller per Reflection lädst, brauchst du "opens"
    opens at.htl.bibliotheksverwaltung.controller to javafx.fxml;
    opens at.htl.bibliotheksverwaltung.view to javafx.fxml;
    opens at.htl.bibliotheksverwaltung to javafx.graphics;

    // Exporte, damit JavaFX deine Klassen laden kann
    exports at.htl.bibliotheksverwaltung.controller;
    exports at.htl.bibliotheksverwaltung.model;
    exports at.htl.bibliotheksverwaltung.view;
}
