package com.openclassrooms.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerEndToEndTest {

  // @Autowired
  // private MockMvc mockMvc;

  @Test
  public void testGetEmployeesEndToEnd() throws Exception {
    // mockMvc.perform(get("/employees"))
    // .andExpect(status().isOk())
    // // Requires this option in application.properties (if not data will not be
    // // loaded): spring.jpa.defer-datasource-initialization: true
    // .andExpect(jsonPath("$[0].firstName", is("Laurent")));
  }
}