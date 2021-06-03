package browserbot.dto;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import browserbot.entities.Manager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ManagerDtoTest {

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void testMapOne() {

        long managerId = 1123;
        boolean working = true;
        String comment = "comment";
        String phone = "+79776117568";

        Manager manager = new Manager();
        manager.setId(managerId);
        manager.setWorking(working);
        manager.setComment(comment);
        manager.setPhone(phone);

        ManagerDto managerDto = modelMapper.map(manager, ManagerDto.class);

        assertThat(managerDto.getId()).isEqualTo(manager.getId());
        assertThat(managerDto.getPhone()).isEqualTo(manager.getPhone());
        assertThat(managerDto.getComment()).isEqualTo(manager.getComment());
        assertThat(managerDto.isWorking()).isEqualTo(manager.isWorking());
    }
}