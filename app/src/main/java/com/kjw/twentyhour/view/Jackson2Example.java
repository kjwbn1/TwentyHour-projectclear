package com.kjw.twentyhour.view;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjw.twentyhour.listener.StoreBranch;
import com.kjw.twentyhour.listener.StoreData;
import com.kjw.twentyhour.model.Staff;

public class Jackson2Example {

    public static void main(String[] args) {
        Jackson2Example obj = new Jackson2Example();
        obj.run();
    }

    public void run() {


        ObjectMapper mapper = new ObjectMapper();

//        Staff staff = createDummyObject();

        StoreBranch storeBranch = createDummy();


        try {
            // Convert object to JSON string and save into a file directly

            mapper.writeValue(new File("/Users/joongwonkim/Project/신림사거리.txt"), storeBranch);

            // Convert object to JSON string
            String jsonInString = mapper.writeValueAsString(storeBranch);
            System.out.println(jsonInString);

            // Convert object to JSON string and pretty print
            jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(storeBranch);
            System.out.println(jsonInString);

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private StoreBranch createDummy() {


        StoreData storeData = new StoreData();
        StoreBranch storeBranch = new StoreBranch(storeData, "보라메공원점");
        storeData.registerObserver(storeBranch);
        storeData.notifyObservers();
        storeBranch.setLatitude(37.487258);
        storeBranch.setLongitude(126.926935);
        storeBranch.setAddress("서울특별시 관악구 신림동 1467-12");



        return storeBranch;

    }

    private Staff createDummyObject() {

        Staff staff = new Staff();

        staff.setName("mkyong");
        staff.setAge(33);
        staff.setPosition("Developer");
        staff.setSalary(new BigDecimal("90000"));

        List<String> skills = new ArrayList<>();
        skills.add("java");
        skills.add("python");

        staff.setSkills(skills);

        return staff;

    }

}
