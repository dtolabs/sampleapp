package com.dtolabs.sample.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.stereotype.Component;

@Entity
@XmlRootElement(name = "Person")
@Component
public class Person implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int age;

    private String name;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long personId;

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public long getPersonId() {
        return personId;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    @Override
    public String toString() {
        return "Person [personId=" + personId + ", name=" + name + ", age=" + age + "]";
    }

}
