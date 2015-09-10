package com.doubletuan.sns.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A SystemConfiguration.
 */
@Entity
@Table(name = "SYSTEMCONFIGURATION")
public class SystemConfiguration implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "identity")
    private String identity;

    @Column(name = "current_value")
    private String currentValue;

    @Column(name = "column1")
    private String column1;

    @Column(name = "value1")
    private String value1;

    @Column(name = "column2")
    private String column2;

    @Column(name = "value2")
    private String value2;

    @Column(name = "column3")
    private String column3;

    @Column(name = "value3")
    private String value3;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public String getColumn1() {
        return column1;
    }

    public void setColumn1(String column1) {
        this.column1 = column1;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getColumn2() {
        return column2;
    }

    public void setColumn2(String column2) {
        this.column2 = column2;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getColumn3() {
        return column3;
    }

    public void setColumn3(String column3) {
        this.column3 = column3;
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SystemConfiguration systemConfiguration = (SystemConfiguration) o;

        if ( ! Objects.equals(id, systemConfiguration.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SystemConfiguration{" +
                "id=" + id +
                ", identity='" + identity + "'" +
                ", currentValue='" + currentValue + "'" +
                ", column1='" + column1 + "'" +
                ", value1='" + value1 + "'" +
                ", column2='" + column2 + "'" +
                ", value2='" + value2 + "'" +
                ", column3='" + column3 + "'" +
                ", value3='" + value3 + "'" +
                '}';
    }
}
