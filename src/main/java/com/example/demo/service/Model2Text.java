package com.example.demo.service;

import com.example.demo.utils.FileReader;
import org.eclipse.epsilon.egl.EglTemplateFactoryModuleAdapter;
import org.eclipse.epsilon.egl.IEglModule;
import org.eclipse.epsilon.emc.emf.InMemoryEmfModel;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class Model2Text {
    private String model2Text = FileReader.readFile("static/uploads/docker-compose.egl");

    public String model2TextTransformer(InMemoryEmfModel targetModel) throws Exception {
        IEglModule module = (IEglModule) new EglTemplateFactoryModuleAdapter();
        System.out.println("the problem is here");

        module.parse(model2Text, new File("/program.egl"));
        System.out.println("progrma.egl is added");
        if (!module.getParseProblems().isEmpty()) {
            throw new RuntimeException(module.getParseProblems().get(0).toString());
        }
        module.getContext().getModelRepository().addModel(targetModel);
        String result = module.execute() + "" ;
        System.out.println("modl to text : "+result);
        return result;

    }

}

