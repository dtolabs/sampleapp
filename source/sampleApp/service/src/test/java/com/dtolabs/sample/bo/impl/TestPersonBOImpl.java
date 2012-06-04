package com.dtolabs.sample.bo.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.dtolabs.sample.bo.api.InvalidArgumentException;
import com.dtolabs.sample.bo.api.NoDataFoundException;
import com.dtolabs.sample.bo.api.PersonBO;
import com.dtolabs.sample.bo.api.ServiceUnAvailableException;
import com.dtolabs.sample.dto.Person;



public class TestPersonBOImpl extends AbstractServiceTest {

    private static final Log LOG = LogFactory.getLog(TestPersonBOImpl.class);

    @Autowired
    Person person;

    @Autowired
    PersonBO personBo;

    @Before
    public void setUp() {
        person.setName("Integrated Test");
        person.setAge(100);
    }

    @Test
    public void testSaveName() throws InvalidArgumentException, ServiceUnAvailableException, NoDataFoundException {
        /** insert **/
        personBo.save(person);
        LOG.info("After Saving Persons in the system are " + personBo.getPersons());

    }

    @Test
    public void testSize() throws InvalidArgumentException, NoDataFoundException, ServiceUnAvailableException {

        int count = personBo.getPersonCount();
        LOG.info("Count of number of persons is " + count);
        Assert.state(count >= 1);

    }

    @Test
    public void testGetNameById() throws InvalidArgumentException, NoDataFoundException, ServiceUnAvailableException {
        Person f = personBo.findByPersonId(1);
        Assert.state(f.getAge() == 100);
    }
}
