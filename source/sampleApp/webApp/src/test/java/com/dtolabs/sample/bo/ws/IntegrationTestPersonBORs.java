package com.dtolabs.sample.bo.ws;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.dtolabs.sample.bo.api.InvalidArgumentException;
import com.dtolabs.sample.bo.api.NoDataFoundException;
import com.dtolabs.sample.bo.api.PersonBO;
import com.dtolabs.sample.bo.api.ServiceUnAvailableException;
import com.dtolabs.sample.dto.Person;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

public class IntegrationTestPersonBORs extends AbstractRsTest {

    private static final Log LOG = LogFactory.getLog(IntegrationTestPersonBORs.class);

    @Autowired
    private PersonBO personBo;

    private Person person;

    @Before
    public void setUp() {
    }

    @Test
    public void testGetPersons() throws InvalidArgumentException, NoDataFoundException, ServiceUnAvailableException {

        List<Person> persons = personBo.getPersons();
        LOG.info("Persons are " + persons);

    }

    @Test
    public void testSaveName() throws Exception {

        int initialCount = personBo.getPersonCount();
        LOG.info("Initial Count is " + initialCount);

        person = new Person();
        person.setName("AutomatedTest");
        person.setAge(100);

        XStream xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("Person", Person.class);
        String jsonPerson = xstream.toXML(person);
        Properties prop = new Properties();
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
        prop.load(in);
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://" + prop.getProperty("application.host") + ":" + prop.getProperty("application.port")
                + "/webApp/personRS/save");

        StringEntity reqEntity = null;

        reqEntity = new StringEntity(jsonPerson);
        reqEntity.setContentType("application/json");
        httppost.setEntity(reqEntity);

        httpclient.execute(httppost);
        int newCount = personBo.getPersonCount();

        LOG.info("New Count is " + newCount);

        LOG.info("After Saving Persons in the system are " + personBo.getPersons());

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