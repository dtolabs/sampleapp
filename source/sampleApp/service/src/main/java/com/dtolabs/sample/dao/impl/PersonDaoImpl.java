package com.dtolabs.sample.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dtolabs.sample.dao.api.PersonDao;
import com.dtolabs.sample.dto.Person;



@Repository("personDao")
public class PersonDaoImpl extends CustomHibernateDaoSupport implements PersonDao {

    @Override
    public Person findByPersonId(long personId) {
        @SuppressWarnings("unchecked")
        List<Person> list = getHibernateTemplate().find("from Person where personId=?", personId);
        return list.get(0);
    }

    @Override
    public int getPersonCount() {
        return getPersons().size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Person> getPersons() {

        return getHibernateTemplate().find("from Person");

    }

    @Override
    public Boolean save(Person person) {
        try {
            getHibernateTemplate().save(person);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
