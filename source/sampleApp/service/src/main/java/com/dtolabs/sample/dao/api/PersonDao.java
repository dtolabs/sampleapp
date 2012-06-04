package com.dtolabs.sample.dao.api;

import java.util.List;

import com.dtolabs.sample.dto.Person;



public interface PersonDao {

    Person findByPersonId(long personId);

    int getPersonCount();

    List<Person> getPersons();

    Boolean save(Person person);
}
