package taplinkbot.controllers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import taplinkbot.dto.ManagerDto;
import taplinkbot.entities.Manager;
import taplinkbot.managers.ManagerRotator;
import taplinkbot.repositories.ManagerRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/managers/")
public class ManagerController {

    private final ManagerRepository managerRepository;

    private final ManagerRotator managerRotator;

    private final ModelMapper modelMapper;

    @GetMapping("/list")
    public List<ManagerDto> list() {

        List<Manager> list;

        List<ManagerDto> listDTO;

        list = managerRepository.findAll();

        listDTO = list.stream()
                .map((Manager manager) -> modelMapper.map(manager, ManagerDto.class))
                .collect(Collectors.toList());

        return listDTO;
    }

    @GetMapping("/works-switch")
    public ResponseEntity managerWorkingSwitch(@RequestParam Long managerId) {

        Optional<Manager> manager = managerRepository.findById(managerId);

        if (!manager.isPresent()) {
            return new ResponseEntity("Менеджер не найден.", HttpStatus.BAD_REQUEST);
        }

        managerRotator.switchManagerWorks(managerId);

        return ResponseEntity.ok("Менеджер сменил статус");
    }
}
