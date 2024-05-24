module com.ubs.controller {
    requires javafx.graphics;
    requires org.hibernate.orm.core;
    requires java.persistence;
    requires java.naming;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires java.xml.bind;

    opens com.ubs.controller to javafx.fxml;
    exports com.ubs.controller;
    exports com.ubs.Model.sdat;
    exports com.ubs.Model.esl;
    opens com.ubs.Model.sdat;
    opens com.ubs.Model.esl;
    //opens com.ubs.dao to javafx.fxml;
}