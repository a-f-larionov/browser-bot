package taplinkbot.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class IndexControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testJSReachable() throws Exception {

        mockMvc.perform(get("/app.js"))
                .andExpect(status().isOk());
    }
}