package com.dtolabs.sample.bo.ws;

import static org.junit.Assert.fail;

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



public class IntegrationTestPersonBOWs extends AbstractWsTest {

    private static final Log LOG = LogFactory.getLog(IntegrationTestPersonBOWs.class);

    @Autowired
    private PersonBO personBo;

    private Person person;

    @Before
    public void setUp() {
        person = new Person();
        person.setName("fasdasdas");
        person.setAge(100);
    }

    @Test
    public void testSaveName() {
        try {
            /** insert **/
            personBo.save(person);
            LOG.info(personBo.getPersons());
        } catch (Exception e) {
            fail("Could not save");
        }
    }

    @Test
    public void testSize() throws InvalidArgumentException, NoDataFoundException, ServiceUnAvailableException {

        int count = personBo.getPersonCount();

        LOG.info("Count of number of persons is " + count);

    }

    @Test
    public void testValidatePersonBo() {
        LOG.info("Person BO is " + personBo);
        Assert.notNull(personBo);
    }

}
