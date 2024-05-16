package com.openclassrooms.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationUnitTest {

  // @Autowired
  // private MockMvc mockMvc;

  @Test
  public void testGetEmployeesMocked() throws Exception {
    // mockMvc.perform(get("/employees"))
    // .andExpect(status().isOk());
  }

}