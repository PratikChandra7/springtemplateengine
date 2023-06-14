package com.niveus.springtemplateengine.controller;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.niveus.springtemplateengine.util.Utility;

@RestController
public class TemplateEngineController {
	
	@Autowired
    ResourceLoader resourceLoader;
	
	@Autowired
	Utility utility;
	
	@GetMapping("/test")
	public String health() {
		return " Health Check...";
	}
	
	@GetMapping("/template")
	public void processTemplate() {
	
		BufferedReader reader;
		
		try {
			
			Resource resourceInput = resourceLoader.getResource("classpath:templates/springbootinput.properties");
			File fileInput = resourceInput.getFile();
			
			TreeMap<String, String> map = utility.getProperties(fileInput);
	        System.out.println(map);
	           
			Resource resource = resourceLoader.getResource("classpath:templates/DockerSpringBoot.txt");
			File file = resource.getFile();

	
			String content = new String(Files.readAllBytes(file.toPath()));
			//System.out.println(" content "+content);
			
		    String result = StringSubstitutor.replace(content, map, "${", "}");
		    System.out.println("result "+result);
		    
		    
		    Resource resourceOutPut = resourceLoader.getResource("classpath:templates/Dockerfile");
			File fileNew = resourceOutPut.getFile();
			
		    
		    if (fileNew.createNewFile()) {
		        System.out.println("File is created!");
		    } else {
		        System.out.println("File already exists.");
		    }
		    System.out.println(fileNew.getAbsolutePath());
		    
		   // Resource resourceOutput = resourceLoader.getResource("classpath:templates/Dockerfile");
		  
		    Path textFilePath = Paths.get(fileNew.toURI());
		    
		    byte[] strToBytes = result.getBytes();

		    Files.write(textFilePath, strToBytes);
		    
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
