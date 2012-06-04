package com.dtolabs.sample.bo.impl;

import java.util.List;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dtolabs.sample.bo.api.PersonBO;
import com.dtolabs.sample.dao.api.PersonDao;
import com.dtolabs.sample.dto.Person;



@Service("personBo")
@WebService(endpointInterface = "com.dtolabs.sample.bo.api.PersonBO")
public class PersonBOImpl implements PersonBO {

    @Autowired
    private PersonDao personDao;

    @Override
    public Person findByPersonId(long personId) {
        return personDao.findByPersonId(personId);
    }

    @Override
    public int getPersonCount() {
        return personDao.getPersonCount();
    }

    @Override
    public List<Person> getPersons() {
        return personDao.getPersons();
    }

    @Override
    public Boolean save(Person person) {
        return personDao.save(person);

    }

}
