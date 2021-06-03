//FIN
package browserbot.controllers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import browserbot.BrowserBotException;
import browserbot.services.LangService;
import browserbot.dto.ManagerDto;
import browserbot.entities.Manager;
import browserbot.repositories.ManagerRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/managers/")
public class ManagerController {

    private final LangService lang;

    private final ManagerRepository managerRepository;

    private final ModelMapper modelMapper;

    @GetMapping("/list")
    public List<ManagerDto> list() {

        List<Manager> list = managerRepository.findAll();

        return list.stream()
                .map((Manager manager) -> modelMapper.map(manager, ManagerDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping("/works-switch")
    public ResponseEntity<String> managerWorkingSwitch(@RequestParam Long managerId) {

        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new BrowserBotException(
                        lang.get("managers.not_found"))
                );

        manager.setWorking(!manager.isWorking());

        managerRepository.save(manager);

        return ResponseEntity.ok(lang.get("managers.works_changes"));
    }
}
