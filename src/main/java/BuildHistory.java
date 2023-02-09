import org.apache.maven.model.Build;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class BuildHistory {

    public void createHistoryFile(HttpServletResponse response, String commitIdentifier, String description) throws IOException {
        String datetime = getTime();
        String historyDirectoryPath = "web/history"; //Set the path to the history directory
        String historyFilePath = historyDirectoryPath + "/" + datetime + ".txt"; //Set the path to the history file, including name

        File historyFile = new File(historyFilePath);
        if (!historyFile.exists()) { //If the file does not exist
            historyFile.createNewFile(); //Create the history file
            writeBuildInfo(historyFilePath,commitIdentifier, datetime, description);
            response.getWriter().println("<h1>New file created: /" + datetime + ".txt</h1>");
            response.getWriter().println("<h2><a href=/history>History explorer</a></h2><br>");
        }else{ //If the file exists
            response.getWriter().println("<h1>A file with the same name does already exist.</h1>");
        }
    }

    private String getTime() {
        String datetime = java.time.LocalDateTime.now().toString(); //Get the current date and time
        datetime = datetime.replace(":", "-"); //Replace the ":" with "," to make the file name valid
        datetime = datetime.substring(0, 19); //Remove the milliseconds from the date and time
        System.out.println(datetime); //Print the current date and time
        return datetime;
    }

    private void writeBuildInfo(String filepath, String commitIdentifier, String buildDate, String description) throws IOException {
        FileWriter writer = new FileWriter(filepath);
        writer.write("Commit identifier: "+commitIdentifier + "\n");
        writer.write("Build date:" +buildDate + "\n");
        writer.write("Build log: " + description + "\n");
        writer.write("Test log: \n");
        File test_log_dict = new File("cloned/target/surefire-reports");
        File[] test_log_files = test_log_dict.listFiles();
        for (File test_log : test_log_files) {
            if (test_log.isFile() && test_log.getName().endsWith(".txt")) {
                Scanner scanner = new Scanner(new File("cloned/target/surefire-reports/" + test_log.getName()));
                while (scanner.hasNextLine()) {
                    writer.write(scanner.nextLine() + "\n");
                }
            }
        }

        writer.close();
        System.out.println("Successfully wrote to the file.");



    }

    public void navigateHistory(HttpServletResponse response, String target) throws IOException {
        String historyDirectoryPath = "web/history"; //Set the path to the history directory
        //Code for the web interface
        if (target.equals("/history")) { //If the user navigates to /history
            File historyDirectory = new File(historyDirectoryPath);
            File[] historyFiles = historyDirectory.listFiles();

            response.getWriter().println("<h1>CI history explorer</h1><br><br>");
            for (File file : historyFiles) { //For each file in the history directory
                if (file.isFile()) { //If the file is a file and not a directory
                    //Basic html code is used to display the files
                    response.getWriter().println("<a href=/history/" + file.getName() + ">" + file.getName() + "</a><br><br>");
                }
            }

        } else if (target.startsWith("/history/")) { //If the user navigates to /history/...
            //String historyDirectoryPath = "web/history"; //Set the path to the history directory

            String fileName = target.substring(9); //Get the file name from the url (starts at position 9), will be used to check if the file exists

            //If the file exists, display it otherwise display an error message
            response.getWriter().println("<h1>CI history explorer</h1>");
            if (new File(historyDirectoryPath + "/" + fileName).exists()) { //Check if the file exists
                response.getWriter().println("<h2>Currently at: " + fileName + "</h2>"); //Print the file name
                response.getWriter().println("<h2><a href=/history>Back</a></h2><br><br>");
                //Read the file into a string and display it
                //String fileContent = new String(Files.readAllBytes(Paths.get(historyDirectoryPath + "/" + fileName)));
                Scanner scanner = new Scanner(new File(historyDirectoryPath + "/" + fileName));
                while (scanner.hasNextLine()) {
                    response.getWriter().println("<div>" + scanner.nextLine() + "</div>");
                }

                //response.getWriter().println(fileContent);
            } else { //If the file does not exist display an error message
                response.getWriter().println("<h2><a href=/history>Back</a></h2><br><br>");
                response.getWriter().println("<b>ERROR: File not found</b>");
            }

        } else {
            response.getWriter().println("CI job done");
        }
    }


}
