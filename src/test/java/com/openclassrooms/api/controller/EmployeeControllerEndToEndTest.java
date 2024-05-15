package com.openclassrooms.api.controller;

import static org.hamcrest.CoreMatchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerEndToEndTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void testGetEmployeesEndToEnd() throws Exception {

    // mockMvc.perform(get("/employees"))
    // .andExpect(status().isOk())
    // // Requires this option in application.properties (if not data will not be
    // // loaded): spring.jpa.defer-datasource-initialization: true
    // .andExpect(jsonPath("$[0].firstName", is("Laurent")));
  }
}