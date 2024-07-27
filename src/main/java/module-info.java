module com.marjax.finansys {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.base;
    requires fontawesomefx;

    opens com.marjax.finansys to javafx.fxml;
    opens com.marjax.finansys.model to javafx.base;
    exports com.marjax.finansys;    
}
