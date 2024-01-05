package com.example.demo.service;
import jakarta.annotation.PostConstruct;

import org.eclipse.epsilon.emc.emf.InMemoryEmfModel;
import org.eclipse.epsilon.etl.EtlModule;
import java.io.IOException;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.springframework.stereotype.Service;

import com.example.demo.utils.FileReader;
import com.example.demo.utils.ModelLoader;

@Service
public class Model2Model {
    private String configCode;
    private String  dockerCompose;
    private String transformation;



    public  InMemoryEmfModel model2ModelTransformation(String inputFlexmi) throws Exception {
        EtlModule module = new EtlModule();
        module.parse(transformation);
        if(!module.getParseProblems().isEmpty()){
            throw new RuntimeException(module.getParseProblems().get(0).toString());
        }
        module.getContext().setOutputStream(System.out);
        return runTransformation(
                module,
                inputFlexmi,
                dockerCompose,
                configCode
        );
    }

    private InMemoryEmfModel runTransformation(EtlModule module, String inputFlexmi, String inputEmfatic, String dockerComposeEmfatic)
            throws IOException, EolRuntimeException {
        System.out.println("runTransformation");
        InMemoryEmfModel inputModel = ModelLoader.getInMemoryFlexmiModel(inputFlexmi, inputEmfatic);
        inputModel.setName("Source");
        InMemoryEmfModel dockerComposeModel = ModelLoader.getBlankInMemoryModel(dockerComposeEmfatic);
        dockerComposeModel.setName("Target");
        module.getContext().getModelRepository().addModel(inputModel);
       
        module.getContext().getModelRepository().addModel(dockerComposeModel);
        System.out.println("5");
        module.execute();
        System.out.println("6");
        return dockerComposeModel;
    }

    @PostConstruct
    public void loadFiles()
    {

    	configCode = FileReader.readFile("classpath:static/uploads/configFile.emf");
    	dockerCompose = FileReader.readFile("classpath:static/uploads/docker-compose.emf");
    	transformation = FileReader.readFile("classpath:static/uploads/Config2Docker.etl");

    }
}