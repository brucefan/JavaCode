package com.brucefan.code.vs;

import com.brucefan.model.Person;
import com.brucefan.model.PersonDto;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bruce01.fan on 2015/11/3.
 */
public class VsDemo {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {

        List<Person> allPersons = genPerson(100000);

        beanUtilsTest(allPersons);

        OrikaTest(allPersons);
    }

    private static void beanUtilsTest(List<Person> allPersons) throws IllegalAccessException, InvocationTargetException {
        long start_1 = System.currentTimeMillis();
        for (Person p : allPersons) {
            PersonDto dto = new PersonDto();
            BeanUtils.copyProperties(dto, p);
        }
        long end_1 = System.currentTimeMillis();
        System.out.println((end_1 - start_1));
    }

    private static void OrikaTest(List<Person> allPersons) {
        long start_1 = System.currentTimeMillis();
        MapperFactory factory = new DefaultMapperFactory.Builder().build();
        factory.classMap(Person.class, PersonDto.class)
                .byDefault()
                .register();
        MapperFacade mapper = factory.getMapperFacade();

        for (Person p : allPersons) {
            PersonDto dto = mapper.map(p, PersonDto.class);
        }

        long end_1 = System.currentTimeMillis();
        System.out.println((end_1 - start_1));
    }

    static List<Person> genPerson(int size) {
        List<Person> list = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Person p = new Person();
            p.setName("fgm_" + i);
            p.setAge(20);
            p.setBrithday(new Date());

            list.add(p);
        }

        return list;
    }
}
