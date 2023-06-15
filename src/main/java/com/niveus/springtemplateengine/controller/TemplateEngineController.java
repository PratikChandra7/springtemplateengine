package com.niveus.springtemplateengine.controller;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeMap;

import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	public ResponseEntity<String> processTemplate(@RequestParam("appType") String appType) {
	
		BufferedReader reader;
		
		try {
			String inputFile = null;
			String templateFile = null;
			
			if("springboot".equalsIgnoreCase(appType)) {
				inputFile = "classpath:templates/springboot/springbootinput.properties";
				templateFile = "classpath:templates/springboot/DockerSpringBootTemplate.txt";
			}
			else if("nodejs".equalsIgnoreCase(appType)) {
				inputFile = "classpath:templates/nodejs/nodejsinput.properties";
				templateFile = "classpath:templates/nodejs/DockerNodeJsTemplate.txt";
			}
			
			Resource resourceInput = resourceLoader.getResource(inputFile);
			File fileInput = resourceInput.getFile();
			
			TreeMap<String, String> map = utility.getProperties(fileInput);
	        System.out.println(map);
	           
			Resource resource = resourceLoader.getResource(templateFile);
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
		
		
		 return new ResponseEntity<>("Dockerfile created.", HttpStatus.OK);
		
	}

}
