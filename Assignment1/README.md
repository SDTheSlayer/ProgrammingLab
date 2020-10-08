##### Commands are assumed to be run from the root of the folder provided.
##### Hence the folder from which commands are run should contain src and resources folder , as well as "forms_rt.jar".

The source code is compiled used java 11 (Openjdk  "11.0.8").
***
## **1) Sock Matching Robot:**

To execute the program run the following commands from the terminal.

`$javac src/Q1SockMatching/*.java`

`$java -classpath ./src/  Q1SockMatching.Main`

(The .class files will pe produced in the same folder as the source files)

- The input file will be located at ./resources/Q1SockMatching/input.txt. The format of the input file is as follows:
    - First line contains an integer specifying the number of robot arms.
    - All subsequent lines contain socks (can have multiple socks in the same line)
- Sock are identified by their color in the input file with the following scheme   
    - 1 : White
    - 2 : Black
    - 3 : Blue
    - 4 : Grey  
    
- The socks are automatically assigned IDs based on the order in the file. (Incremental counter starting from 1)
- The output of the program will display the socks picks up by each Robot arm and the IDs of the socks of same color matched by the Matching Machine.
- The final output will contain the number of pairs of socks in the Shelf once all possible matchings are done. 
****

## **2) Data Modification System:**

To execute the program run the following commands from the terminal:

`$javac src/Q2DataModification/*.java`

`$java -classpath ./src/  Q2DataModification.Main`

(The .class files will pe produced in the same folder as the source files)

The input file will be located at ./resources/Q2DataModification/stud_info.txt. It is a comma seperated file with the following columns in order (No columns headings):
1. Roll_No
1. Name
1. Mail_id
1. Marks
1. Teacher_Code

The files sorted by name and roll number can be found at ./resources/Q2DataModification/ with the names stud_info_name_sorted.txt and stud_roll_name_sorted.txt respectively

After this follow along the instructions from terminal.The following sequence of actions may be taken: 

-  "1] Schedule an Update" -> It can be used for scheduling updates. Since we provide updates in batch format we can keep on selecting this option to add more updates to the current batch.
    -   The Teachers name will be asked, which will have to be one of TA1/TA2/CC.
    -   Next enter the Roll number of the Student. (Note since Data is only read from file while executing the updates, a roll number which doesn't exist will be accepted here, however will flash a failure to update while executing updates .)
    -   Next Choose if you wish to increse or decrese marks.
    -   Finally enter by how much to change the marks.
- To schedule multiple updates repeat the first step.
- To execute the pending updates select "2] Execute all pending updates". This will read the data from the file and execute all the valid updates.
- To exit select option 3.


Here is sample output and input format if TA1 wants to increase 170101086's marks by  5.


        Choose an operation to perform: 
               1] Schedule an Update
               2] Execute all pending updates
               3] Exit
        1
        Enter Teacher's Code: 
        CC
        Enter Roll Number : 
        170101086
        Chose how to update Marks 
           1] Increase marks 
           2] Decrease marks 
        1
        Increase the marks by: 
        5
        Update Scheduled Successfully!
        Choose an operation to perform: 
               1] Schedule an Update
               2] Execute all pending updates
               3] Exit
        2
        Marks for Roll No 170101086 successfully updated by CC with a modification of 5 marks. Current Marks: 74
        Choose an operation to perform: 
               1] Schedule an Update
               2] Execute all pending updates
               3] Exit




## **3) Calculator for Differently Abled Persons:**
    
To execute the program run the following commands from the terminal:

`$javac -cp "forms_rt.jar" src/Q3Calculator/*.java`

`$java -classpath ./src:./forms_rt.jar Q3Calculator.Main`

("forms_rt.jar" is needed since the GUI form was created using InteliJ's IDEA, which uses custom jar files for simpler display control.)

On Running the Program you can select one of the following control schemes to follow:
1. ENTER key is used for selecting both numbers and operators and the calculate and clear button. Starting from the numberpad, the number and function pads are alternatively highlighted.
2. A button for both numberpad and lockpad are simultaneously highlighted. SPACE bar for selecting from the fucntion pad and ENTER for selecting from numpad.

- Mouse can be used for clicking on buttons irrespective of the control scheme chosen.
- For calculating an expression use the Calculate button.
- You can either use the result of previous calculation or clear it using CLear button.
- Close the Calculator window to exit

(Note: Since I wrote this program initially in java 8, There might be a warning displayed if running in Java 11 or higher. You can ignore it since it doesnt affect the running of the program.)
