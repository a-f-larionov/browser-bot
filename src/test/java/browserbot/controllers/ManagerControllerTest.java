package browserbot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import browserbot.services.LangService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ManagerControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    LangService lang;

    @Test
    void listUnauthorizedForbidden() throws Exception {

        mvc.perform(get("/managers/list"))
                .andExpect(status()
                        .is(HttpStatus.FOUND.value()))
        ;
    }

    @Test
    @WithMockUser
    void listIsOk() throws Exception {

        mvc.perform(get("/managers/list"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void listIsJsonHeaders() throws Exception {

        mvc.perform(get("/managers/list"))
                .andExpect(header()
                        .string("Content-Type", "application/json"));
    }

    @Test
    @WithMockUser
    void listIsJsonLength() throws Exception {

        String content = mvc.perform(get("/managers/list"))
                .andReturn().getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();

        List<Object> list = objectMapper.readValue(content, List.class);

        assertThat(list.size()).isEqualTo(4);
    }

    @Test
    void worksSwitchIsUnAuthorizedForbidden() throws Exception {

        mvc.perform(get("/managers/works-switch"))
                .andExpect(status().is(HttpStatus.FOUND.value()));
    }

    @Test
    @WithMockUser
    void worksSwitchIsAuthorizedOk() throws Exception {

        mvc.perform(post("/managers/works-switch")
                .param("managerId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void worksSwitchDataRealChanged() throws Exception {

        mvc.perform(post("/managers/works-switch")
                .param("managerId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string(lang.get("managers.works_changes")));

    }
}