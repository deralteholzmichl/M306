module com.ubs.controller {
    requires javafx.graphics;
    requires org.hibernate.orm.core;
    requires java.persistence;
    requires java.naming;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.ubs.controller to javafx.fxml;
    exports com.ubs.controller;
    exports com.ubs.Model;
    opens com.ubs.Model;
    //opens com.ubs.dao to javafx.fxml;
}