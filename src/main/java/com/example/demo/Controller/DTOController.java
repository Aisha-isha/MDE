package com.example.demo.Controller;

import com.example.demo.dto.ModelingDTO;
import com.example.demo.service.Model2Model;
import com.example.demo.service.Model2Text;
import org.eclipse.epsilon.emc.emf.InMemoryEmfModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DTOController {
    @Autowired
    private Model2Model model2Model;
    @Autowired
    private Model2Text model2Text;

    @PostMapping("/forwards")
    public ResponseEntity<?> forwardEngineering(@RequestBody ModelingDTO modelingDTO) throws Exception {

            InMemoryEmfModel targetModel = model2Model.model2ModelTransformation(modelingDTO.getConfigFile());
            String generatedConfigFile = model2Text.model2Text(targetModel);
            System.out.println("ayaaaat");
            return ResponseEntity.ok(generatedConfigFile);
        }

    }

