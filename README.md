William Kurek
CS 1538
Homework #1

HOW TO RUN PROGRAM:

    1. Ensure the following source files are in the same directory:
        RobotSimulation.java
        Robot.java
        Map.java
        Coordinate.java
        ProbabilityCompare.java
        Statistics.java
        
    2. On the command line (tested on a Windows OS) execute the following commands:   
        "javac RobotSimulation.java"
        "java RobotSimulation (.csv file) (strategy number)"
            i.e. "java RobotSimulation hw1map.csv 1"
            
            STRATEGY NUMBERS:
            1. Move toward the location where the material might appear (and reappear)
            with the highest probability and ramain there for the rest of the time.
            2. Randomly choose a direction and continue uutil the robot bumps into a wall/obstacle.
            3. Randomly choose a direction at every step.
            4. Always head toward a (randomly chosen) location where the material could appear.
            
REFERENCES:

    To determine the best and most efficient "shortest path" algorithm for this particular simulation,
    I did extensive research into many of the most important algorithms for solving this problem.
    
    https://en.wikipedia.org/wiki/Shortest_path_problem#Algorithms
    
    After thorough research into each, and the knowledge of others I've attained in previous
    courses, I determined the A-Star search algorithm would be best for the code that I 
    have already implemented.
    
    https://en.wikipedia.org/wiki/A*_search_algorithm
   

OVERVIEW:
    The goal of this experiment is to determine whether one of the strategies is better 
    than the others for the given map and if changing environmental conditions can alter those
    results. I believe strategy 4 will be the best because it is constantly moving towards 
    a location that can generate a material, while potentially harvesting other materials
    on the way.
    
EXPERIMENTAL DESIGN:
    My simulation program asks the user how many times they would like to run the particular
    simulation they entered as an argument. For this experiment, I decided to run each navigation
    strategy simulation 1000 times, to ensure the average doesn't move. After the simulation is run,
    the program outputs the average harvest and the variance for the 1000 runs. I will use this 
    valuable information to decide which navigation strategy is better. Also, I plan on adding more
    material to the environment to see if some strategies without a set path perform differently. Also,
    I plan to add more time to determine if some strategies perform poorly due to time restraints.

ANALYSIS:

    STRATEGY 1:
        AVERAGE: 2.797
        VARIANCE: .66979
        
    STRATEGY 2:
        AVERAGE: 7.013
        VARIANCE: 21.21883
        
    STRATEGY 3:
        AVERAGE: 7.274
        VARIANCE: 37.270924
        
    STRATEGY 4:
        AVERAGE: 18.779
        VARIANCE: 14.0141
        
        The results of the experiment correlate with my initial guesses. After many test runs to verify accurate
        and persistent results, I can trust the number I got.

FURTHER DISCUSSIONS:
    Although strategy 4 provides the best results for material harvesting, the programming proved more difficult 
    than the others. Since I needed to figure out a way to find the shortest path between locations, the implementation
    of a complex shortest path algorithm was essential.
            
         


