package util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
/*
Copyright 2017 Javaparser

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
public class NoCommentJavacodeParser {
	
	public static String parseToJavaCodeWithJavaDoc(Path javaCodeWithComments) {
	    // möglichkeoit: paket übergeben dann (bookmarks) classen finden dann methoden finden aber wie dann javadoc extrahieren und vergliechen?
	    //reflection findet methoden aus klassen... hilft das weiter?
	    try {
            String javaCodeWithoutComments = parse(javaCodeWithComments);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	    
	    
	    File fileWithComments=  javaCodeWithComments.toFile();

    
		List<String> meth = new ArrayList<String>();
		Method[] mthd = Class.class.getDeclaredMethods();
		for(Method m : mthd) {
			meth.add(m.toString());
		}
		return meth;
		
//		try {
//			String withoutComments = parse(javaCodeWithComments);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		}
		
	/**
	 * Gets a file and removes all of the comments - javadoc-,block- and linecomments
	 * @param javaCodeWithComments
	 * @return
	 * @throws ParseException
	 */
	private static String parse(Path javaCodeWithComments) throws ParseException {
		FileInputStream fis;
		try {
			fis = new FileInputStream(javaCodeWithComments.toFile());
			CompilationUnit cu = JavaParser.parse(fis);
	        removeComments(cu);
	        return cu.toString();
	    } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
        //InputStream in = new ByteArrayInputStream(javaCodeWithComments.getBytes(StandardCharsets.UTF_8));
    }
	
	private static String addJavaDocToCodeWithoutComments(String noCommentFile, String withCommentFile) {
		return null;
	}
	
	/**
	 * @param node
	 */
	private static void removeComments(Node node) {
        for (Node child : node.getChildrenNodes()) {
            child.setComment(null);
            removeComments(child);
        }
    }
	
}
