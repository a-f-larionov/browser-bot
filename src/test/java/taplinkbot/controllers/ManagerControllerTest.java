package taplinkbot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ManagerControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void listUnauthorizedForbidden() throws Exception {

        mvc.perform(get("/managers/list"))
                .andExpect(status()
                        .is(HttpStatus.NOT_FOUND.value()))
        ;
    }

    @Test
    @WithMockUser
    void listIsOk() throws Exception {

        mvc.perform(get("/managers/list"))
                .andExpect(status().isOk());
    }

    @Test
    void listIsJsonHeaders() throws Exception {

        mvc.perform(get("/managers/list"))
                .andExpect(header()
                        .string("Content-Type", "application/json"));
    }

    @Test
    void listIsJsonLength() throws Exception {

        class ManagersListRow {
            int id;
            String phone;
            String comment;
            boolean isWorking;
        }

        String content = mvc.perform(get("/managers/list"))
                .andReturn().getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();

        List<ManagersListRow> list = objectMapper.readValue(content, List.class);

        assertThat(list.size()).isEqualTo(4);
    }

    @Test
    void managerWorkingSwitch() {
        //@todo


    }
}