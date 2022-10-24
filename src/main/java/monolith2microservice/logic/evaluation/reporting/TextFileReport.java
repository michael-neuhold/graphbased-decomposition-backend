package monolith2microservice.logic.evaluation.reporting;

import monolith2microservice.shared.models.git.GitRepository;
import monolith2microservice.shared.models.graph.ClassNode;
import monolith2microservice.shared.models.graph.Component;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class TextFileReport {

	private final static String homePath = System.getProperty("user.home");

	public static void generate(GitRepository repo, Set<Component> components) throws Exception {
		Path outputPath = Paths.get(homePath + "/" + repo.getName() + "_" + repo.getId()+ ".txt");
		
		BufferedWriter writer = new BufferedWriter(Files.newBufferedWriter(outputPath));
		
		
		writer.write("Microservice Decomposition for " + repo.getName());
		writer.newLine();
		writer.write("----------------------------------------------------------------");
		writer.newLine();
		writer.newLine();
		writer.write("Microservice id | Belonging classes");
		writer.newLine();
		writer.write("----------------------------------------------------------------");

		int counter = 0;
		for(Component m: components){
			writer.newLine();
			writer.write(counter + "  |  ");
			for(ClassNode cls : m.getNodes()){
				writer.write(cls.getId() + " ,");
			}
			counter++;
		}
		
		writer.close();

	}


	public static void writeToFile(String content, String fileName) throws Exception{
		Path outputPath = Paths.get(homePath + "/" + fileName);
		BufferedWriter writer = new BufferedWriter(Files.newBufferedWriter(outputPath));
		writer.write(content);
		writer.close();
	}

}
