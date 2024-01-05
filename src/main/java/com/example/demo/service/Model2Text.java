package com.example.demo.service;

import org.eclipse.epsilon.egl.EglTemplateFactoryModuleAdapter;
import org.eclipse.epsilon.egl.IEglModule;
import org.eclipse.epsilon.emc.emf.InMemoryEmfModel;
import org.springframework.stereotype.Service;

import com.example.demo.utils.FileReader;

import java.io.File;

@Service
public class Model2Text {
	private String model2Text = FileReader.readFile("classpath:/static/uploads/docker-compose.egl");


    public String model2TextTransformer(InMemoryEmfModel targetModel) throws Exception {
        IEglModule module = (IEglModule) new EglTemplateFactoryModuleAdapter();
        System.out.println("the problem is here");

        module.parse(model2Text, new File("classpath:/static/uploads/program.egl"));
        System.out.println("progrma.egl is added");
        
        if (!module.getParseProblems().isEmpty()) {
            throw new RuntimeException(module.getParseProblems().get(0).toString());
            
        }
        System.out.println("1");
        module.getContext().getModelRepository().addModel(targetModel);
        System.out.println("2");
        String result = module.execute() + "" ;
        
        System.out.println("modl to text : "+ result);
        return result;

    }

}

